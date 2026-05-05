package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public abstract class Cliente {
    int id;
    String nome;
    String email;
    String celular;
    LocalDate dataCadastro;
}
