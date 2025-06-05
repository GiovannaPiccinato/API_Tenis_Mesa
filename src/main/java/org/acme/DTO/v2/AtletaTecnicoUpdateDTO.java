package org.acme.DTO.v2;

import jakarta.validation.constraints.NotNull;

public class AtletaTecnicoUpdateDTO {
    @NotNull(message = "ID do técnico é obrigatório")
    private Long tecnicoId;

    public Long getTecnicoId() {
        return tecnicoId;
    }
    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }
}