package org.acme.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import org.acme.entitys.Atleta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AtletaDTO {
    public Long atleta_id;
    public String nome;
    public int idade;
    public String rankingNacional;
    public String nomeTecnico;

    @JsonIdentityReference(alwaysAsId = false)
    public List<CompeticaoDTO> competicoes;

    public AtletaDTO(Atleta atleta) {
        this.atleta_id = atleta.atleta_id;
        this.nome = atleta.nome;
        this.idade = atleta.idade;
        this.rankingNacional = atleta.rankingNacional;
        this.nomeTecnico = atleta.tecnico != null ? atleta.tecnico.nome :  "Sem técnico";

        if (atleta.competicoes != null) {
            this.competicoes = atleta.competicoes.stream()
                    .map(CompeticaoDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.competicoes = Collections.emptyList();
        }

    }
}
