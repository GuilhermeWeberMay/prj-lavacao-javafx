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
public class Marca {
    private int id;
    private String nome;

    public Marca (String nome){
        this.nome = nome;
    }
}
