package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Motor {
    private int potencia;
    private ETipoCombustivel tipoCombustivel;
    //private Modelo modelo; Por que isso existe? Se é unidirecional?

    @Override
    public String toString() {
        return "Motor{" +
                "potencia=" + potencia +
                ", tipoCombustivel=" + tipoCombustivel +
                '}';
    }
}
