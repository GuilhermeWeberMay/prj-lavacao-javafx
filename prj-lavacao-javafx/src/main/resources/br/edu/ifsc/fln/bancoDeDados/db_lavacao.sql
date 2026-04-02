CREATE DATABASE IF NOT EXISTS db_lavacao;
USE db_lavacao;

CREATE TABLE IF NOT EXISTS cor(
 id int not null unique auto_increment primary key,
 nome varchar(50) not null
 )engine InnoDB;
 
 INSERT INTO cor (nome) VALUES ('Azul'), ('Preto'), ('Branco'), ('Vermelho');
 
 CREATE TABLE IF NOT EXISTS marca(
  id int not null unique auto_increment primary key,
 nome varchar(50) not null
 )engine InnoDB;
 
 INSERT INTO marca (nome) VALUES ('Volksvagen') , ('Nissan'), ('Fiat');