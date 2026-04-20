DROP DATABASE IF EXISTS db_lavacao;

CREATE DATABASE db_lavacao;

USE db_lavacao;

CREATE TABLE IF NOT EXISTS cor
(
    id   INT         NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT pk_cor PRIMARY KEY (id)
) ENGINE = InnoDB;

INSERT INTO cor (nome)
VALUES ('Azul'),
       ('Preto'),
       ('Branco'),
       ('Vermelho');

CREATE TABLE IF NOT EXISTS marca
(
    id   INT         NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT pk_marca PRIMARY KEY (id)
) ENGINE = InnoDB;

INSERT INTO marca (nome)
VALUES ('Volkswagen'),
       ('Nissan'),
       ('Fiat');

CREATE TABLE IF NOT EXISTS servico
(
    id        INT           NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100)  NOT NULL,
    valor     DECIMAL(5, 2) NOT NULL,
    CONSTRAINT pk_servico PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE configuracoes
(
    id             INT AUTO_INCREMENT,
    pontos_servico INT NOT NULL,
    CONSTRAINT pk_configuracoes PRIMARY KEY (id)
);

INSERT INTO configuracoes (pontos_servico)
VALUES (10);

INSERT INTO servico (descricao, valor)
VALUES ('lavagem simples', 50.00),
       ('lavagem com cera', 70.00);

CREATE TABLE IF NOT EXISTS motor
(
    id               INT                                                           NOT NULL AUTO_INCREMENT,
    potencia         VARCHAR(100)                                                  NOT NULL,
    tipo_combustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL,
    CONSTRAINT pk_motor PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS modelo
(
    id        INT                                                   NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100)                                          NOT NULL,
    categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL,
    id_marca  INT                                                   NOT NULL,
    id_motor  INT                                                   NOT NULL,
    CONSTRAINT pk_modelo PRIMARY KEY (id),
    CONSTRAINT fk_modelo_marca
        FOREIGN KEY (id_marca)
            REFERENCES marca (id),
    CONSTRAINT fk_modelo_motor
        FOREIGN KEY (id_motor)
            REFERENCES motor (id) ON DELETE CASCADE
) ENGINE = InnoDB;

INSERT INTO motor (potencia, tipo_combustivel)
VALUES ('1.0', 'FLEX'),
       ('1.6', 'DIESEL');

INSERT INTO modelo (descricao, categoria, id_marca, id_motor)
VALUES ('pulse', 'PADRAO', 3, 1),
       ('versa', 'MEDIO', 2, 2);

CREATE TABLE IF NOT EXISTS veiculo
(
    id         INT          NOT NULL AUTO_INCREMENT,
    placa      CHAR(7)      NOT NULL UNIQUE,
    observacao VARCHAR(500) NOT NULL,
    id_cor     INT          NOT NULL,
    id_modelo  INT          NOT NULL,
    CONSTRAINT pk_veiculo PRIMARY KEY (id),
    CONSTRAINT fk_veiculo_cor
        FOREIGN KEY (id_cor)
            REFERENCES cor (id),
    CONSTRAINT fk_veiculo_modelo
        FOREIGN KEY (id_modelo)
            REFERENCES modelo (id)
) ENGINE = InnoDB;

INSERT INTO veiculo(placa, observacao, id_cor, id_modelo)
VALUES ('SSF4B28', 'MEU CARRO', 1, 2);

/* UPDATE configuracoes set pontos_servico = 20 where id = 1; */

SELECT m.descricao, m.categoria, ma.nome, mo.potencia, mo.tipo_combustivel
FROM modelo m
         INNER JOIN marca ma ON m.id_marca = ma.id
         INNER JOIN motor mo ON m.id_motor = mo.id;

SELECT v.placa,
       v.observacao,
       c.nome  AS cor,
       m.descricao,
       m.categoria,
       ma.nome AS marca,
       mo.potencia,
       mo.tipo_combustivel
FROM veiculo v
         INNER JOIN cor c ON v.id_cor = c.id
         INNER JOIN modelo m ON v.id_modelo = m.id
         INNER JOIN marca ma ON m.id_marca = ma.id
         INNER JOIN motor mo ON m.id_motor = mo.id;