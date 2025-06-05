package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class IdempotencyService {

    private final Map<String, Object> idempotencyCache = new ConcurrentHashMap<>();

    public Response checkIdempotency(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return null;
        }

        Object cachedResponse = idempotencyCache.get(idempotencyKey);
        if (cachedResponse != null) {
            return Response.ok(cachedResponse)
                    .status(Response.Status.CONFLICT)
                    .header("Idempotent-Replay", "true")
                    .build();
        }
        return null;
    }

    public void registerIdempotency(String idempotencyKey, Object response) {
        if (idempotencyKey != null && !idempotencyKey.isEmpty()) {
            idempotencyCache.put(idempotencyKey, response);
        }
    }
}
