package org.acme.resources.V1;

import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.TecnicoDTO;
import org.acme.DTO.TecnicoEntradaDTO;
import org.acme.entitys.Competicao;
import org.acme.entitys.Tecnico;
import org.acme.enums.Nivel;
import org.acme.utils.Messages;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TecnicoResource implements Messages {

    @GET
    @Operation(summary = "Listar todos os técnicos")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Técnicos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TecnicoDTO.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "204", description = "Nenhum técnico encontrado")
    })
    public Response listarTodos() {
        List<Tecnico> tecnicos = Tecnico.listAll();
        if (tecnicos.isEmpty()) {
            return Response.noContent().build();
        }
        List<TecnicoDTO> dtos = tecnicos.stream().map(TecnicoDTO::new).toList();
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar técnico por ID",
            description = "Busca um técnico pelo ID. Retorna 404 se não encontrado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Técnico encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TecnicoDTO.class))),
            @APIResponse(responseCode = "404", description = "Técnico não encontrado")
    })
    public Response buscarPorId(@PathParam("id") Long id) {
        Tecnico tecnico = Tecnico.findById(id);
        if (tecnico == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_NAO_ENCONTRADO_ID))
                    .build();
        }
        return Response.ok(new TecnicoDTO(tecnico)).build();
    }

    @POST
    @Operation(summary = "Cadastrar novo técnico",
            description = "Cadastra um novo técnico no sistema. Retorna 201 se sucesso, 400 se erro.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Técnico cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TecnicoDTO.class))),
            @APIResponse(responseCode = "400", description = "Erro ao cadastrar técnico"),
            @APIResponse(responseCode = "500", description = "Erro interno (Níveis: INICIANTE, INTERMEDIARIO, AVANCADO)")
    })
    @Transactional
    public Response cadastrarTecnico(@NotNull @Valid TecnicoEntradaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON(Messages.MSG_VAZIO))
                    .build();
        }

        Tecnico existente = Tecnico.find("nome", dto.nome).firstResult();
        if (existente != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON("Já existe um Tecnico com este nome"))
                    .build();
        }
        try {
            Tecnico tecnico = new Tecnico();
            tecnico.nome = dto.nome;
            tecnico.nivel = Nivel.valueOf(dto.nivel.toUpperCase());

            tecnico.persist();

            return Response.status(Response.Status.CREATED)
                    .entity(new TecnicoDTO(tecnico))
                    .build();

        } catch (IllegalArgumentException e) {
            String msg = "Nível inválido. Valores permitidos: " + Arrays.toString(Nivel.values());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON(msg))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar técnico por ID",
            description = "Atualiza os dados do técnico pelo ID. Retorna 404 se não encontrado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Técnico atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TecnicoDTO.class))),
            @APIResponse(responseCode = "404", description = "Técnico não encontrado"),
            @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    @Transactional
    public Response atualizarTecnico(@PathParam("id") Long id, @NotNull @Valid TecnicoEntradaDTO dto) {
        Tecnico tecnicoToUpdate = Tecnico.findById(id);

        if (tecnicoToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_ATUALIZADO_ERRO))
                    .build();
        }

        tecnicoToUpdate.nome = dto.nome;
        tecnicoToUpdate.nivel = Nivel.valueOf(dto.nivel);

        tecnicoToUpdate.persist();

        return Response.ok(new TecnicoDTO(tecnicoToUpdate)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar técnico por ID",
            description = "Exclui o técnico pelo ID. Retorna 404 se não encontrado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Técnico excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Técnico não encontrado")
    })
    @Transactional
    public Response deletarTecnico(@PathParam("id") Long id) {
        Tecnico tecnicoToDelete = Tecnico.findById(id);
        if (tecnicoToDelete == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_NAO_ENCONTRADO_ID))
                    .build();
        }
        tecnicoToDelete.delete();
        return Response.ok(mensagemToJSON(Messages.MSG_DELETADO)).build();
    }

    @Override
    public String mensagemToJSON(String msg) {
        return "{" +
                "\"message\": \"" + msg + "\"" +
                "}";
    }
}