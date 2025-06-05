package org.acme.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class AtletaEntradaDTO {
    @NotBlank(message = "Nome não pode estar vazio")
    public String nome;

    @Positive(message = "Idade precisa ser positiva")
    @Min(18)
    public int idade;

    public String rankingNacional;
    public Long tecnicoId;
    public List<Long> competicaoIds;
}
