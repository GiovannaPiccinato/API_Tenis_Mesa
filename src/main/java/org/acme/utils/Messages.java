package org.acme.utils;

public interface Messages {
    public String MSG_VAZIO = "Campo vazio!";
    public String MSG_ATUALIZADO = "Atualizado com sucesso!";
    public String MSG_ATUALIZADO_ERRO = "Erro ao atualizar!";
    public String MSG_NAO_ENCONTRADO_ID = "Id não encontrado!";
    public String MSG_DELETADO = "Deletado com sucesso!";
    public String COMPETICAO_NAO_ENCONTRADA = "Competição não encontrada";
    public String TECNICO_NAO_ENCONTRADO = "Técnico não encontrado";
    public String ATLETA_EXISTE = "Já existe um atleta com este nome";
    public String ERRO_CADASTRO = "Erro ao Cadastrar";
    String COMPETICAO_EXISTE = "Já existe uma Competição com este nome";
    String ATLETA_NAO_ENCONTRADO = "Atleta(s) não encontrado(s)";
    String ATLETAS_NAO_ENCONTRADOS = "Um ou mais atletas não foram encontrados";


    public String mensagemToJSON(String msg);
}
