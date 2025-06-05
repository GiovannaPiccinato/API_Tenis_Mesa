package org.acme.DTO;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class CompeticaoEntradaDTO {
    @NotBlank(message = "Nome não pode estar vazio")
    public String nome;

    @NotBlank(message = "Local não pode estar vazio")
    public String local;

    @Positive(message = "Ano deve ser positivo")
    public int ano;

    @NotNull
    public List<Long> atletasIds; // aceita varios atletas para uma mesma competição
}