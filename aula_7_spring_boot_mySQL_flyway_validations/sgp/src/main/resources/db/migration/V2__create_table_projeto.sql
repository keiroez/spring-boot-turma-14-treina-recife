CREATE TABLE projeto (
    id_projeto BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NULL,
    data_inicio DATE NOT NULL,
    data_conclusao DATE NULL,
    status VARCHAR(10) NOT NULL,
    id_usuario BIGINT NOT NULL,
    PRIMARY KEY(id_projeto),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);