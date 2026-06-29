package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void create(Modelo modelo) throws DAOException {
        String sql = "INSERT INTO modelo (descricao, id_marca, categoria) VALUES (?, ?, ?);";
        String sqlMotor = "INSERT INTO motor(potencia, tipo_combustivel, id_modelo) values ( ?, ?, ?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getCategoria().name());

            stmt.execute();

            // Pegando o id de motor
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int idModelo = rs.getInt(1);

            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, modelo.getMotor().getTipoCombustivel().name());
            stmt.setInt(3, idModelo);
            stmt.execute();
            //return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            //return false;
        }
    }

    public void alterar(Modelo modelo) throws DAOException {
        String sql = "UPDATE modelo SET descricao=?, id_marca=?, categoria=? WHERE id=?";
        String sqlMotor = "UPDATE motor SET potencia=?, tipo_combustivel=? where id_modelo=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getCategoria().name());
            stmt.setInt(4, modelo.getId());
            stmt.execute();

            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, modelo.getMotor().getTipoCombustivel().name());
            stmt.setInt(3, modelo.getId());
            stmt.execute();

            //return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            //return false;
        }
    }

    public void remover(Modelo modelo) throws DAOException {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            //return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            //return false;
        }
    }

    public List<Modelo> listar() throws DAOException {
        String sql = "SELECT m.id as modelo_id, m.descricao as modelo_descricao, m.categoria as modelo_categoria, "
                + "ma.id as marca_id, ma.nome as marca_nome "
                + "FROM modelo m INNER JOIN marca ma ON ma.id = m.id_marca;";
        // String sql = "SELECT * FROM PRODUTO";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Modelo> listarPorMarca(Marca marca) throws DAOException {
        String sql = "SELECT * FROM modelo WHERE id_marca = ?;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado, true);
                modelo.setMarca(marca);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) throws DAOException {
        String sql = "SELECT * FROM modelo WHERE id = ?;";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Modelo populateVO(ResultSet rs, boolean comMarca) throws SQLException {
        Modelo modelo = new Modelo();
        //modelo.setMarca(marca);

        modelo.setId(rs.getInt("modelo_id"));
        modelo.setDescricao(rs.getString("modelo_descricao"));
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("modelo_categoria")));
        if (comMarca) {
            Marca marca = new Marca();
            marca.setId(rs.getInt("marca_id"));
            MarcaDAO marcaDAO = new MarcaDAO();
            marcaDAO.setConnection(connection);
            try {
                marca = marcaDAO.buscar(marca);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            modelo.setMarca(marca);
        }

        return modelo;
    }

    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();

        modelo.setId(rs.getInt("modelo_id"));
        modelo.setDescricao(rs.getString("modelo_descricao"));
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("modelo_categoria")));
        Marca marca = new Marca();
        marca.setId(rs.getInt("marca_id"));
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        try {
            marca = marcaDAO.buscar(marca);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        modelo.setMarca(marca);

        return modelo;
    }

}
