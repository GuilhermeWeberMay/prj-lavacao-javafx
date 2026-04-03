CREATE DATABASE IF NOT EXISTS db_lavacao_teste;
USE db_lavacao_teste;

CREATE TABLE IF NOT EXISTS cor
(
    id   int         not null unique auto_increment primary key,
    nome varchar(50) not null
) engine InnoDB;

INSERT INTO cor (nome)
VALUES ('Azul'),
       ('Preto'),
       ('Branco'),
       ('Vermelho');

CREATE TABLE IF NOT EXISTS marca
(
    id   int         not null unique auto_increment primary key,
    nome varchar(50) not null
) engine InnoDB;

INSERT INTO marca (nome)
VALUES ('Volksvagen'),
       ('Nissan'),
       ('Fiat');

CREATE TABLE IF NOT EXISTS servico
(
    id   int         not null unique auto_increment primary key,
    descricao varchar(100) not null,
    valor DECIMAL(3,1) not null,
    pontos int not null
)engine InnoDB;

INSERT INTO servico (descricao, valor, pontos)
VALUES ('lavagem simples', 50, 10),
       ('lavagem com cera', 70, 10)
