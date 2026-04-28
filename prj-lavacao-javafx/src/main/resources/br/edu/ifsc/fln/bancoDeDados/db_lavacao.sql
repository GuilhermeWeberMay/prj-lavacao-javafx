#DROP DATABASE IF EXISTS db_lavacao;

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
    id        INT                                                   NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100)                                          NOT NULL,
    valor     DECIMAL(10, 2)                                        NOT NULL,
    categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL,
    CONSTRAINT pk_servico PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS configuracoes
(
    id             INT AUTO_INCREMENT,
    pontos_servico INT NOT NULL,
    CONSTRAINT pk_configuracoes PRIMARY KEY (id)
);

INSERT INTO configuracoes (pontos_servico)
VALUES (10);

INSERT INTO servico (descricao, valor, categoria)
VALUES ('lavagem simples', 50.00, 'GRANDE'),
       ('lavagem com cera', 70.00, 'MEDIO');

CREATE TABLE if not exists modelo
(
    id        INT                                                   NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100)                                          NOT NULL,
    categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL,
    id_marca  INT                                                   NOT NULL,
    CONSTRAINT pk_modelo PRIMARY KEY (id),
    CONSTRAINT fk_modelo_marca
        FOREIGN KEY (id_marca)
            REFERENCES marca (id)
) ENGINE = InnoDB;

INSERT INTO modelo (descricao, categoria, id_marca)
VALUES ('pulse', 'PADRAO', 3),
       ('versa', 'MEDIO', 2);

CREATE TABLE IF NOT EXISTS motor
(
    id_modelo        INT                                                           NOT NULL AUTO_INCREMENT,
    potencia         VARCHAR(100)                                                  NOT NULL,
    tipo_combustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL,
    CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo foreign key (id_modelo)
        references modelo (id) on delete cascade on update cascade
) ENGINE = InnoDB;

INSERT INTO motor (potencia, tipo_combustivel)
VALUES ('1.0', 'FLEX'),
       ('1.6', 'DIESEL');

/* UPDATE configuracoes set pontos_servico = 20 where id = 1; */

-- Ver qual motor está sendo referenciado
##
-- Veiculo vai ter chave extrangeira de cliente
##
-- Motor vai ter id_modelo
-- Id de motor é o mesmo de modelo
##