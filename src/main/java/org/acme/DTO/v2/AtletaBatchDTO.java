package org.acme.DTO.v2;

public class AtletaBatchDTO {
    private String nome;
    private int idade;
    private String rankingNacional;

    // getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getRankingNacional() {
        return rankingNacional;
    }

    public void setRankingNacional(String rankingNacional) {
        this.rankingNacional = rankingNacional;
    }
}