package org.acme.requestConfig;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Provider
public class RateLimitingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final long WINDOW_MILLIS = 60000; // 1 minuto
    private static final String HEADER_LIMIT = "X-RateLimit-Limit";
    private static final String HEADER_REMAINING = "X-RateLimit-Remaining";

    private static final Map<String, Window> ipWindows = new ConcurrentHashMap<>();

    private static final Map<String, Integer> ENDPOINT_LIMITS = Map.of(
            "/v2/atletas", 50
    );

    private static final int DEFAULT_LIMIT = 100;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String ip = getClientIp(requestContext);
        String path = normalizePath(requestContext.getUriInfo().getPath());
        int limit = ENDPOINT_LIMITS.getOrDefault(path, DEFAULT_LIMIT);

        long now = Instant.now().toEpochMilli();
        String key = ip + ":" + path;

        Window window = ipWindows.computeIfAbsent(key, k -> new Window());
        synchronized (window) {
            if (now - window.windowStart > WINDOW_MILLIS) {
                window.count = 0;
                window.windowStart = now;
            }
            window.count++;
            if (window.count > limit) {
                Map<String, Object> errorResponse = Map.of(
                        "error", "Você ultrapassou o limite de requisições.",
                        "tente novamente", WINDOW_MILLIS / 1000 + " segundos",
                        "status", 429
                );

                requestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                        .header(HEADER_LIMIT, limit)
                        .header(HEADER_REMAINING, 0)
                        .entity(errorResponse)
                        .build());
                return;
            }
        }
        requestContext.setProperty("rateLimit", limit);
        requestContext.setProperty("rateLimitKey", key);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object limitObj = requestContext.getProperty("rateLimit");
        Object keyObj = requestContext.getProperty("rateLimitKey");

        int limit = DEFAULT_LIMIT;
        String key = null;

        if (limitObj instanceof Integer) {
            limit = (Integer) limitObj;
        }
        if (keyObj instanceof String) {
            key = (String) keyObj;
        }

        int remaining = limit;
        if (key != null) {
            Window window = ipWindows.get(key);
            if (window != null) {
                remaining = Math.max(0, limit - window.count);
            }
        }

        responseContext.getHeaders().add(HEADER_LIMIT, limit);
        responseContext.getHeaders().add(HEADER_REMAINING, remaining);
    }

    private String getClientIp(ContainerRequestContext requestContext) {
        String ip = requestContext.getHeaderString("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim();
        }
        ip = requestContext.getHeaderString("X-Real-IP");
        if (ip != null && !ip.isEmpty()) {
            return ip;
        }
        return requestContext.getUriInfo().getRequestUri().getHost(); // fallback final
    }

    private String normalizePath(String path) {
        return path.replaceAll("/\\d+", "/{id}");
    }

    private static class Window {
        long windowStart = Instant.now().toEpochMilli();
        int count = 0;
    }
}
