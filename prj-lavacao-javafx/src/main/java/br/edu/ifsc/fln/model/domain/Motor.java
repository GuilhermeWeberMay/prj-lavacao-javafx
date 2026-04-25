package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Motor {
    private int potencia;
    private ETipoCombustivel tipoCombustivel = ETipoCombustivel.GASOLINA;
    private Modelo modelo;
}
