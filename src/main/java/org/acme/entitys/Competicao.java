package org.acme.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "competicao")
public class Competicao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long competicao_id;

    @NotBlank
    public String nome;

    @NotBlank
    public String local;

    @Min(1900)
    @Max(2100)
    public int ano;

    @ManyToMany(mappedBy = "competicoes")
    @JsonBackReference
    public List<Atleta> atletas;

    public Competicao(){
    }

    public Competicao(Long competicao_id, String nome, String local, int ano, List<Atleta> atletas) {
        this.competicao_id = competicao_id;
        this.nome = nome;
        this.local = local;
        this.ano = ano;
        this.atletas = atletas;
    }

    //buscar várias competições por lista de IDs
    public static List<Competicao> listByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList(); // evita query com lista vazia.
        }
        return find("competicao_id in ?1", ids).list();
    }


    public Long getCompeticao_id() {return competicao_id;}
}
