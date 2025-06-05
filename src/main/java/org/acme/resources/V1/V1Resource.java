package org.acme.resources.V1;

import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;

public class V1Resource {
    @Path("atletas")
    @Operation(
            summary = "Rotas referente a atletas",
            description = "Essa rota é responsável por gerenciar os atletas do sistema."
    )
    public Class<AtletaResource> atletas() {
        return AtletaResource.class;
    }

    @Path("competicoes")
    @Operation(
            summary = "Rotas referente a competições",
            description = "Essa rota é responsável por gerenciar as competições do sistema."
    )
    public Class<CompeticaoResource> competicoes() {
        return CompeticaoResource.class;
    }

    @Path("tecnicos")
    @Operation(
            summary = "Rotas referente a tecnicos",
            description = "Essa rota é responsável por gerenciar as inscrições dos tecnicos nos eventos."
    )
    public Class<TecnicoResource> tecnicos() {
        return TecnicoResource.class;
    }
}
