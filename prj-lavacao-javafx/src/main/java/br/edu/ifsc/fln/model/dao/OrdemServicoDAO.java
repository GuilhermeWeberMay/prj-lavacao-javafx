package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                Servico servico = itemOs.getServico();
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
        String sql = "UPDATE ordemServico SET data=?, total=?, pago=?, taxa_desconto=?, empresa=?, situacao=?, id_cliente=? WHERE id=?";
        try {
            //antes de atualizar a nova ordemServico, a anterior terá seus itens de ordemServico removidos
            // e o estoque dos produtos da ordemServico sofrerão um estorno
            connection.setAutoCommit(false);
            ItemDeOrdemServicoDAO itemDeOrdemServicoDAO = new ItemDeOrdemServicoDAO();
            itemDeOrdemServicoDAO.setConnection(connection);

            //OrdemServico ordemServicoAnterior = buscar(ordemServico.getCdOrdemServico());
            OrdemServico ordemServicoAnterior = buscar(ordemServico);
            List<ItemDeOrdemServico> itensDeOrdemServico = itemDeOrdemServicoDAO.listarPorOrdemServico(ordemServicoAnterior);
            for (ItemDeOrdemServico iv : itensDeOrdemServico) {
                //Produto p = iv.getProduto(); //isto não da certo ...
                Produto p = estoqueDAO.getEstoque(iv.getProduto());
                p.getEstoque().repor(iv.getQuantidade());
                estoqueDAO.atualizar(p.getEstoque());
                itemDeOrdemServicoDAO.remover(iv);
            }
            //atualiza os dados da ordemServico
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(ordemServico.getData()));
            stmt.setBigDecimal(2, ordemServico.getTotal());
            stmt.setBoolean(3, ordemServico.isPago());
            stmt.setDouble(4, ordemServico.getTaxaDesconto());
            stmt.setString(5, OrdemServico.getEmpresa());
            if  (ordemServico.getStatusOrdemServico() != null) {
                stmt.setString(6, ordemServico.getStatusOrdemServico().name());
            } else {
                stmt.setString(6, EStatusOrdemServico.ABERTA.name());
            }
            stmt.setInt(7, ordemServico.getCliente().getId());
            stmt.setInt(8, ordemServico.getId());
            stmt.execute();
            for (ItemDeOrdemServico iv: ordemServico.getItensDeOrdemServico()) {
                //Produto p = iv.getProduto(); //isto não da certo ...
                Produto p = estoqueDAO.getEstoque(iv.getProduto());
                p.getEstoque().retirar(iv.getQuantidade());
                estoqueDAO.atualizar(p.getEstoque());
                itemDeOrdemServicoDAO.inserir(iv);
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
        String sql = "DELETE FROM ordemServico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemDeOrdemServicoDAO itemDeOrdemServicoDAO = new ItemDeOrdemServicoDAO();
                itemDeOrdemServicoDAO.setConnection(connection);
                for (ItemDeOrdemServico itemDeOrdemServico : ordemServico.getItensDeOrdemServico()) {
                    Produto produto = itemDeOrdemServico.getProduto();
                    produto.getEstoque().repor(itemDeOrdemServico.getQuantidade());
                    estoqueDAO.atualizar(produto.getEstoque());
                    itemDeOrdemServicoDAO.remover(itemDeOrdemServico);
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
        String sql = "SELECT * FROM ordemServico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Cliente cliente = new Cliente();
                List<ItemDeOrdemServico> itensDeOrdemServico = new ArrayList();

                ordemServico.setId(resultado.getInt("id"));
                ordemServico.setData(resultado.getDate("data").toLocalDate());
                //ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setPago(resultado.getBoolean("pago"));
                ordemServico.setTaxaDesconto(resultado.getDouble("taxa_desconto"));
                ordemServico.setStatusOrdemServico(Enum.valueOf(EStatusOrdemServico.class, resultado.getString("situacao")));
                OrdemServico.setEmpresa(resultado.getString("empresa"));
                cliente.setId(resultado.getInt("id_cliente"));

                //Obtendo os dados completos do Cliente associado à OrdemServico
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.setConnection(connection);
                cliente = clienteDAO.buscar(cliente);

                //Obtendo os dados completos dos Itens de OrdemServico associados à OrdemServico
                ItemDeOrdemServicoDAO itemDeOrdemServicoDAO = new ItemDeOrdemServicoDAO();
                itemDeOrdemServicoDAO.setConnection(connection);
                itensDeOrdemServico = itemDeOrdemServicoDAO.listarPorOrdemServico(ordemServico);

                ordemServico.setCliente(cliente);
                ordemServico.setItensDeOrdemServico(itensDeOrdemServico);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordemServico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, ordemServico.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente();
                ordemServicoRetorno.setId(resultado.getInt("id"));
                ordemServicoRetorno.setData(resultado.getDate("data").toLocalDate());
//                ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setStatusOrdemServico(Enum.valueOf(EStatusOrdemServico.class, resultado.getString("situacao")));
                ordemServicoRetorno.setPago(resultado.getBoolean("pago"));
                ordemServicoRetorno.setTaxaDesconto(resultado.getDouble("taxa_desconto"));
                //ordemServicoRetorno.setEmpresa(resultado.getString("empresa"));
                cliente.setId(resultado.getInt("id_cliente"));
                ordemServicoRetorno.setCliente(cliente);
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
        String sql = "SELECT * FROM ordemServico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente();
                ordemServicoRetorno.setId(resultado.getInt("id"));
                ordemServicoRetorno.setData(resultado.getDate("data").toLocalDate());
//                ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setStatusOrdemServico(Enum.valueOf(EStatusOrdemServico.class, resultado.getString("situacao")));
                ordemServicoRetorno.setPago(resultado.getBoolean("pago"));
                ordemServicoRetorno.setTaxaDesconto(resultado.getDouble("taxa_desconto"));
                //ordemServicoRetorno.setEmpresa(resultado.getString("empresa"));
                cliente.setId(resultado.getInt("id_cliente"));
                ordemServicoRetorno.setCliente(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }
    public OrdemServico buscarUltimaOrdemServico() {
        String sql = "SELECT max(id) as max FROM ordemServico";

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
}
