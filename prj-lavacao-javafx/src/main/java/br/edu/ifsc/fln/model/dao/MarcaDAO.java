package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Marca;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class MarcaDAO {

    private Connection connection;

    public boolean create (Marca marca){
        // Intrução SQL
        String sql = "INSERT INTO marca(nome) VALUES (?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca.getNome());
            stmt.execute();
            return true;
        }catch (SQLException ex){
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
