package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        final String sql = "INSERT INTO veiculo(placa, observacao, id_cor, id_modelo, id_cliente) VALUES(?,?,?,?,?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            //registra o veiculo
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacao());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacao=?, id_cor=?, id_modelo=?, id_cliente=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacao());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar() {
        String sql =
                "SELECT v.id as id_veiculo, v.placa as placa, v.observacao as observacoes, " +
                        "cor.id as id_cor, cor.nome as nome_cor, " +
                        "mdl.id as id_modelo, mdl.descricao as desc_modelo, mdl.categoria as categoria_modelo, " +
                        "mot.potencia as potencia, mot.tipo_combustivel as combustivel," +
                        "mrc.id as id_marca, mrc.nome as nome_marca," +
                        "c.id as id_cliente, c.nome as nome_cliente, c.celular as celular_cliente, c.email as email_cliente," +
                        "c.data_cadastro as  data_cadastro," +
                        "pf.cpf as cpf, pf.data_nascimento as data_nasc," +
                        "pj.cnpj as cnpj, pj.incricao_estadual as inscricao_estadual" +
                        "FROM veiculo v INNER JOIN cor ON v.id_cor = cor.id" +
                        "INNER JOIN modelo mdl ON v.id_modelo = mdl.id" +
                        "INNER JOIN marca mrc ON mdl.id = mrc.id" +
                        "INNER JOIN motor mot ON mdl.id = mot.id_modelo" +
                        "INNER JOIN cliente c ON c.id= v.id_cliente" +
                        "LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id" +
                        "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id WHERE v.id = ?;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Veiculo> listagem() {
        String sql =
                "SELECT v.id as id_veiculo, v.placa as placa, v.observacao as observacoes, " +
                        "cor.id as id_cor, cor.nome as nome_cor, " +
                        "mdl.id as id_modelo, mdl.descricao as desc_modelo, mdl.categoria as categoria_modelo, " +
                        "mot.potencia as potencia, mot.tipo_combustivel as combustivel, " +
                        "mrc.id as id_marca, mrc.nome as nome_marca, " +
                        "c.id as id_cliente, c.nome as nome_cliente, c.celular as celular_cliente, c.email as email_cliente, " +
                        "c.data_cadastro as  data_cadastro, " +
                        "pf.cpf as cpf, pf.data_nascimento as data_nasc, " +
                        "pj.cnpj as cnpj, pj.inscricao_estadual as inscricao_estadual " +
                        "FROM veiculo v INNER JOIN cor ON v.id_cor = cor.id " +
                        "INNER JOIN modelo mdl ON v.id_modelo = mdl.id " +
                        "INNER JOIN marca mrc ON mdl.id = mrc.id " +
                        "INNER JOIN motor mot ON mdl.id = mot.id_modelo " +
                        "INNER JOIN cliente c ON c.id= v.id_cliente " +
                        "LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id " +
                        "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVOFull(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Veiculo> listarPorCategoria(Marca marca) {
        String sql =
                "SELECT p.id as veiculo_id, p.nome as veiculo_nome, p.descricao as veiculo_descricao, p.preco as veiculo_preco, "
                + "c.id as categoria_id, c.descricao as categoria_descricao "
                + "FROM veiculo p INNER JOIN categoria c ON c.id = p.id_categoria WHERE c.id = ?;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Veiculo buscar(int id) {
        String sql =
                "SELECT v.id as id_veiculo, v.placa as placa, v.observacao as observacoes, " +
                        "cor.id as id_cor, cor.nome as nome_cor, " +
                        "mdl.id as id_modelo, mdl.descricao as desc_modelo, mdl.categoria as categoria_modelo, " +
                        "mot.potencia as potencia, mot.tipo_combustivel as combustivel," +
                        "mrc.id as id_marca, mrc.nome as nome_marca," +
                        "c.id as id_cliente, c.nome as nome_cliente, c.celular as celular_cliente, c.email as email_cliente," +
                        "c.data_cadastro as  data_cadastro," +
                        "pf.cpf as cpf, pf.data_nascimento as data_nasc," +
                        "pj.cnpj as cnpj, pj.incricao_estadual as inscricao_estadual" +
                        "FROM veiculo v INNER JOIN cor ON v.id_cor = cor.id" +
                        "INNER JOIN modelo mdl ON v.id_modelo = mdl.id" +
                        "INNER JOIN marca mrc ON mdl.id = mrc.id" +
                        "INNER JOIN motor mot ON mdl.id = mot.id_modelo" +
                        "INNER JOIN cliente c ON c.id= v.id_cliente" +
                        "LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id" +
                        "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id WHERE v.id = ?;";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Veiculo populateVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Cor cor = new Cor();
        Modelo modelo = new Modelo();
        veiculo.setCor(cor);
        veiculo.setModelo(modelo);

        // Atributos que estão somente na tabela de veículo
        veiculo.setId(rs.getInt("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setObservacao(rs.getString("observacao"));
        // Atributos que vem dos relacionamentos de veículo
        cor.setId(rs.getInt("id_cor"));
        modelo.setId(rs.getInt("id_modelo"));
        int idCliente = rs.getInt("id_cliente");
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        Cliente cliente = clienteDAO.buscar(idCliente);
        veiculo.setCliente(cliente);
        return veiculo;
    }


    private Veiculo populateVOFull(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Cor cor = new Cor();
        Modelo modelo = new Modelo();
        Marca marca = new Marca();

        cor.setId(rs.getInt("id_cor"));
        cor.setNome(rs.getString("nome_cor"));

        marca.setId(rs.getInt("id_marca"));
        marca.setNome(rs.getString("nome_marca"));

        modelo.setId(rs.getInt("id_modelo"));
        modelo.setDescricao(rs.getString("desc_modelo"));
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria_modelo")));
        modelo.setMotor(rs.getInt("potencia"), Enum.valueOf(ETipoCombustivel.class, rs.getString("combustivel")));

        veiculo.setId(rs.getInt("id_veiculo"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setObservacao(rs.getString("observacoes"));

        Cliente cliente;
        if (rs.getString("cnpj") == null) {
            cliente = new PessoaFisica();
            ((PessoaFisica) cliente).setCpf(rs.getString("cpf"));
            ((PessoaFisica)cliente).setDataNascimento(rs.getDate("data_nasc").toLocalDate());
        } else {
            cliente = new PessoaJuridica();
            ((PessoaJuridica)cliente).setCnpj(rs.getString("cnpj"));
            ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
        }
        cliente.setId(rs.getInt("id_cliente"));
        cliente.setNome(rs.getString("nome_cliente"));
        cliente.setCelular(rs.getString("celular_cliente"));
        cliente.setEmail(rs.getString("email_cliente"));
        cliente.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());

        modelo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);
        veiculo.setCliente(cliente);
//        int idCliente = rs.getInt("cliente_id");
//        ClienteDAO clienteDAO = new ClienteDAO();
//        Cliente cliente = clienteDAO.buscar(idCliente);
//        veiculo.setCliente(cliente);
        return veiculo;
    }
}
