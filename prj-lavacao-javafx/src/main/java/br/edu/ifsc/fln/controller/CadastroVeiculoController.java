package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Veiculo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class CadastroVeiculoController implements Initializable {
    @FXML
    private AnchorPane anchorPaneCadastroCliente;

    @FXML
    private Label labelCategoriaModeloVeiculo;

    @FXML
    private Label labelClienteVeiculo;

    @FXML
    private Label labelCombustivelModeloVeiculo;

    @FXML
    private Label labelCorVeiculo;

    @FXML
    private Label labelDescModeloVeiculo;

    @FXML
    private Label labelIdCliente5;

    @FXML
    private Label labelIdVeiculo;

    @FXML
    private Label labelMarcaVeiculo;

    @FXML
    private Label labelObservacaoVeiculo;

    @FXML
    private Label labelPlacaVeiculo;

    @FXML
    private Label labelPotenciaModeloVeiculo;

    @FXML
    private TableColumn<Veiculo, String> tableColumnCor;

    @FXML
    private TableColumn<Veiculo, String> tableColumnMarca;

    @FXML
    private TableColumn<Veiculo, String> tableColumnModelo;

    @FXML
    private TableColumn<Veiculo, String> tableColumnPlaca;

    @FXML
    private TableView<Veiculo> tableViewVeiculo;

    private List<Veiculo> veiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        veiculoDAO.setConnection(connection);

        carregarTableView();

        tableViewVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tableColumnModelo.setCellValueFactory(cellData -> {
            Veiculo veiculo = cellData.getValue();
            String descricaoModelo = veiculo.getModelo().getDescricao();

            return new SimpleStringProperty(descricaoModelo);
        });
        tableColumnMarca.setCellValueFactory(cellData -> {
            Veiculo veiculo = cellData.getValue();
            String marcaVeiculo = veiculo.getModelo().getMarca().getNome();

            return new SimpleStringProperty(marcaVeiculo);
        });
        tableColumnCor.setCellValueFactory(cellData -> {
            Veiculo veiculo = cellData.getValue();
            String corVeiculo = veiculo.getCor().getNome();

            return new SimpleStringProperty(corVeiculo);
        });

        veiculos = veiculoDAO.listagem();

        observableListVeiculos = FXCollections.observableArrayList(veiculos);
        tableViewVeiculo.setItems(observableListVeiculos);
    }

    public void selecionarItemTableView(Veiculo veiculo) {
        if (veiculo != null) {
            labelIdVeiculo.setText(Integer.toString(veiculo.getId()));
            labelPlacaVeiculo.setText(veiculo.getPlaca());
            labelClienteVeiculo.setText(veiculo.getCliente().getNome());
            labelCorVeiculo.setText(veiculo.getCor().getNome());
            labelObservacaoVeiculo.setText(veiculo.getObservacao());

            labelMarcaVeiculo.setText(veiculo.getModelo().getMarca().getNome());
            labelDescModeloVeiculo.setText(veiculo.getModelo().getDescricao());
            labelCategoriaModeloVeiculo.setText(veiculo.getModelo().getCategoria().getDescricao());

            labelPotenciaModeloVeiculo.setText(String.valueOf(veiculo.getModelo().getMotor().getPotencia()));
            labelCombustivelModeloVeiculo.setText(veiculo.getModelo().getMotor().getTipoCombustivel().getDescricao());
        } else {
            labelIdVeiculo.setText("");
            labelPlacaVeiculo.setText("");
            labelClienteVeiculo.setText("");
            labelCorVeiculo.setText("");
            labelObservacaoVeiculo.setText("");
            labelMarcaVeiculo.setText("");
            labelDescModeloVeiculo.setText("");
            labelCategoriaModeloVeiculo.setText("");
            labelPotenciaModeloVeiculo.setText("");
            labelCombustivelModeloVeiculo.setText("");
        }
    }

    @FXML
    void buttonCreateVeiculo() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean buttonConfirmarClicked = showDialogCadastroVeiculo(veiculo);
        if (buttonConfirmarClicked) {
            veiculoDAO.inserir(veiculo);
            carregarTableView();
        }

    }

    @FXML
    void buttonDeleteVeiculo() {
        Veiculo veiculo = tableViewVeiculo.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            veiculoDAO.remover(veiculo);
            carregarTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }

    }

    @FXML
    void buttonUpdateVeiculo() throws IOException {
        Veiculo veiculo = tableViewVeiculo.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean buttonConfirmarClicked = showDialogCadastroVeiculo(veiculo);
            if (buttonConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }

    }

    public boolean showDialogCadastroVeiculo(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroVeiculo.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de veiculos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o veiculo ao controller
        DialogCadastroVeiculoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}
