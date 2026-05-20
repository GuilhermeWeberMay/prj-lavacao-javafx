package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CadastroClienteController implements Initializable {

    @FXML
    private AnchorPane anchorPaneCadastroCliente;

    @FXML
    private Label labelCelularCliente;

    @FXML
    private Label labelDataCadastroCliente;

    @FXML
    private Label labelDataInsCliente;

    @FXML
    private Label labelDocumentoCliente;

    @FXML
    private Label labelEmailCliente;

    @FXML
    private Label labelIdCliente;

    @FXML
    private Label labelNomeCliente;

    @FXML
    private TableColumn<Cliente, String> tableColumnDocumento;

    @FXML
    private TableColumn<Cliente, String> tableColumnNome;

    @FXML
    private TableColumn<Cliente, String> tableColumnTIpo;

    @FXML
    private TextField textFielClienteInscEstadual;

    @FXML
    private TextField textFielClienteCnpj;

    @FXML
    private TableView<Cliente> tableViewCliente;

    private List<Cliente> clientes;
    private ObservableList<Cliente> observableClientes;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableViweCliente();

        tableViewCliente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)
                        -> selecionarItemTableViewFornecedores(newValue)
        );
    }

    public void carregarTableViweCliente() {
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnDocumento.setCellValueFactory(new PropertyValueFactory<>("celular"));
        tableColumnTIpo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        instanceof PessoaFisica ? "Pessoa Fisica" : "Pessoa Juridica"));

        clientes = clienteDAO.listar();

        observableClientes = FXCollections.observableArrayList(clientes);
        tableViewCliente.setItems(observableClientes);
    }

    public void selecionarItemTableViewFornecedores(Cliente cliente) {
        if (cliente != null) {
            labelIdCliente.setText(String.valueOf(cliente.getId()));
            labelNomeCliente.setText(cliente.getNome());
            labelCelularCliente.setText(cliente.getCelular());
            labelEmailCliente.setText(cliente.getEmail());
            labelDataCadastroCliente.setText(
                cliente.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (cliente instanceof PessoaFisica) {
                labelDocumentoCliente.setText(((PessoaFisica) cliente).getCpf());
                labelDataInsCliente.setText(String.valueOf(((PessoaFisica) cliente).getDataNascimento()));
            } else {
                labelDocumentoCliente.setText(((PessoaJuridica) cliente).getCnpj());
                labelDataInsCliente.setText(((PessoaJuridica) cliente).getInscricaoEstadual());
            }
        } else {
            labelIdCliente.setText("");
            labelNomeCliente.setText("");
            labelCelularCliente.setText("");
            labelEmailCliente.setText("");
            labelDataCadastroCliente.setText("");
            labelDocumentoCliente.setText("");
            labelDataInsCliente.setText("");
        }

    }

    @FXML
    void buttonCreateCliente() throws IOException {
        Cliente cliente = getTipoCliente();
        if (cliente != null) {
            boolean btConfirmarClicked = showDialogCadastroCliente(cliente);
            if (btConfirmarClicked) {
                clienteDAO.inserir(cliente);
                carregarTableViweCliente();
            }
        }
    }

    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Pessoa Fisica");
        opcoes.add("Pessoa Juridica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(opcoes.getFirst(),opcoes);
        dialog.setTitle("Dialogo de opções");
        dialog.setHeaderText("Selecione um tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("Pessoa Fisica"))
                return new PessoaFisica();
             else
                return new PessoaJuridica();

        } else {
            return null;
        }
    }

    @FXML
    void buttonDeleteCliente() {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o cliente " + cliente.getNome())) {
                clienteDAO.remover(cliente);
                carregarTableViweCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void buttonUpdateCliente() throws IOException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showDialogCadastroCliente(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViweCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Fornecedor na tabela ao lado");
            alert.show();
        }

    }

    private boolean showDialogCadastroCliente(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroCliente.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto cliente para o controller
        DialogCadastroClienteController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}
