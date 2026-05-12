package br.edu.ifsc.fln.model.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class Cliente {
    int id;
    String nome;
    String email;
    String celular;
    LocalDate dataCadastro;
    // Notação do Lombok para ele não implementar o método setter - regra de negócio
    @Setter(AccessLevel.NONE)
    List<Veiculo> veiculos = new ArrayList<>();

    public void add(Veiculo veiculo){
        this.veiculos.add(veiculo);
        veiculo.setCliente(this);
    }

    public void remove(Veiculo veiculo){
        this.veiculos.remove(veiculo);
        veiculo.setCliente(null);
    }

    @Override
    public String toString() {
        return nome;
    }
}
