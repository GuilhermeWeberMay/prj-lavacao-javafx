CREATE DATABASE db_lavacao;

USE db_lavacao;

CREATE TABLE IF NOT EXISTS cliente
(
    id            int          not null auto_increment,
    nome          varchar(100) not null,
    celular       varchar(11)  not null,
    email         varchar(100) not null,
    data_cadastro date         not null,
    CONSTRAINT pk_cliente primary key (id)
) Engine InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_fisica
(
    id_cliente      int         not null,
    cpf             varchar(11) not null,
    data_nascimento date        not null,
    CONSTRAINT pk_pessoa_fisica primary key (id_cliente),
    CONSTRAINT pk_pessoa_fisica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) Engine InnoDB;

INSERT INTO cliente (nome, celular, email, data_cadastro)
VALUES ('Guilherme', '99999999999', 'guilherme.wm@aluno.ifsc.edu.br', '2026-05-04');
INSERT INTO pessoa_fisica (id_cliente, cpf, data_nascimento)
VALUES (1, '00000000000', '2006-10-24');

CREATE TABLE IF NOT EXISTS pessoa_juridica
(
    id_cliente        int         not null,
    cnpj              varchar(14) not null,
    inscricao_estadual varchar(15) not null,
    CONSTRAINT pk_pessoa_juridica primary key (id_cliente),
    CONSTRAINT pk_pessoa_juridica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) Engine InnoDB;

INSERT INTO cliente (nome, celular, email, data_cadastro)
VALUES ('Rockembach Construções LTDA', '00000000000', 'rockcontrucoes@contato.com.br', '2026-05-04');
INSERT INTO pessoa_juridica (id_cliente, cnpj, inscricao_estadual)
VALUES (2, '99999999999999', '123456');

CREATE TABLE IF NOT EXISTS cor
(
    id   INT         NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT pk_cor PRIMARY KEY (id)
) ENGINE = INNODB;

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
) ENGINE = INNODB;

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
) ENGINE = INNODB;

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

CREATE TABLE modelo
(
    id        INT                                                   NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100)                                          NOT NULL,
    categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL,
    id_marca  INT                                                   NOT NULL,
    CONSTRAINT pk_modelo PRIMARY KEY (id),
    CONSTRAINT fk_modelo_marca FOREIGN KEY (id_marca)
        REFERENCES marca (id)
) ENGINE = INNODB;

INSERT INTO modelo (descricao, categoria, id_marca)
VALUES ('pulse', 'PADRAO', 3),
       ('versa', 'MEDIO', 2);

CREATE TABLE IF NOT EXISTS motor
(
    id_modelo        INT                                                           NOT NULL AUTO_INCREMENT,
    potencia         VARCHAR(100)                                                  NOT NULL,
    tipo_combustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL,
    CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo)
        REFERENCES modelo (id)
        ON DELETE CASCADE
) ENGINE = INNODB;

INSERT INTO motor (potencia, tipo_combustivel)
VALUES ('1.0', 'FLEX'),
       ('1.6', 'DIESEL');

CREATE TABLE IF NOT EXISTS veiculo
(
    id         INT          NOT NULL AUTO_INCREMENT,
    placa      CHAR(7)      NOT NULL UNIQUE,
    observacao VARCHAR(500) NOT NULL,
    id_cor     INT          NOT NULL,
    id_modelo  INT          NOT NULL,
    id_cliente INT          NOT NULL,
    CONSTRAINT pk_veiculo PRIMARY KEY (id),
    CONSTRAINT fk_veiculo_cor FOREIGN KEY (id_cor)
        REFERENCES cor (id),
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo)
        REFERENCES modelo (id),
    CONSTRAINT fk_veiculo_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id)
) ENGINE = INNODB;

INSERT INTO veiculo(placa, observacao, id_cor, id_modelo, id_cliente)
VALUES ('SSF4B28', 'MEU CARRO', 1, 2, 1);

INSERT INTO veiculo(placa, observacao, id_cor, id_modelo, id_cliente)
VALUES ('GOL1234', 'GOL DA ALEMANHA', 2, 1, 2);