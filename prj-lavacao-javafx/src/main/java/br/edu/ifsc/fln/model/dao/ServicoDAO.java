package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void create(Servico servico) throws DAOException {
        String sql = "INSERT INTO servico(descricao, valor, categoria) VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setString(3, servico.getCategoria().getDescricao());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel cadastrar o serviço "+ex);
        }
    }

    public void alterar(Servico servico) throws DAOException {
        String sql = "UPDATE servico SET descricao=?,valor=?, categoria=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setString(3, servico.getCategoria().getDescricao());
            stmt.setInt(4, servico.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel alterar o serviço "+ex);
        }
    }

    public void remover(Servico servico) throws DAOException {
        String sql = "DELETE FROM servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            stmt.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel deletar o serviço "+ex);
        }
    }

    public List<Servico> listar() throws DAOException {
        String sql = "SELECT * FROM servico";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setValor(resultado.getDouble("valor"));
                servico.setCategoria(Enum.valueOf(ECategoria.class, resultado.getString("categoria")));
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel listar o serviço "+ex);
        }
        return retorno;
    }

    public Servico buscar(Servico servico) throws DAOException {
        try {
            Servico retorno = buscar(servico.getId());
            return retorno;
        }catch (DAOException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel cadastrar o serviço "+ex);
        }
    }

    public Servico buscar(int id) throws DAOException {
        String sql = "SELECT * FROM servico WHERE id=?";
        Servico retorno = new Servico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setId(resultado.getInt("id"));
                retorno.setDescricao(resultado.getString("descricao"));
                retorno.setValor(resultado.getDouble("valor"));
                retorno.setCategoria(Enum.valueOf(ECategoria.class, resultado.getString("categoria")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel buscar o serviço "+ex);
        }
        return retorno;
    }

    public List<Servico> listarCategoria(ECategoria categoria) throws DAOException {
        String sql = "SELECT * FROM servico where  categoria=?";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, categoria.getDescricao());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setValor(resultado.getDouble("valor"));
                servico.setCategoria(Enum.valueOf(ECategoria.class, resultado.getString("categoria")));
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possivel listar o serviço "+ex);
        }
        return retorno;
    }
}

