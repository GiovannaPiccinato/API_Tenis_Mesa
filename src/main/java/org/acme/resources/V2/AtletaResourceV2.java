package org.acme.resources.V2;

import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.AtletaDTO;
import org.acme.DTO.AtletaEntradaDTO;
import org.acme.DTO.v2.AtletaBatchDTO;
import org.acme.DTO.v2.AtletaTecnicoUpdateDTO;
import org.acme.entitys.Atleta;
import org.acme.entitys.Competicao;
import org.acme.entitys.Tecnico;
import org.acme.service.IdempotencyService;
import org.acme.utils.Messages;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Path("/v2/atletas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RateLimit(
        value = 5,
        window = 10,
        minSpacing = 5,
        windowUnit = ChronoUnit.MINUTES
)
@APIResponses({
        @APIResponse(responseCode = "200", description = "Atletas encontrados com sucesso",
                content = @Content(mediaType = "application/json", schema= @Schema(implementation = AtletaDTO.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "204", description = "Nenhum atleta encontrado")
})
public class AtletaResourceV2 implements Messages {
    @Inject
    IdempotencyService idempotencyService;

    @GET
    @Operation(
            summary = "Listar todos os atletas",
            description = "Essa rota lista todos os atletas cadastrados no sistema."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Atletas encontrados com sucesso",
                    content = @Content(mediaType = "application/json", schema= @Schema(implementation = AtletaDTO.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "204", description = "Nenhum atleta encontrado")
    })
    public Response listarAtletas() {
        List<Atleta> atletas = Atleta.listAll();
        if (atletas.isEmpty()) {
            return Response.noContent().build();
        }

        atletas.forEach(a -> {
            if (a.tecnico != null) {
                a.tecnico.nome.length();
            }
            if (a.competicoes != null) {
                a.competicoes.forEach(c -> {
                    c.nome.length();
                });
            }
        });

        List<AtletaDTO> dtos = atletas.stream()
                .map(AtletaDTO::new)
                .toList();

        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Buscar atleta por ID",
            description = "Busca um atleta pelo ID. Retorna 404 se não encontrado."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Atleta encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Atleta.class))),
            @APIResponse(responseCode = "404", description = "Atleta não encontrado"),
            @APIResponse(responseCode = "400", description = "Dados Inválidos"),
    })
    public Response buscarAtletaByID(
            @Parameter(description = "ID do atleta", required = true) @PathParam("id") Long id) {

        Atleta atleta = Atleta.findById(id);

        if (atleta == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_NAO_ENCONTRADO_ID))
                    .build();
        }
        return Response.ok(atleta).build();
    }

    @POST
    @Transactional
    public Response cadastrarAtleta(@Valid AtletaEntradaDTO dto) {
        try {
            if (dto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON(Messages.MSG_VAZIO))
                        .build();
            }
            Atleta existente = Atleta.find("nome", dto.nome).firstResult();
            if (existente != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON("Já existe um atleta com este nome"))
                        .build();
            }
            Tecnico tecnico = null;
            if (dto.tecnicoId != null) {
                tecnico = Tecnico.findById(dto.tecnicoId);
                if (tecnico == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity(mensagemToJSON("Técnico não encontrado para o ID: " + dto.tecnicoId))
                            .build();
                }
            }

            List<Competicao> competicoes = Collections.emptyList();
            if (dto.competicaoIds != null && !dto.competicaoIds.isEmpty()) {
                competicoes = Competicao.listByIds(dto.competicaoIds);
                if (competicoes.size() != dto.competicaoIds.size()) {
                    List<Long> idsNaoEncontrados = new ArrayList<>(dto.competicaoIds);
                    competicoes.forEach(c -> idsNaoEncontrados.remove(c.getCompeticao_id()));

                    return Response.status(Response.Status.NOT_FOUND)
                            .entity(mensagemToJSON("Competições não encontradas para os IDs: " + idsNaoEncontrados))
                            .build();
                }
            }

            Atleta atleta = new Atleta();
            atleta.nome = dto.nome;
            atleta.idade = dto.idade;
            atleta.rankingNacional = dto.rankingNacional;
            atleta.tecnico = tecnico;
            atleta.competicoes = competicoes;

            atleta.persist();

            return Response.status(Response.Status.CREATED)
                    .entity(new AtletaDTO(atleta))  // mostrar o nome do técnico
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(mensagemToJSON("Erro interno ao cadastrar atleta"))
                    .build();
        }
    }

    @GET
    @Path("/{id}/competicoes")
    @Operation(
            summary = "Listar as competições de um atleta especifico",
            description = "Essa rota lista todas as competições de um atleta via ID."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Competições do atleta encontradas com sucesso",
                    content = @Content(mediaType = "application/json", schema= @Schema(implementation = AtletaDTO.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "204", description = "Nenhuma competição encontrada")
    })
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    public Response listarCompeticoesAtleta(@PathParam("id") Long id) {
        Atleta atleta = Atleta.findById(id);
        if (atleta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(atleta.getCompeticoes()).build();
    }

    @PUT
    @Path("/{id}/tecnico")
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Transactional
    public Response atualizarTecnico(
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @PathParam("id") Long atletaId,
            @Valid AtletaTecnicoUpdateDTO dto) {

        // Verifica idempotência
        Response idempotencyResponse = idempotencyService.checkIdempotency(idempotencyKey);
        if (idempotencyResponse != null) {
            return idempotencyResponse;
        }

        Atleta atleta = Atleta.findById(atletaId);
        if (atleta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Tecnico tecnico = Tecnico.findById(dto.getTecnicoId());
        if (tecnico == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Técnico não encontrado\"}").build();
        }

        atleta.setTecnico(tecnico);
        atleta.persist();

        idempotencyService.registerIdempotency(idempotencyKey, atleta);

        return Response.ok(atleta).build();
    }

    @POST
    @Path("/batch")
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Transactional
    public Response criarAtletasEmLote(
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @Valid List<AtletaBatchDTO> atletasDTO) {

        Response idempotencyResponse = idempotencyService.checkIdempotency(idempotencyKey);
        if (idempotencyResponse != null) {
            return idempotencyResponse;
        }

        List<Atleta> atletasCriados = atletasDTO.stream()
                .map(dto -> {
                    Atleta atleta = new Atleta();
                    atleta.setNome(dto.getNome());
                    atleta.setIdade(dto.getIdade());
                    atleta.setRankingNacional(dto.getRankingNacional());
                    atleta.persist();
                    return atleta;
                })
                .toList();

        idempotencyService.registerIdempotency(idempotencyKey, atletasCriados);

        return Response.status(Response.Status.CREATED)
                .entity(atletasCriados)
                .build();
    }
    @Override
    public String mensagemToJSON(String msg) {
        return "{\"message\": \"" + msg + "\"}";
    }
}


