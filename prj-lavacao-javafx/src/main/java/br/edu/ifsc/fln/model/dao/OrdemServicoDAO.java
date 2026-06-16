package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrdemServicoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(OrdemServico ordemServico) {
        String sql = "INSERT INTO ordem_servico(numero, total, agenda, desconto, status, id_veiculo) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.setDouble(2, ordemServico.getTotal());
            stmt.setDate(3, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(4, ordemServico.getDesconto());
            stmt.setString(5, ordemServico.getStatus().name());
//            if  (ordemServico.getStatusOrdemServico() != null) {
//                stmt.setString(6, ordemServico.getStatusOrdemServico().name());
//            } else {
//                //TODO apresentar situação clara de inconsistência de dados
//                //tratamento de exceções e a necessidade de uso de commit e rollback
//                //stmt.setString(6, "teste");
//                //stmt.setString(6, EStatusOrdemServico.ABERTA.name());
//            }
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.execute();

            ItemOsDAO itemOsDAO = new ItemOsDAO();
            itemOsDAO.setConnection(connection);

            for (ItemOS itemOs: ordemServico.getItensOS()) {
                itemOs.setOrdemServico(this.buscarUltimaOrdemServico());
                itemOsDAO.inserir(itemOs);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordem_servico SET numero=?, total=?, agenda=?, desconto=?, status=?, id_veiculo=? WHERE id=?";
        try {
            //antes de atualizar a nova ordemServico, a anterior terá seus itens de ordemServico removidos
            // e o estoque dos produtos da ordemServico sofrerão um estorno
            connection.setAutoCommit(false);
            ItemOsDAO itemOsDAO = new ItemOsDAO();
            itemOsDAO.setConnection(connection);

            //OrdemServico ordemServicoAnterior = buscar(ordemServico.getCdOrdemServico());
            OrdemServico ordemServicoAnterior = buscar(ordemServico);
            List<ItemOS> itensDeOs = itemOsDAO.listarPorOrdemServico(ordemServicoAnterior);
            for (ItemOS iv : itensDeOs) {
                //Produto p = iv.getProduto(); //isto não da certo ...
                itemOsDAO.remover(iv);
            }
            //atualiza os dados da ordemServico
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.setDouble(2, ordemServico.getTotal());
            stmt.setDate(3, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(4, ordemServico.getDesconto());
            if  (ordemServico.getStatus() != null) {
                stmt.setString(5, ordemServico.getStatus().name());
            } else {
                stmt.setString(5, EStatus.ABERTA.name());
            }
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.setInt(7, ordemServicoAnterior.getId());
            stmt.execute();
            for (ItemOS iv: ordemServico.getItensOS()) {
                itemOsDAO.inserir(iv);
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException exc1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(OrdemServico ordemServico) {
        String sql = "DELETE FROM ordem_servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemOsDAO itemOsDAO = new ItemOsDAO();
                itemOsDAO.setConnection(connection);
                for (ItemOS itemOs : ordemServico.getItensOS()) {
                    itemOsDAO.remover(itemOs);
                }
                stmt.setInt(1, ordemServico.getId());
                stmt.execute();
                connection.commit();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<OrdemServico> listar() {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                List<ItemOS> itensOS = new ArrayList<>();

                ordemServico.setId(resultado.getInt("id"));
                ordemServico.setNumero(resultado.getLong("numero"));
                ordemServico.setTotal(resultado.getDouble("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));

                //Obtendo os dados completos do Veiculo associado à OrdemServico
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo.getId());

                //Obtendo os dados completos dos Itens de OrdemServico associados à OrdemServico
                ItemOsDAO itemOsDAO = new ItemOsDAO();
                itemOsDAO.setConnection(connection);
                itensOS = itemOsDAO.listarPorOrdemServico(ordemServico);

                ordemServico.setVeiculo(veiculo);
                ordemServico.setItensOS(itensOS);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordem_servico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, ordemServico.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setId(resultado.getInt("id"));
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
                ordemServicoRetorno.setTotal(resultado.getDouble("total"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscar(int id) {
        /*
            Método necessário para evitar que a instância de retorno seja 
            igual a instância a ser atualizada.
        */
        String sql = "SELECT * FROM ordem_servico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setId(resultado.getInt("id"));
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
                ordemServicoRetorno.setTotal(resultado.getDouble("total"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscarUltimaOrdemServico() {
        String sql = "SELECT max(id) as max FROM ordem_servico";

        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setId(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Map<Integer, ArrayList> listarQuantidadeVendasPorMes() {
        String sql = """
                        select count(id) as count, extract(year from agenda) as ano,
                                         extract(month from agenda) as mes from ordem_servico group by ano,
                                        mes order by ano, mes;
                       """;
        Map<Integer, ArrayList> retorno = new HashMap();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                ArrayList linha = new ArrayList();
                if (!retorno.containsKey(resultado.getInt("ano")))
                {
                    linha.add(resultado.getInt("mes"));
                    linha.add(resultado.getInt("count"));
                    retorno.put(resultado.getInt("ano"), linha);
                }else{
                    ArrayList linhaNova = retorno.get(resultado.getInt("ano"));
                    linhaNova.add(resultado.getInt("mes"));
                    linhaNova.add(resultado.getInt("count"));
                }
            }
            if (retorno.size() > 0) {
                retorno = ordenar(retorno);
            }
            return retorno;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
        return retorno;
    }

    private Map<Integer, ArrayList> ordenar(Map<Integer, ArrayList> vendas) {
        LinkedHashMap<Integer, ArrayList> orderedMap = vendas.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, //
                        (key, content) -> content, //
                        LinkedHashMap::new));
        return orderedMap;
    }
}
