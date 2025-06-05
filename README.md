# 🏓 API de Tênis de Mesa

Bem-vindo à API de Tênis de Mesa desenvolvida com Quarkus!  
Aqui você encontra todos os recursos para gerenciar Atletas, Competições e Técnicos de forma simples e eficiente.

## 📌 Sobre

- **Versão atual**: 2.0.0
- **Desenvolvido por**: Giovanna Piccinato
- **Data**: V1: 07/04/2025 — V2: 05/06/2025
- **Documentação Swagger**: [Swagger UI](http://localhost:8080/swagger-ui)
- **Console do Banco**: [H2 Console](http://localhost:8080/h2-console)
- **Licença**: Apache 2.0

---

## 🚀 Tecnologias

- Java 11+
- Quarkus
- Hibernate ORM
- H2 Database
- Swagger (OpenAPI)
- Thunder Client / Postman para testes

---

## 🛡️ Autenticação

> 🔐 **Obrigatória na V2**

- **Header**: `X-API-Key`
- **Valor**: `df776693-555b-4b3b-bc42-76809ba320b2`

### Como usar:

✅ **No Swagger UI**:
1. Clique em “Authorize”.
2. Informe `df776693-555b-4b3b-bc42-76809ba320b2` no campo `X-API-Key`.
3. Clique em “Authorize” e “Close”.

✅ **No Thunder Client ou Postman**:
1. Vá na aba **Headers**
2. Adicione:
    - **Key**: `X-API-Key`
    - **Value**: `df776693-555b-4b3b-bc42-76809ba320b2`

---

## 🔑 Novidades da V2

✅ Autenticação via API Key  
✅ Controle de Idempotência (`Idempotency-Key`)  
✅ Rate Limiting: **60 requisições por minuto**  
✅ Novos endpoints:

- Criar múltiplos atletas: `POST /v2/atletas/batch`
- Atualizar técnico de atleta: `PUT /v2/atletas/{id}/tecnico`
- Associar atletas a competição: `POST /v2/competicoes/{id}/atletas`
- Atualizar nível do técnico: `PATCH /v2/tecnicos/{id}/nivel`

---

## 🗂️ Estrutura da API

### ✅ V1 — Endpoints Básicos

### 🎯 Atletas
- `GET /v1/atletas` — Listar atletas
- `POST /v1/atletas` — Cadastrar atleta
- `GET /v1/atletas/{id}` — Buscar por ID
- `PUT /v1/atletas/{id}` — Atualizar
- `DELETE /v1/atletas/{id}` — Deletar

### 🎯 Competições
- `GET /v1/competicoes` — Listar competições
- `POST /v1/competicoes` — Cadastrar competição
- `GET /v1/competicoes/{id}` — Buscar por ID
- `PUT /v1/competicoes/{id}` — Atualizar
- `DELETE /v1/competicoes/{id}` — Deletar

### 🎯 Técnicos
- `GET /v1/tecnicos` — Listar técnicos
- `POST /v1/tecnicos` — Cadastrar técnico
- `GET /v1/tecnicos/{id}` — Buscar por ID
- `PUT /v1/tecnicos/{id}` — Atualizar
- `DELETE /v1/tecnicos/{id}` — Deletar

---

### ✅ V2 — Novos Recursos e Segurança 🔐

### 🎯 Atletas
- `GET /v2/atletas` — Listar atletas
- `POST /v2/atletas` — Cadastrar atleta
- `POST /v2/atletas/batch` — Criar vários atletas de uma vez
- `GET /v2/atletas/{id}` — Buscar por ID
- `GET /v2/atletas/{id}/competicoes` — Listar competições de um atleta
- `PUT /v2/atletas/{id}/tecnico` — Atualizar técnico

### 🎯 Competições
- `GET /v2/competicoes` — Listar competições
- `POST /v2/competicoes` — Cadastrar competição
- `GET /v2/competicoes/{id}` — Buscar por ID
- `PUT /v2/competicoes/{id}` — Atualizar
- `DELETE /v2/competicoes/{id}` — Deletar
- `POST /v2/competicoes/{id}/atletas` — Adicionar atletas à competição

### 🎯 Técnicos
- `GET /v2/tecnicos` — Listar técnicos
- `POST /v2/tecnicos` — Cadastrar técnico
- `GET /v2/tecnicos/{id}` — Buscar por ID
- `PUT /v2/tecnicos/{id}` — Atualizar
- `DELETE /v2/tecnicos/{id}` — Deletar
- `PATCH /v2/tecnicos/{id}/nivel` — Atualizar nível do técnico

---

## 📦 Modelos de Requisição — Exemplos de JSON

### 🎯 Atleta — Cadastro

{
"nome": "Hugo Calderano2",
"idade": 35,
"rankingNacional": "2º lugar",
"tecnicoId": 1,
"competicaoIds": [1]
}

### 🎯 Cadastro de Atleta em Lote: Batch de Atletas
Requisição POST:http://localhost:8080/v2/atletas/batch

[ { "nome": "João Silva", "idade": 23, "rankingNacional": 150, "tecnicoId": 1 }, { "nome": "Maria Souza", "idade": 21, "rankingNacional": 200, "tecnicoId": 2 } ]

### 🎯Listar Competição de um Atleta
Requisição GET: http://localhost:8080/v2/atletas/1/competicoes

### 🎯Atualizar Técnico de um atleta
Requisição PUT: http://localhost:8080/v2/atletas/1/tecnico

{ "tecnicoId": 1 }



### 🎯 Competição — Cadastro
{
"nome": "Campeonato Estadual Atualizado",
"local": "Rio de Janeiro",
"ano": 2026,
"atletasIds": [1]
}

### 🎯 Associar Atletas a uma Competição
{
"atletaIds": [4, 5]
}


### 🎯 Técnico — Cadastro
Requisição POST:http://localhost:8080/v2/competicoes/1/atletas

{
"nome": "Thiago Monteiro2",
"nivel": "AVANCADO" // enum de INICIANTE, INTERMEDIARIO e AVANCADO 
}

### ⚙️ Como rodar o projeto localmente
#### Clone o repositório
git clone https://github.com/seu-usuario/api-tenis-mesa.git

#### Acesse a pasta
cd api-tenis-mesa

#### Execute com Quarkus Dev Mode
./mvnw quarkus:dev


### 💡 Recursos adicionais
✅ Swagger UI — Documentação interativa
✅ H2 Console — Console do banco de dados

### 📞 Suporte
📧 Email: giovanna@vermais.com
🌐 LinkedIn: [Link para o LinkedIn]

### ⚖️ Licença
- Esta API está licenciada sob a Apache 2.0.
- 
### ✅ Boas práticas
- Use o header X-API-Key em todas as requisições da V2.
- Utilize o Idempotency-Key para evitar duplicações.
- Respeite o rate limit: 60 requisições por minuto.