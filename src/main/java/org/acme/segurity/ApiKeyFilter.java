package org.acme.segurity;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

@Provider
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION)
public class ApiKeyFilter implements ContainerRequestFilter {

    @Inject
    @ConfigProperty(name = "quarkus.api-key.value")
    String expectedApiKey;

    @Inject
    @ConfigProperty(name = "quarkus.api-key.header-name")
    String apiKeyHeaderName;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Permite rotas públicas sem validação
        if (isPublicRoute(requestContext.getUriInfo().getPath())) {
            return;
        }

        // Pega o valor da key no application
        String apiKey = requestContext.getHeaderString(apiKeyHeaderName);

        // Se não houver a key no, retorna 401
        if (apiKey == null || apiKey.isBlank()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"API key obrigatória\"}").build());
            return;
        }

        // Se a API key for diferente, retorna 401
        if (!expectedApiKey.equals(apiKey)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"API key inválida ou ausente\"}").build());
        }
    }

    private boolean isPublicRoute(String path) {
        return path.contains("/v1/") || path.startsWith("/health")
                || path.startsWith("/metrics");
    }
}
