package org.acme.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TecnicoEntradaDTO {
    @NotBlank(message = "Nome não pode estar vazio")
    public String nome;

    @NotNull(message = "Níveis: INICIANTE, INTERMEDIARIO, AVANCADO")
    public String nivel; // Recebe string e converte para enum
}
