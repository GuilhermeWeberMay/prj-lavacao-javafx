package br.edu.ifsc.fln.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ECategoria {
    PEQUENO("Pequeno"),
    MEDIO("Medio"),
    GRANDE("Grande"),
    MOTO("Moto"),
    PADRAO("Padrão");

    private String descricao;

    @Override
    public String toString() {
        return descricao;
    }
}
