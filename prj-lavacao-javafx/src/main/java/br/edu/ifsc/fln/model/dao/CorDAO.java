package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void create(Cor cor) throws DAOException {
        String sql = "INSERT INTO cor(nome) VALUES(?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cor.getNome());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível cadastrar cor ", ex);
        }
    }

    public void alterar(Cor cor) throws DAOException {
        String sql = "UPDATE cor SET nome=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cor.getNome());
            stmt.setInt(2, cor.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar cor ", ex);
        }
    }

    public void remover(Cor cor) throws DAOException {
        String sql = "DELETE FROM cor WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cor.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível deletar a cor ", ex);
        }
    }

    public List<Cor> listar() throws DAOException {
        String sql = "SELECT * FROM cor";
        List<Cor> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cor cor = new Cor();
                cor.setId(resultado.getInt("id"));
                cor.setNome(resultado.getString("nome"));
                retorno.add(cor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar a cor ", ex);
        }
        return retorno;
    }

//    public Cor buscar(Cor cor) {
//        Cor retorno = buscar(cor.getId());
//        return retorno;
//    }

    public Cor buscar(int id) throws DAOException {
        String sql = "SELECT * FROM cor WHERE id=?";
        Cor retorno = new Cor();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setId(resultado.getInt("id"));
                retorno.setNome(resultado.getString("nome"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar a cor ", ex);
        }
        return retorno;
    }
}
