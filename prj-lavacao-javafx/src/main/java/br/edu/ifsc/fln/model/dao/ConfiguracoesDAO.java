package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Configuracoes;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfiguracoesDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<Configuracoes> listar() throws DAOException {
        String sql = "SELECT * FROM configuracoes";
        List<Configuracoes> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Configuracoes config = new Configuracoes();
                config.setId(resultado.getInt("id"));
                config.setPontos(resultado.getInt("pontos_servico"));
                retorno.add(config);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar a potuação ",ex);
        }
        return retorno;
    }

    public void alterar(Configuracoes configuracoes) throws DAOException {
        String sql = "UPDATE configuracoes set pontos_servico = ? where id = 1;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, configuracoes.getPontos());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar a pontuação ",ex);
        }
    }

    public Configuracoes buscar() throws DAOException {
        String sql = "SELECT * FROM configuracoes WHERE id=1";
        Configuracoes retorno = new Configuracoes();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setId(resultado.getInt("id"));
                retorno.setPontos(resultado.getInt("pontos_servico"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar a pontuação ",ex);
        }
        return retorno;
    }
}
