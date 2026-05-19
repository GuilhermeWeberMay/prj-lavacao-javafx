DROP DATABASE IF EXISTS db_lavacao1;

CREATE DATABASE db_lavacao1;

USE db_lavacao1;

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
VALUES
    ('João Pedro', '48977776666', 'joao@gmail.com', '2026-05-05'),
    ('Fernanda Lima', '48966665555', 'fernanda@gmail.com', '2026-05-06'),
    ('Mercado Central LTDA', '4832221111', 'financeiro@mercadocentral.com', '2026-05-06'),
    ('Lucas Martins', '48955554444', 'lucas@gmail.com', '2026-05-07'),
    ('Transportadora Águia', '4831112222', 'contato@aguia.com.br', '2026-05-07');

INSERT INTO pessoa_fisica
(cpf, data_nascimento)
VALUES
    (1, '33333333333', '2000-01-10'),
    (2, '44444444444', '1992-12-05'),
    (4, '55555555555', '1988-07-21');

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

INSERT INTO pessoa_juridica
(id_cliente, cnpj, inscricao_estadual)
VALUES
    (3, '55566677000188', '456123'),
    (15, '88899900000155', '741852');

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

INSERT INTO modelo
(descricao, categoria, id_marca)
VALUES
    ('Voyage', 'PEQUENO', 1),
    ('Frontier', 'GRANDE', 2),
    ('Argo', 'PEQUENO', 3),
    ('Cronos', 'MEDIO', 3),
    ('Virtus', 'MEDIO', 1);

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
VALUES
    ('1.6', 'FLEX'),
    ('2.3', 'DIESEL'),
    ('1.3', 'FLEX'),
    ('1.8', 'FLEX'),
    ('1.0', 'FLEX');
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

INSERT INTO veiculo
(placa, observacao, id_cor, id_modelo, id_cliente)
VALUES
    ('JKL5M67', 'Carro novo', 1, 7, 6),
    ('MNO8P90', 'Utilizado para entregas', 2, 8, 10),
    ('RST1U23', 'Veículo familiar', 3, 9, 7),
    ('VWX4Y56', 'Sedan executivo', 4, 10, 8),
    ('HGT7R89', 'Motor recém revisado', 1, 11, 9);

CREATE TABLE IF NOT EXISTS ordem_servico
(
    id       INT 			NOT NULL AUTO_INCREMENT,
    numero 	 LONG 			NOT NULL,
    total 	 DECIMAL(10,2) 	NOT NULL,
    agenda 	 DATE 			NOT NULL,
    desconto DECIMAL(3,1),
    status 	 ENUM ('ABERTA', 'FECHADA', 'CANCELADA') DEFAULT ('ABERTA'),
    id_veiculo INT NOT NULL,
    CONSTRAINT pk_ordem_servico PRIMARY KEY (id),
    CONSTRAINT fk_veiculo FOREIGN KEY (id_veiculo)
        REFERENCES veiculo (id)
) ENGINE = InnoDB;

INSERT INTO ordem_servico
(numero, total, agenda, desconto, status, id_veiculo)
VALUES
    (1001, 50.00, '2026-05-10', 0.0, 'ABERTA', 1),
    (1002, 70.00, '2026-05-11', 5.0, 'FECHADA', 2),
    (1003, 120.00, '2026-05-12', 10.0, 'CANCELADA', 1),
    (1004, 90.00, '2026-05-13', NULL, 'ABERTA', 2),
    (1005, 150.00, '2026-05-14', 15.0, 'FECHADA', 1),
    (1006, 45.00, '2026-05-15', 0.0, 'ABERTA', 2);

CREATE TABLE IF NOT EXISTS item_os
(
    valor_servico DECIMAL(10,2) NOT NULL,
    observacoes VARCHAR(300),
    id_servico  INT NOT NULL,
    id_ordem_servico INT NOT NULL,
    CONSTRAINT pk_item_os PRIMARY KEY (id_ordem_servico),
    CONSTRAINT fk_ordem_servico FOREIGN KEY (id_ordem_servico)
        REFERENCES ordem_servico (id),
    CONSTRAINT fk_servico FOREIGN KEY (id_servico)
        REFERENCES servico (id)
)ENGINE = InnoDB;

INSERT INTO item_os
(valor_servico, observacoes, id_servico, id_ordem_servico)
VALUES
    (50.00, 'Lavagem externa simples', 1, 1),
    (70.00, 'Lavagem com aplicação de cera', 2, 2),
    (120.00, 'Lavagem premium completa', 3, 3),
    (50.00, 'Lavagem rápida realizada', 1, 4),
    (70.00, 'Veículo muito sujo', 2, 5),
    (50.00, 'Lavagem agendada para manhã', 1, 6);DROP DATABASE IF EXISTS db_lavacao1;

CREATE DATABASE db_lavacao1;

USE db_lavacao1;

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
VALUES
    ('João Pedro', '48977776666', 'joao@gmail.com', '2026-05-05'),
    ('Fernanda Lima', '48966665555', 'fernanda@gmail.com', '2026-05-06'),
    ('Mercado Central LTDA', '4832221111', 'financeiro@mercadocentral.com', '2026-05-06'),
    ('Lucas Martins', '48955554444', 'lucas@gmail.com', '2026-05-07'),
    ('Transportadora Águia', '4831112222', 'contato@aguia.com.br', '2026-05-07');

INSERT INTO pessoa_fisica
(cpf, data_nascimento)
VALUES
    (1, '33333333333', '2000-01-10'),
    (2, '44444444444', '1992-12-05'),
    (4, '55555555555', '1988-07-21');

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

INSERT INTO pessoa_juridica
(id_cliente, cnpj, inscricao_estadual)
VALUES
    (3, '55566677000188', '456123'),
    (15, '88899900000155', '741852');

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

INSERT INTO modelo
(descricao, categoria, id_marca)
VALUES
    ('Voyage', 'PEQUENO', 1),
    ('Frontier', 'GRANDE', 2),
    ('Argo', 'PEQUENO', 3),
    ('Cronos', 'MEDIO', 3),
    ('Virtus', 'MEDIO', 1);

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
VALUES
    ('1.6', 'FLEX'),
    ('2.3', 'DIESEL'),
    ('1.3', 'FLEX'),
    ('1.8', 'FLEX'),
    ('1.0', 'FLEX');
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

INSERT INTO veiculo
(placa, observacao, id_cor, id_modelo, id_cliente)
VALUES
    ('JKL5M67', 'Carro novo', 1, 7, 6),
    ('MNO8P90', 'Utilizado para entregas', 2, 8, 10),
    ('RST1U23', 'Veículo familiar', 3, 9, 7),
    ('VWX4Y56', 'Sedan executivo', 4, 10, 8),
    ('HGT7R89', 'Motor recém revisado', 1, 11, 9);

CREATE TABLE IF NOT EXISTS ordem_servico
(
    id       INT 			NOT NULL AUTO_INCREMENT,
    numero 	 LONG 			NOT NULL,
    total 	 DECIMAL(10,2) 	NOT NULL,
    agenda 	 DATE 			NOT NULL,
    desconto DECIMAL(3,1),
    status 	 ENUM ('ABERTA', 'FECHADA', 'CANCELADA') DEFAULT ('ABERTA'),
    id_veiculo INT NOT NULL,
    CONSTRAINT pk_ordem_servico PRIMARY KEY (id),
    CONSTRAINT fk_veiculo FOREIGN KEY (id_veiculo)
        REFERENCES veiculo (id)
) ENGINE = InnoDB;

INSERT INTO ordem_servico
(numero, total, agenda, desconto, status, id_veiculo)
VALUES
    (1001, 50.00, '2026-05-10', 0.0, 'ABERTA', 1),
    (1002, 70.00, '2026-05-11', 5.0, 'FECHADA', 2),
    (1003, 120.00, '2026-05-12', 10.0, 'CANCELADA', 1),
    (1004, 90.00, '2026-05-13', NULL, 'ABERTA', 2),
    (1005, 150.00, '2026-05-14', 15.0, 'FECHADA', 1),
    (1006, 45.00, '2026-05-15', 0.0, 'ABERTA', 2);

CREATE TABLE IF NOT EXISTS item_os
(
    valor_servico DECIMAL(10,2) NOT NULL,
    observacoes VARCHAR(300),
    id_servico  INT NOT NULL,
    id_ordem_servico INT NOT NULL,
    CONSTRAINT pk_item_os PRIMARY KEY (id_ordem_servico),
    CONSTRAINT fk_ordem_servico FOREIGN KEY (id_ordem_servico)
        REFERENCES ordem_servico (id),
    CONSTRAINT fk_servico FOREIGN KEY (id_servico)
        REFERENCES servico (id)
)ENGINE = InnoDB;

INSERT INTO item_os
(valor_servico, observacoes, id_servico, id_ordem_servico)
VALUES
    (50.00, 'Lavagem externa simples', 1, 1),
    (70.00, 'Lavagem com aplicação de cera', 2, 2),
    (120.00, 'Lavagem premium completa', 3, 3),
    (50.00, 'Lavagem rápida realizada', 1, 4),
    (70.00, 'Veículo muito sujo', 2, 5),
    (50.00, 'Lavagem agendada para manhã', 1, 6);