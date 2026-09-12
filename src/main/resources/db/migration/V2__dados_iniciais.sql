-- =====================================================================
-- V2 - Dados iniciais para estudo
--
-- O H2 e em memoria: tudo desaparece quando a aplicacao para. Esta
-- migration roda de novo a cada start, garantindo um usuario para logar
-- e alguns registros nas telas.
--
-- A senha e gravada com hash BCrypt (o mesmo formato que o
-- PasswordEncoder gera). O hash abaixo corresponde a senha "123456".
-- =====================================================================

INSERT INTO tb_usuario (nome, cpf, email, senha, data_nascimento, status) VALUES
    ('Aluno Treina Recife', '111.111.111-11', 'aluno@treina.com',
     '$2a$10$TlRfk4Cy3XdeuRTMFORtjOxia10XuAZwCkdCamDHbNxPgviAgkeZ.',
     '2000-01-15', 'ATIVO');

-- Os ids sao gerados pelo banco, por isso buscamos o usuario pelo e-mail
-- em vez de chumbar "1" nas chaves estrangeiras.
INSERT INTO tb_projeto (nome, descricao, data_inicio, data_conclusao, status, id_responsavel) VALUES
    ('Site do Treina Recife', 'Projeto de exemplo criado pela migration V2',
     '2026-01-10', NULL, 'ATIVO',
     (SELECT id FROM tb_usuario WHERE email = 'aluno@treina.com'));

INSERT INTO tb_tarefa (titulo, descricao, data_criacao, data_conclusao, prioridade, status, id_projeto, id_usuario) VALUES
    ('Criar a tela de login', 'HTML + CSS + fetch no /auth/login',
     '2026-01-10', '2026-01-12', 'ALTA', 'CONCLUIDA',
     (SELECT id FROM tb_projeto WHERE nome = 'Site do Treina Recife'),
     (SELECT id FROM tb_usuario WHERE email = 'aluno@treina.com')),
    ('Listar projetos no painel', 'Consumir GET /projetos enviando o token',
     '2026-01-11', NULL, 'MEDIA', 'FAZENDO',
     (SELECT id FROM tb_projeto WHERE nome = 'Site do Treina Recife'),
     (SELECT id FROM tb_usuario WHERE email = 'aluno@treina.com'));
