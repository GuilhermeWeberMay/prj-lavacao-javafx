package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class DialogCadastroVeiculoController implements Initializable {
    @Getter
    @Setter
    private Stage dialogStage;
    @Getter
    @Setter
    private boolean btConfirmarClicked = false;
    @Getter
    @Setter
    private Veiculo veiculo;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CorDAO corDAO = new CorDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();

    @FXML
    private ChoiceBox<Cliente> cbClienteVeiculo;

    @FXML
    private ChoiceBox<Cor> cbCorVeiculo;

    @FXML
    private ChoiceBox<Modelo> cbModeloVeiculo;

    @FXML
    private TextField textFielObservacaoVeiculo;

    @FXML
    private TextField textFielPlacaVeiculo;

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        this.textFielPlacaVeiculo.setText(veiculo.getPlaca());
        this.textFielObservacaoVeiculo.setText(veiculo.getObservacao());

        if (veiculo.getModelo() != null && veiculo.getCor() != null && veiculo.getCliente() != null) {
            this.cbModeloVeiculo.setValue(veiculo.getModelo());
            this.cbCorVeiculo.setValue(veiculo.getCor());
            this.cbClienteVeiculo.setValue(veiculo.getCliente());
        }
    }

    @FXML
    void handleButtonCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleButtonConfirmar() {
        veiculo.setPlaca(textFielPlacaVeiculo.getText());
        veiculo.setObservacao(textFielObservacaoVeiculo.getText());
        veiculo.setModelo(cbModeloVeiculo.getValue());
        veiculo.setCor(cbCorVeiculo.getValue());
        veiculo.setCliente(cbClienteVeiculo.getValue());

        btConfirmarClicked = true;
        dialogStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clienteDAO.setConnection(connection);
        corDAO.setConnection(connection);
        modeloDAO.setConnection(connection);

        List<Cliente> clientes = clienteDAO.listar();
        List<Cor> cores = corDAO.listar();
        List<Modelo> modelos = modeloDAO.listar();

        //Adicionando os tipos de enum aos itens do ChoiceBox
        cbClienteVeiculo.getItems().addAll(clientes);
        cbCorVeiculo.getItems().addAll(cores);
        cbModeloVeiculo.getItems().addAll(modelos);
    }
}
