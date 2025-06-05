package org.acme.resources.V1;
import io.smallrye.common.constraint.NotNull;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.AtletaDTO;
import org.acme.DTO.AtletaEntradaDTO;
import org.acme.entitys.Atleta;
import org.acme.entitys.Competicao;
import org.acme.entitys.Tecnico;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.acme.utils.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Path("v1/atletas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtletaResource implements Messages {
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
            // verifica se é nulo
            if (dto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON(Messages.MSG_VAZIO))
                        .build();
            }
            // verifica se ja tem atleta com o nome
            Atleta existente = Atleta.find("nome", dto.nome).firstResult();
            if (existente != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON("Já existe um atleta com este nome"))
                        .build();
            }

            // Busca técnico pelo ID
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

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Atualizar atleta",
            description = "Atualiza os dados do atleta pelo ID. Retorna 404 se não encontrado."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Atleta atualizado com sucesso"),
            @APIResponse(responseCode = "404", description = "Atleta não encontrado"),
            @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    @Transactional
    public Response atualizarAtleta(@PathParam("id") Long id, @NotNull @Valid AtletaEntradaDTO atletaInputDTO) {
        Atleta atletaToUpdate = Atleta.findById(id);
        if (atletaToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_ATUALIZADO_ERRO))
                    .build();
        }

        atletaToUpdate.setNome(atletaInputDTO.nome);
        atletaToUpdate.setIdade(atletaInputDTO.idade);
        atletaToUpdate.setRankingNacional(atletaInputDTO.rankingNacional);

        // buscar técnico pela ID
        if (atletaInputDTO.tecnicoId != null) {
            Tecnico tecnico = Tecnico.findById(atletaInputDTO.tecnicoId);
            if (tecnico == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensagemToJSON("Técnico com ID informado não encontrado"))
                        .build();
            }
            atletaToUpdate.setTecnico(tecnico);
        } else {
            atletaToUpdate.setTecnico(null);
        }

        // buscar lista de competições pelo ID
        if (atletaInputDTO.competicaoIds != null && !atletaInputDTO.competicaoIds.isEmpty()) {
            List<Competicao> competicoes = Competicao.listByIds(atletaInputDTO.competicaoIds);
            atletaToUpdate.setCompeticoes(competicoes);
        } else {
            atletaToUpdate.setCompeticoes(Collections.emptyList());
        }

        atletaToUpdate.persist();

        return Response.ok(mensagemToJSON(Messages.MSG_ATUALIZADO)).build();
    }


    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Deletar atleta",
            description = "Deleta o atleta pelo ID. Retorna 404 se não encontrado."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Atleta deletado com sucesso"),
            @APIResponse(responseCode = "404", description = "Atleta não encontrado")
    })
    @Transactional
    public Response deletarAtleta(@PathParam("id") Long id) {
        Atleta atletaToDelete = Atleta.findById(id);
        if (atletaToDelete == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_NAO_ENCONTRADO_ID))
                    .build();
        }
        atletaToDelete.delete();
        return Response.ok(mensagemToJSON(Messages.MSG_DELETADO)).build();
    }

    @Override
    public String mensagemToJSON(String msg) {
        return "{" +
                "\"message\": \"" + msg + "\"" +
                "}";
    }
}
