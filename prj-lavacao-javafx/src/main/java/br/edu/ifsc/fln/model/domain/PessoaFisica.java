package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class PessoaFisica extends Cliente{
    private String cpf;
    private LocalDate dataNascimento;
}
