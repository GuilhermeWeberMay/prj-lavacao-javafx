package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
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

public class CadastroCorController implements Initializable {

    // Atributos para manipulação de BDA
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CorDAO corDAO = new CorDAO();

    @FXML
    private Label labelDescCor;

    @FXML
    private Label labelIdCor;

    @FXML
    private TableColumn<Cor, String> tableColumnCor;

    @FXML
    private TableView<Cor> tableViewCores;

    @FXML
    private AnchorPane anchorPane;

    private List<Cor> cores = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        corDAO.setConnection(connection);
        carregarTableViewCor();

        tableViewCores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCores(newValue));
    }

    private void selecionarItemTableViewCores(Cor cor) {
        if (cor != null) {
            labelIdCor.setText(String.valueOf(cor.getId()));
            labelDescCor.setText(cor.getNome());
        } else {
            labelIdCor.setText("");
            labelDescCor.setText("");
        }

    }

    private void carregarTableViewCor() {
        tableColumnCor.setCellValueFactory(new PropertyValueFactory<>("nome"));
        try{
        cores = corDAO.listar();
        }catch (DAOException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Cor> observableListCores = FXCollections.observableArrayList(cores);
        tableViewCores.setItems(observableListCores);
    }

    @FXML
    void buttonCreateCor(ActionEvent event) throws IOException {
        Cor cor = new Cor();
        boolean buttonConfirmarClicked = showDialogCadastroCor(cor);
        if (buttonConfirmarClicked) {
            try {
                corDAO.create(cor);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            carregarTableViewCor();
        }
    }

    @FXML
    void buttonDeleteCor(ActionEvent event) throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            try {
                corDAO.remover(cor);
            }catch (DAOException e) {
                throw new RuntimeException(e);
            }
            carregarTableViewCor();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(" Por favor escolha uma cor na tabela! ");
            alert.showAndWait();
        }
    }

    @FXML
    void buttonUpdateCor(ActionEvent event) throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            boolean buttonCorfirmarClicked = showDialogCadastroCor(cor);
            if (buttonCorfirmarClicked) {
                try{
                corDAO.alterar(cor);
                }catch (DAOException e) {
                    throw new RuntimeException(e);
                }
                carregarTableViewCor();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" Por favor escolha uma cor na tabela! ");
                alert.showAndWait();
            }
        }
    }

    private boolean showDialogCadastroCor(Cor cor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroCor.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro cor");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogCadastroCorController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCor(cor);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}
