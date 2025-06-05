-- Inserção de Competições
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (1, 'Campeonato Nacional', 'São Paulo', 2023);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (2, 'Torneio Internacional', 'Rio de Janeiro', 2023);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (3, 'Copa do Mundo', 'Buenos Aires', 2024);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (4, 'Jogos Regionais', 'Belo Horizonte', 2023);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (5, 'Desafio das Estrelas', 'Porto Alegre', 2024);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (6, 'Super Liga', 'Curitiba', 2023);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (7, 'Circuito Profissional', 'Recife', 2024);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (8, 'Torneio Primavera', 'Salvador', 2023);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (9, 'Festival de Verão', 'Fortaleza', 2024);
INSERT INTO competicao (id_competicao, nome, local, ano) VALUES (10, 'Campeonato Aberto', 'Brasília', 2023);

-- Inserção de Atletas
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (1, 'Pedro Alves', 22, 'Top 10', 1);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (2, 'Mariana Santos', 19, 'Top 5', 2);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (3, 'Rafael Pereira', 25, 'Top 20', 3);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (4, 'Camila Oliveira', 21, 'Top 15', 4);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (5, 'Diego Costa', 23, 'Top 30', 5);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (6, 'Beatriz Nunes', 20, 'Top 8', 6);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (7, 'Gustavo Henrique', 24, 'Top 25', 7);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (8, 'Larissa Machado', 18, 'Top 40', 8);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (9, 'Thiago Silva', 26, 'Top 3', 9);
INSERT INTO atleta (id_atleta, nome, idade, ranking_nacional, id_tecnico) VALUES (10, 'Vanessa Almeida', 22, 'Top 12', 10);

-- Enum Nivel: INICIANTE, INTERMEDIARIO, AVANCADO
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (1, 'Carlos Alberto', 'AVANCADO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (2, 'Maria Silva', 'INTERMEDIARIO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (3, 'João Pedro', 'INICIANTE');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (4, 'Ana Beatriz', 'AVANCADO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (5, 'Roberto Carlos', 'INTERMEDIARIO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (6, 'Fernanda Lima', 'AVANCADO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (7, 'Ricardo Oliveira', 'INICIANTE');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (8, 'Patricia Souza', 'INTERMEDIARIO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (9, 'Lucas Mendes', 'AVANCADO');
INSERT INTO tecnico (id_tecnico, nome, nivel) VALUES (10, 'Juliana Costa', 'INTERMEDIARIO');

-- Inserção de relações Atleta-Competição (muitos para muitos)
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (1, 1);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (1, 2);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (2, 1);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (2, 3);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (3, 4);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (3, 5);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (4, 6);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (4, 7);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (5, 8);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (5, 9);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (6, 10);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (6, 1);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (7, 2);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (7, 3);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (8, 4);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (8, 5);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (9, 6);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (9, 7);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (10, 8);
INSERT INTO atleta_competicao (id_atleta, id_competicao) VALUES (10, 9);
