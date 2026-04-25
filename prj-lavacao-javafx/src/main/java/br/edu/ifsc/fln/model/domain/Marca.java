package br.edu.ifsc.fln.model.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Marca {
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
