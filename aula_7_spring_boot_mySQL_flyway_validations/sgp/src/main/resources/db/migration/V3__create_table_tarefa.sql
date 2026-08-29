CREATE TABLE tarefa (
    id_tarefa BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NULL,
    data_criacao DATE NOT NULL,
    data_conclusao DATE NULL,
    prioridade VARCHAR(10) NOT NULL,
    status VARCHAR(10) NOT NULL,
    id_projeto BIGINT NULL,
    id_usuario BIGINT NULL,
    PRIMARY KEY (id_tarefa),
    FOREIGN KEY (id_projeto) REFERENCES projeto(id_projeto),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);