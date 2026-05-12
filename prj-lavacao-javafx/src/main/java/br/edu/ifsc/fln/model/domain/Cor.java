package br.edu.ifsc.fln.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Cor {
    @ToString.Exclude
    private int id;
    private String nome;

    @Override
    public String toString() {
        return nome;
    }
}
