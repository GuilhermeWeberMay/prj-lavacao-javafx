package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Modelo {
    private int id;
    private String descricao;
    private Marca marca;
    // private Motor motor;
    private ECategoria categoria;

    public Modelo(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Modelo(int id, String descricao, Marca marca) {
        this.id = id;
        this.descricao = descricao;
        this.marca = marca;
    }

//    private void createMotor(ETipoCombustivel tipoCombustivel, int potencia) {
//        this.motor = new Motor();
//        this.motor.setTipoCombustivel(tipoCombustivel);
//        this.motor.setPotencia(potencia);
//    }
}
