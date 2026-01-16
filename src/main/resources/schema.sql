CREATE TABLE IF NOT EXISTS jogadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    telefone varchar(255),
    codinome varchar(255) NOT NULL,
    grupoCodinome varchar(255) NOT NULL,
    CONSTRAINT UC_CODINOME_GRUPO UNIQUE (codinome, grupoCodinome)
    );