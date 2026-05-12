package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@ToString
public class Modelo {
    @ToString.Exclude
    private int id;
    private String descricao;
    @ToString.Exclude
    private Marca marca;
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Motor motor;
    @ToString.Exclude
    private ECategoria categoria;

    public Modelo() {
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
        this.motor = new Motor(potencia, tipoCombustivel);
    }

    @Override
    public String toString() {
        return descricao;

    }
}
