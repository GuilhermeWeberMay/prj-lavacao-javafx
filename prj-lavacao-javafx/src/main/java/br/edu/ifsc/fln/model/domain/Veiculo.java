package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Veiculo {
    @ToString.Exclude
    private int id;
    private String placa;
    @ToString.Exclude
    private String observacao;
    // Relacionamento Unidericional
    @ToString.Exclude
    private Cor cor;
    // Relacionamento Unidericional
    @ToString.Exclude
    private Modelo modelo;
    // relacionamento bidericional
    @ToString.Exclude
    private Cliente cliente;

    @Override
    public String toString() {
        return placa + " - " + cliente.getNome();
    }
}
