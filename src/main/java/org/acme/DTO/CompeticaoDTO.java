package org.acme.DTO;

import org.acme.entitys.Atleta;
import org.acme.entitys.Competicao;

import java.util.Collections;
import java.util.List;

public class CompeticaoDTO {
    public Long id;
    public String nome;
    public String local;
    public int ano;
    public List<?> atletas;

    public CompeticaoDTO(Competicao competicao) {
        this.id = competicao.competicao_id;
        this.nome = competicao.nome;
        this.local = competicao.local;
        this.ano = competicao.ano;

        this.atletas = competicao.atletas != null ?
                competicao.atletas.stream()
                        .map(AtletaResumoDTO::new)
                        .toList() :
                Collections.emptyList();
    }

    class AtletaResumoDTO {
        public Long atleta_id;
        public String nome;

        public AtletaResumoDTO(Atleta atleta) {
            this.atleta_id = atleta.atleta_id;
            this.nome = atleta.nome;
        }
    }
}