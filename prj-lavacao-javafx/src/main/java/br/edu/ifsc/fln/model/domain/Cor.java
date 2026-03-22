package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Cor {
    private int id;
    private String nome;

    public Cor(String nome) {
        this.nome = nome;
    }
}
