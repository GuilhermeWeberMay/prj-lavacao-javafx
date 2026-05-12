package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String inscricaoEstadual;

    @Override
    public String toString() {
        return nome;
    }
}
