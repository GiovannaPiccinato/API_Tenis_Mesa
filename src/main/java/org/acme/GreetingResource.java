package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.resources.V1.V1Resource;
import org.acme.resources.V2.V2Resource;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/")
public class GreetingResource {

    @Deprecated
    @Path("v1")
    @Operation(
            summary = "Versão 1 da API",
            description = "Essa rota é a versão 1 da API"
    )
    public Class<V1Resource> v1() {
        return V1Resource.class;
    }

    @Path("v2")
    @Operation(
            summary = "Versão 2 da API",
            description = "Essa rota é a versão 2 da API"
    )
    public Class<V2Resource> v2() {
        return V2Resource.class;
    }


}