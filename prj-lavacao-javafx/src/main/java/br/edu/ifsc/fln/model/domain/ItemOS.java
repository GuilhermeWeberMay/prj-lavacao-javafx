package br.edu.ifsc.fln.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOS {
    private int id;
    private double valorServico;
    private String observacoes;
    // Classe associativa com as duas classes
    private OrdemServico ordemServico;
    private Servico servico;

    @Override
    public String toString() {
        return "ItemOS{" +
                "id=" + id +
                ", valorServico=" + valorServico +
                ", observacoes='" + observacoes + '\'' +
                ", servico=" + servico +
                '}';
    }
}
