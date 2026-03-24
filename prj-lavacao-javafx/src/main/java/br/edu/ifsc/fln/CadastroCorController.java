package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.domain.Cor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class CadastroCorController {


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
