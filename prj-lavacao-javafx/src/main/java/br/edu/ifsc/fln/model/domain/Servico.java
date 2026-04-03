package br.edu.ifsc.fln.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Servico {
    private String descricao;
    private Double valor;
    private int pontos;
    private int id;
}
