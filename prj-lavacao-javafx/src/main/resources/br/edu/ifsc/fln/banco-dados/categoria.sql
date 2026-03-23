CREATE DATABASE IF NOT EXISTS db_lavacao;
USE db_lavacao;

    CREATE TABLE marca(
        id int not null auto_increment,
        descricao varchar(50) not null
    ) engine=InnoDB;