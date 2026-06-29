DROP DATABASE IF EXISTS db_lavacao;

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

CREATE TABLE IF NOT EXISTS pontuacao
(
    id         int not null,
    quantidade int not null,
    CONSTRAINT pk_potuacao PRIMARY KEY (id),
    CONSTRAINT fk_pontuacao_cliente FOREIGN KEY (id) REFERENCES cliente (id)
        ON DELETE CASCADE
) ENGINE InnoDB;

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
VALUES ('Ana Silva', '98888888888', 'ana.silva@email.com', '2026-05-01'),
       ('Bruno Santos', '97777777777', 'bruno.santos@email.com', '2026-05-02'),
       ('Carla Oliveira', '96666666666', 'carla.oliveira@email.com', '2026-05-03'),
       ('Diego Costa', '95555555555', 'diego.costa@email.com', '2026-05-04'),
       ('Elaine Mendes', '94444444444', 'elaine.mendes@email.com', '2026-05-05');


INSERT INTO pessoa_fisica (id_cliente, cpf, data_nascimento)
VALUES (1, '12345678901', '2005-03-15'),
       (2, '23456789012', '2004-07-22'),
       (3, '34567890123', '2003-11-10'),
       (4, '45678901234', '2002-05-18'),
       (5, '56789012345', '2001-09-30');

CREATE TABLE IF NOT EXISTS pessoa_juridica
(
    id_cliente         int         not null,
    cnpj               varchar(14) not null,
    inscricao_estadual varchar(15) not null,
    CONSTRAINT pk_pessoa_juridica primary key (id_cliente),
    CONSTRAINT pk_pessoa_juridica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) Engine InnoDB;

INSERT INTO cliente (nome, celular, email, data_cadastro)
VALUES ('Felipe Rocha', '93333333333', 'felipe.rocha@email.com', '2026-05-06'),
       ('Gisele Martins', '92222222222', 'gisele.martins@email.com', '2026-05-07'),
       ('Henrique Lima', '91111111111', 'henrique.lima@email.com', '2026-05-08'),
       ('Isabela Gomes', '90000000000', 'isabela.gomes@email.com', '2026-05-09'),
       ('João Pedro', '99999999998', 'joao.pedro@email.com', '2026-05-10');

INSERT INTO pessoa_juridica (id_cliente, cnpj, inscricao_estadual)
VALUES (6, '55566677000188', '456123'),
       (7, '88899900000155', '741852'),
       (8, '11223344000199', '123456'),
       (9, '44556677000166', '789012'),
       (10, '99887766000144', '345678');

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
       ('Vermelho'),
       ('Verde'),
       ('Amarelo'),
       ('Laranja'),
       ('Rosa'),
       ('Roxo'),
       ('Cinza');

CREATE TABLE IF NOT EXISTS marca
(
    id   INT         NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT pk_marca PRIMARY KEY (id)
) ENGINE = INNODB;

INSERT INTO marca (nome)
VALUES ('Volkswagen'),
       ('Nissan'),
       ('Fiat'),
       ('Toyota'),
       ('Honda'),
       ('Ford'),
       ('Chevrolet'),
       ('BMW'),
       ('Mercedes-Benz'),
       ('Audi'),
       ('Hyundai'),
       ('Kia'),
       ('Renault'),
       ('Peugeot'),
       ('Citroën');

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
VALUES ('Lavagem', 25.00, 'MOTO'),
       ('Lavagem', 35.00, 'PEQUENO'),
       ('Lavagem', 45.00, 'PADRAO'),
       ('Lavagem', 55.00, 'MEDIO'),
       ('Lavagem', 65.00, 'GRANDE'),

       ('Polimento', 5.00, 'MOTO'),
       ('Polimento', 10.00, 'PEQUENO'),
       ('Polimento', 15.00, 'PADRAO'),
       ('Polimento', 20.00, 'MEDIO'),
       ('Polimento', 25.00, 'GRANDE'),

       ('Higenização', 20.00, 'PEQUENO'),
       ('Higenização', 30.00, 'MEDIO'),
       ('Higenização', 40.00, 'PADRAO'),
       ('Higenização', 50.00, 'GRANDE');

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

INSERT INTO modelo
    (descricao, categoria, id_marca)
VALUES ('Voyage', 'PEQUENO', 1),
       ('Frontier', 'GRANDE', 2),
       ('Argo', 'PEQUENO', 3),
       ('Cronos', 'MEDIO', 3),
       ('Virtus', 'MEDIO', 1);

INSERT INTO modelo
    (descricao, categoria, id_marca)
VALUES
-- PEQUENO
('Gol', 'PEQUENO', 1),
('Uno', 'PEQUENO', 3),
('Kwid', 'PEQUENO', 13),
('March', 'PEQUENO', 2),

-- MEDIO
('Cronos', 'MEDIO', 3),
('Virtus', 'MEDIO', 1),
('Civic', 'MEDIO', 5),
('Corolla', 'MEDIO', 4),

-- GRANDE
('Frontier', 'GRANDE', 2),
('Amarok', 'GRANDE', 1),
('Ranger', 'GRANDE', 6),
('Hilux', 'GRANDE', 4),

-- MOTO
('CB 500', 'MOTO', 5),
('XJ6', 'MOTO', 14),
('MT-07', 'MOTO', 4),
('Rebel 500', 'MOTO', 5),

-- PADRAO
('Scenic', 'PADRAO', 13),
('Duster', 'PADRAO', 13),
('Sportage', 'PADRAO', 12),
('Sportage', 'PADRAO', 12);

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

INSERT INTO motor
    (potencia, tipo_combustivel)
VALUES (82, 'FLEX'),
       (82, 'FLEX'),
       (110, 'FLEX'),
       (120, 'FLEX'),
       (140, 'FLEX'),
       (150, 'FLEX'),
       (180, 'FLEX'),
       (203, 'FLEX'),
       (160, 'DIESEL'),
       (163, 'DIESEL'),
       (213, 'FLEX'),
       (163, 'DIESEL'),
       (50, 'GASOLINA'),
       (60, 'GASOLINA'),
       (75, 'GASOLINA'),
       (46, 'GASOLINA'),
       (140, 'FLEX'),
       (160, 'FLEX'),
       (168, 'FLEX'),
       (190, 'FLEX');

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

INSERT INTO veiculo (placa, observacao, id_cor, id_modelo, id_cliente)
VALUES ('ABC1D23', 'Carro novo', 1, 1, 1),
       ('DEF4G56', 'Utilizado para entregas', 2, 2, 2),
       ('GHI7J89', 'Veículo familiar', 3, 3, 3),
       ('JKL0M12', 'Sedan executivo', 4, 4, 4),
       ('NOP3Q45', 'Motor recém revisado', 5, 5, 5),
       ('RST6U78', 'Carro novo', 6, 6, 6),
       ('VWX9Y01', 'Utilizado para entregas', 7, 7, 7),
       ('ZAB2C34', 'Veículo familiar', 8, 8, 8),
       ('DEF5G67', 'Sedan executivo', 9, 9, 9),
       ('GHI8J90', 'Motor recém revisado', 10, 10, 10),
       ('JKL1M23', 'Carro novo', 1, 11, 1),
       ('NOP4Q56', 'Utilizado para entregas', 2, 12, 2),
       ('RST7U89', 'Veículo familiar', 3, 13, 3),
       ('VWX0Y12', 'Sedan executivo', 4, 14, 4),
       ('ZAB3C45', 'Motor recém revisado', 5, 15, 5),
       ('DEF6G78', 'Carro novo', 6, 16, 6),
       ('GHI9J01', 'Utilizado para entregas', 7, 17, 7),
       ('JKL2M34', 'Veículo familiar', 8, 18, 8),
       ('NOP5Q67', 'Sedan executivo', 9, 19, 9),
       ('RST8U90', 'Motor recém revisado', 10, 20, 10);

CREATE TABLE IF NOT EXISTS ordem_servico
(
    id         INT            NOT NULL AUTO_INCREMENT,
    numero     LONG           NOT NULL,
    total      DECIMAL(10, 2) NOT NULL,
    agenda     DATE           NOT NULL,
    desconto   DECIMAL(5, 2),
    status     ENUM ('ABERTA', 'FECHADA', 'CANCELADA') DEFAULT ('ABERTA'),
    id_veiculo INT            NOT NULL,
    CONSTRAINT pk_ordem_servico PRIMARY KEY (id),
    CONSTRAINT fk_veiculo FOREIGN KEY (id_veiculo)
        REFERENCES veiculo (id)
) ENGINE = InnoDB;

INSERT INTO ordem_servico
    (numero, total, agenda, desconto, status, id_veiculo)
VALUES (1001, 25.00, '2026-05-10', NULL, 'ABERTA', 1),
       (1002, 45.00, '2026-05-11', NULL, 'FECHADA', 2),
       (1003, 45.00, '2026-05-12', NULL, 'FECHADA', 3),
       (1004, 35.00, '2026-05-13', NULL, 'ABERTA', 4),
       (1005, 45.00, '2026-05-14', NULL, 'FECHADA', 5),
       (1006, 25.00, '2026-05-15', NULL, 'ABERTA', 6),
       (1007, 35.00, '2026-05-16', NULL, 'ABERTA', 7),
       (1008, 45.00, '2026-05-17', NULL, 'FECHADA', 8),
       (1009, 35.00, '2026-05-18', NULL, 'ABERTA', 9),
       (1010, 45.00, '2026-05-19', NULL, 'FECHADA', 10);

CREATE TABLE IF NOT EXISTS item_os
(
    id               INT            NOT NULL AUTO_INCREMENT,
    valor_servico    DECIMAL(10, 2) NOT NULL,
    observacoes      VARCHAR(300)   NULL,
    id_servico       INT            NOT NULL,
    id_ordem_servico INT            NOT NULL,

    CONSTRAINT pk_item_os PRIMARY KEY (id),

    CONSTRAINT fk_ordem_servio FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_servico FOREIGN KEY (id_servico)
        REFERENCES servico (id)
) ENGINE = InnoDB;

INSERT INTO item_os
    (valor_servico, observacoes, id_servico, id_ordem_servico)
VALUES (25.00, '', 1, 1),
       (45.00, '', 3, 2),
       (45.00, '', 3, 3),
       (35.00, '', 1, 4),
       (45.00, '', 3, 5),
       (25.00, '', 1, 6),
       (35.00, '', 2, 7),
       (45.00, '', 3, 8),
       (35.00, '', 2, 9),
       (45.00, '', 3, 10);