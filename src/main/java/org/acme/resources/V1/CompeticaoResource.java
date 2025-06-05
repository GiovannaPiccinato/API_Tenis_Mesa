package org.acme.resources.V1;

import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.CompeticaoDTO;
import org.acme.DTO.CompeticaoEntradaDTO;
import org.acme.entitys.Atleta;
import org.acme.entitys.Competicao;
import org.acme.utils.Messages;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompeticaoResource implements Messages {

    @GET
    @Operation(
            summary = "Listar todas as competições",
            description = "Essa rota lista todas as competições cadastradas no sistema."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Competições encontradas com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompeticaoDTO.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "204", description = "Nenhuma competição encontrada")
    })
    public Response listarCompeticao() {
        List<Competicao> competicoes = Competicao.listAll();
        if (competicoes.isEmpty()) {
            return Response.noContent().build();
        }
        competicoes.forEach(c -> {
            if (c.atletas != null) {
                c.atletas.size();
            }
        });
        List<CompeticaoDTO> dtos = competicoes.stream().map(CompeticaoDTO::new).toList();
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Buscar competição por ID",
            description = "Busca uma competição pelo ID. Retorna 404 se não encontrada."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Competição encontrada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompeticaoDTO.class))),
            @APIResponse(responseCode = "404", description = "Competição não encontrada")
    })
    public Response buscarPorId(
            @Parameter(description = "ID da competição", required = true) @PathParam("id") Long id
    ) {
        Competicao competicao = Competicao.findById(id);
        if (competicao == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(MSG_NAO_ENCONTRADO_ID))
                    .build();
        }

        if (competicao.atletas != null) {
            competicao.atletas.size();
        }
        return Response.ok(new CompeticaoDTO(competicao)).build();
    }

    @POST
    @Operation(
            summary = "Cadastrar nova competição",
            description = "Cadastra uma nova competição no sistema. Retorna 201 se sucesso, 400 se erro."
    )
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Competição cadastrada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompeticaoDTO.class))),
            @APIResponse(responseCode = "400", description = "Erro ao cadastrar competição")
    })
    @Transactional
    public Response cadastrarCompeticao(@NotNull @Valid CompeticaoEntradaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON(MSG_VAZIO))
                    .build();
        }

        Competicao existente = Competicao.find("nome", dto.nome).firstResult();
        if (existente != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON("Já existe uma Competicao com este nome"))
                    .build();
        }

        Competicao competicao = new Competicao();
        competicao.nome = dto.nome;
        competicao.local = dto.local;
        competicao.ano = dto.ano;

        if (dto.atletasIds != null && !dto.atletasIds.isEmpty()) {
            List<Atleta> atletasEncontrados = Atleta.list("id in ?1", dto.atletasIds);
            if (atletasEncontrados.size() != dto.atletasIds.size()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON("Atleta(s) não encontrado(s)"))
                        .build();
            }
            competicao.atletas = atletasEncontrados;
        }

        competicao.persist();

        return Response.status(Response.Status.CREATED)
                .entity(new CompeticaoDTO(competicao))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Atualizar competição",
            description = "Atualiza os dados da competição pelo ID. Retorna 404 se não encontrada."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Competição atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompeticaoDTO.class))),
            @APIResponse(responseCode = "404", description = "Competição não encontrada"),
            @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    @Transactional
    public Response atualizar(
            @Parameter(description = "ID da competição", required = true) @PathParam("id") Long id,
            @NotNull @Valid CompeticaoEntradaDTO dto) {

        Competicao competicao = Competicao.findById(id);
        if (competicao == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(MSG_ATUALIZADO_ERRO))
                    .build();
        }

        competicao.nome = dto.nome;
        competicao.local = dto.local;
        competicao.ano = dto.ano;

        if (dto.atletasIds != null) {
            List<Atleta> atletasEncontrados = Atleta.list("id in ?1", dto.atletasIds);
            if (atletasEncontrados.size() != dto.atletasIds.size()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON("Um ou mais atletas não foram encontrados"))
                        .build();
            }
            competicao.atletas = atletasEncontrados;
        } else {
            competicao.atletas = new ArrayList<>();
        }

        competicao.persist();

        return Response.ok(new CompeticaoDTO(competicao)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Excluir competição",
            description = "Exclui a competição pelo ID. Retorna 404 se não encontrada."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Competição excluída com sucesso"),
            @APIResponse(responseCode = "404", description = "Competição não encontrada")
    })
    @Transactional
    public Response deletarCompeticao(
            @Parameter(description = "ID da competição", required = true) @PathParam("id") Long id) {

        Competicao competicao = Competicao.findById(id);
        if (competicao == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(MSG_NAO_ENCONTRADO_ID))
                    .build();
        }

        competicao.delete();
        return Response.ok(mensagemToJSON(MSG_DELETADO)).build();
    }

    @Override
    public String mensagemToJSON(String msg) {
        return "{\"message\": \"" + msg + "\"}";
    }
}