package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Marca {
    private int id;
    private String nome;

    public Marca (String nome){
        this.nome = nome;
    }
}
