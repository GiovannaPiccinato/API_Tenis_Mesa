package org.acme.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "atleta")
public class Atleta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long atleta_id;

    @NotBlank(message = "O nome é obrigatório")
    public String nome;

    @Min(10)
    @Max(50)
    public int idade;

    @NotBlank
    public String rankingNacional;

    @ManyToOne // cada atleta tem um técnico
    @JsonManagedReference
    public Tecnico tecnico;

    @ManyToMany// Vários atletas participam de várias competições.
    @JoinTable(
            name = "Atleta_Competicao",
            joinColumns = @JoinColumn(name = "atleta_id"),
            inverseJoinColumns = @JoinColumn(name = "competicao_id")
    )
    @JsonManagedReference
    public List<Competicao> competicoes;


    public Atleta() {
    }

    public Atleta(String nome, int idade, String rankingNacional, Tecnico tecnico, List<Competicao> competicoes) {
        this.nome = nome;
        this.idade = idade;
        this.rankingNacional = rankingNacional;
        this.tecnico = tecnico;
        this.competicoes = competicoes;
    }

    public Long getAtleta_id() {return atleta_id;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public int getIdade() { return idade;}
    public void setIdade( int idade) {this.idade = idade;}
    public @NotBlank String getRankingNacional() {return rankingNacional;}
    public void setRankingNacional(String rankingNacional) {this.rankingNacional = rankingNacional;}
    public Tecnico getTecnico() {return tecnico;}
    public void setTecnico(Tecnico tecnico) {this.tecnico = tecnico;}
    public List<Competicao> getCompeticoes() {return competicoes;}
    public void setCompeticoes(List<Competicao> competicoes) {this.competicoes = competicoes;}

}