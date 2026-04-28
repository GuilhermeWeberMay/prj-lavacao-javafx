package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@ToString
public class Modelo {
    private int id;
    private String descricao;
    private Marca marca;
     private Motor motor;
    private ECategoria categoria;

    public Modelo(){
        this.motor = new Motor();
    }

    public Modelo(int id, String descricao, Marca marca, Motor motor, ECategoria categoria, int potencia, ETipoCombustivel tipoCombustivel) {
        this.id = id;
        this.descricao = descricao;
        this.marca = marca;
        this.motor = motor;
        this.categoria = categoria;
        this.motor = new Motor(potencia, tipoCombustivel);
    }

    public void setMotor(int potencia, ETipoCombustivel tipoCombustivel) {
        this.motor = new  Motor(potencia,  tipoCombustivel);
    }
}
