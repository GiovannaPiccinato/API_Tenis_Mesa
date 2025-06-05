package org.acme.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.acme.enums.Nivel;

import java.util.List;

@Entity
@Table(name = "tecnico")
public class Tecnico extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long tecnico_id;

    @NotBlank (message = "Nome não pode estar vazio")
    public String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Nivel nivel;

    @OneToMany(mappedBy = "tecnico")
    @JsonBackReference
    public List<Atleta> atletas;

    public Tecnico(){
    }

    public Tecnico(String nome, Nivel nivel) {
        this.nome = nome;
        this.nivel = nivel;
    }

    public Long getTecnico_id() { return tecnico_id;}
    public @NotBlank String getNome() {return nome;}
    public void setNome(@NotBlank String nome) {this.nome = nome;}
    public Nivel getNivel() {return nivel;}
    public void setNivel(Nivel nivel) {this.nivel = nivel;}
    public List<Atleta> getAtletas() {return atletas;}
    public void setAtletas(List<Atleta> atletas) {this.atletas = atletas;}
}
