package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemOsDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemOS itemDeVenda) {
        String sql = "INSERT INTO item_os(valor_servico, observaocoes, id_servico, id_ordem_servico) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, itemDeVenda.getValorServico());
            stmt.setString(2, itemDeVenda.getObservacoes());
            stmt.setInt(3, itemDeVenda.getServico().getId());
            stmt.setInt(4, itemDeVenda.getOrdemServico().getId());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(ItemOS itemDeVenda) {
        return true;
    }

    public boolean remover(ItemOS itemDeVenda) {
        String sql = "DELETE FROM item_os WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemDeVenda.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<ItemOS> listar() {
        String sql = "SELECT * FROM item_os";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();

                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valor_servico"));
                itemOS.setObservacoes(resultado.getString("observacoes"));

                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setId(resultado.getInt("id_ordem_servico"));

                //Obtendo os dados completos do Serviço associado ao Item da OS
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico.getId());

                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico.getId());

                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);

                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<ItemOS> listarPorOrdemServico(OrdemServico ordemServico) {
        String sql = "SELECT * FROM item_os WHERE id_ordem_servico=?";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, ordemServico.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico os = new OrdemServico();

                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valor_servico"));
                itemOS.setObservacoes(resultado.getString("observacoes"));

                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setId(resultado.getInt("id_ordem_servico"));

                //Obtendo os dados completos do Serviço associado ao Item da OS
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico.getId());

                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico.getId());

                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);

                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public ItemOS buscar(ItemOS itemOs) {
        String sql = "SELECT * FROM item_os WHERE id=?";
        ItemOS retorno = new ItemOS();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemOs.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();

                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valor_servico"));
                itemOS.setObservacoes(resultado.getString("observacoes"));

                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setId(resultado.getInt("id_ordem_servico"));

                //Obtendo os dados completos do Cliente associado à Venda
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico.getId());

                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico.getId());

                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);

                retorno = itemOS;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
