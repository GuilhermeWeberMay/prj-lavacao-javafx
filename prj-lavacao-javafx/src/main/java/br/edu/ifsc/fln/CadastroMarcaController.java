package br.edu.ifsc.fln;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CadastroMarcaController implements Initializable {

    // Atributos para manipulação de BDA
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    @FXML
    private Label labelDescMarca;

    @FXML
    private Label labelIdMarca;

    @FXML
    private TableColumn<Marca, String> tableColumnMarca;

    @FXML
    private TableView<Marca> tableViewMarcas;

    @FXML
    private AnchorPane anchorPane;

    private List<Marca> marcas = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        marcaDAO.setConnection(connection);
        carregarTableViewMarca();

        tableViewMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewMarcas(newValue));
    }

    private void selecionarItemTableViewMarcas(Marca marca) {
        if (marca != null) {
            labelIdMarca.setText(String.valueOf(marca.getId()));
            labelDescMarca.setText(marca.getNome());
        } else {
            labelIdMarca.setText("");
            labelDescMarca.setText("");
        }

    }

    private void carregarTableViewMarca() {
        tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("nome"));
        try{
        marcas = marcaDAO.listar();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Marca> observableListMarcas = FXCollections.observableArrayList(marcas);
        tableViewMarcas.setItems(observableListMarcas);
    }

    @FXML
    void buttonCreateMarca(ActionEvent event) throws IOException {
        Marca marca = new Marca();
        boolean buttonConfirmarClicked = showDialogCadastroMarca(marca);
        if (buttonConfirmarClicked) {
            try {
                marcaDAO.create(marca);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            carregarTableViewMarca();
        }
    }

    @FXML
    void buttonDeleteMarca(ActionEvent event) throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            try {
                marcaDAO.remover(marca);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            carregarTableViewMarca();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(" Por favor escolha uma marca na tabela! ");
            alert.showAndWait();
        }
    }

    @FXML
    void buttonUpdateMarca(ActionEvent event) throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            boolean buttonMarcafirmarClicked = showDialogCadastroMarca(marca);
            if (buttonMarcafirmarClicked) {
                try{
                marcaDAO.alterar(marca);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
                carregarTableViewMarca();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" Por favor escolha uma marca na tabela! ");
                alert.showAndWait();
            }
        }
    }

    private boolean showDialogCadastroMarca(Marca marca) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroMarca.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro marca");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogCadastroMarcaController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(marca);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}
