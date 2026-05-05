package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Veiculo {
    private int id;
    private String placa;
    private String observacao;
    // Relacionamento Unidericional
    private Cor cor;
    // Relacionamento Unidericional
    private Modelo modelo;
    // relacionamento bidericional
    private Cliente cliente;
}
