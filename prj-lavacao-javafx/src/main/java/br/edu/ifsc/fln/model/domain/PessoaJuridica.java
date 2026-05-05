package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String inscricaoEstadual;
}
