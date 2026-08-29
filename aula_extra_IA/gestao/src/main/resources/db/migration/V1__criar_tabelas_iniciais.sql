-- =====================================================================
-- V1 - Criacao das tabelas iniciais do Sistema de Gestao de Projetos
-- Banco: MySQL 8+
-- =====================================================================

CREATE TABLE tb_usuario (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nome             VARCHAR(255) NOT NULL,
    cpf              VARCHAR(14)  NOT NULL,
    email            VARCHAR(255) NOT NULL,
    senha            VARCHAR(255) NOT NULL,
    data_nascimento  DATE         NULL,
    status           VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT uk_usuario_cpf UNIQUE (cpf)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE tb_projeto (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nome             VARCHAR(255) NOT NULL,
    descricao        TEXT         NULL,
    data_inicio      DATE         NULL,
    data_conclusao   DATE         NULL,
    status           VARCHAR(20)  NOT NULL,
    id_responsavel   BIGINT       NOT NULL,
    CONSTRAINT pk_projeto PRIMARY KEY (id),
    CONSTRAINT fk_projeto_responsavel FOREIGN KEY (id_responsavel) REFERENCES tb_usuario (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE tb_tarefa (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    titulo           VARCHAR(255) NOT NULL,
    descricao        TEXT         NULL,
    data_criacao     DATE         NULL,
    data_conclusao   DATE         NULL,
    prioridade       VARCHAR(20)  NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    id_projeto       BIGINT       NULL,
    id_usuario       BIGINT       NULL,
    CONSTRAINT pk_tarefa PRIMARY KEY (id),
    CONSTRAINT fk_tarefa_projeto FOREIGN KEY (id_projeto) REFERENCES tb_projeto (id),
    CONSTRAINT fk_tarefa_usuario FOREIGN KEY (id_usuario) REFERENCES tb_usuario (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
