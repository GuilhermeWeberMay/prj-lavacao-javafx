package br.edu.ifsc.fln.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EStatus {
    ABERTA("Aberta"),
    FECHADA("Fechada"),
    CANCELADA("Cancelada");
    private String descricao;
}
