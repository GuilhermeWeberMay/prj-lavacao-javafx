package br.edu.ifsc.fln.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Marca {
    @ToString.Exclude
    private int id;
    private String nome;

    public Marca (String nome){
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
