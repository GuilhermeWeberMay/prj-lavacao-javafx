package br.edu.ifsc.fln.model.domain;

import br.edu.ifsc.fln.exception.ExceptionLavacao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrdemServico {
    private long  numero;
    private double  total;
    private LocalDate agenda;
    private double desconto;
    private EStatus status;
    private Veiculo veiculo;
    private List<ItemOS> itensOS =  new ArrayList<>();

    public double getTotal() throws ExceptionLavacao {
        if (total < 0){
            throw new ExceptionLavacao("Não há valor total pois não há serviço vinculado");
        }else {
            for (ItemOS itemOS : itensOS) {
                total += itemOS.getServico().getValor();
            }
            return total;
        }
    }

    public List<ItemOS> getItensOS() throws ExceptionLavacao {
        if (itensOS.isEmpty()){
            throw new ExceptionLavacao("Não há serviços na lista para serem calculados");
        }else {
            return itensOS;
        }
    }

    public double calcularServico() throws ExceptionLavacao {
        if (itensOS.isEmpty()) {
            throw new ExceptionLavacao("Não há serviços na lista para serem calculados");
        }else{
            return total -= total * (getDesconto() / 100);
        }
    }

    public void add(ItemOS itemOS) throws ExceptionLavacao{
        if(itensOS.contains(itemOS)){
            throw new ExceptionLavacao("Esse serviço já está na ordem de serviço");
        }else {
            itensOS.add(itemOS);
        }
    }

    public void remove(ItemOS itemOS) throws  ExceptionLavacao{
        if(!itensOS.contains(itemOS)){
            throw new ExceptionLavacao("Esse serviço não está na ordem de serviço");
        }else {
            itensOS.add(itemOS);
        }
    }
}
