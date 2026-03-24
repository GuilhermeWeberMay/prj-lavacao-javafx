package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
    }

    private void carregarTableViewCor() {
        tableColumnCor.setCellValueFactory(new PropertyValueFactory<>("Cor"));

        cores = corDAO.listar();

        ObservableList<Cor> observableListCores = FXCollections.observableArrayList(cores);
        tableViewCores.setItems(observableListCores);
    }

    @FXML
    void buttonCreateCor(ActionEvent event) {

    }

    @FXML
    void buttonDeleteCor(ActionEvent event) {

    }

    @FXML
    void buttonUpdateCor(ActionEvent event) {

    }
}
