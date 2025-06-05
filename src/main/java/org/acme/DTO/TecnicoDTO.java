package org.acme.DTO;

import org.acme.entitys.Tecnico;

public class TecnicoDTO {
    public Long id;
    public String nome;
    public String nivel;

    public TecnicoDTO(Tecnico tecnico) {
        this.id = tecnico.tecnico_id;
        this.nome = tecnico.nome;
        this.nivel = tecnico.nivel != null ? tecnico.nivel.name() : null;
    }
}
