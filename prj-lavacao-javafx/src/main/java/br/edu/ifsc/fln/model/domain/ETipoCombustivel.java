package br.edu.ifsc.fln.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ETipoCombustivel {
    GASOLINA("Gasolina"), ETANOL("Etanol"), FLEX("Flex"), DIESEL("Diesel"), GNV("GNV"), OUTRO("Outro");
    private String descricao;
}
