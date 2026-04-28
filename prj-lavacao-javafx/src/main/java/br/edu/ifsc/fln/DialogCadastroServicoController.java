package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
@Setter
public class DialogCadastroServicoController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField textFielServicoDescricao;

    @FXML
    private TextField textFielServicoValor;

    @FXML
    private ChoiceBox<ECategoria> choiceBoxCategoria;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Servico servico;

    public void setServico(Servico servico) {
        this.servico = servico;
        textFielServicoDescricao.setText(servico.getDescricao());
        textFielServicoValor.setText(String.valueOf(servico.getValor()));
        choiceBoxCategoria.setValue(servico.getCategoria());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarChoiceBoxCategoria();
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            servico.setDescricao(textFielServicoDescricao.getText());
            servico.setValor(Double.valueOf(textFielServicoValor.getText()));
            servico.setCategoria(choiceBoxCategoria.getValue());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (textFielServicoDescricao.getText() == null || textFielServicoDescricao.getText().length() == 0) {
            errorMessage += "Servico inválida!\n";
        }
        if (textFielServicoValor.getText() == null || textFielServicoValor.getText().length() == 0) {
            errorMessage += "Valor inválido!";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    public void carregarChoiceBoxCategoria() {
        choiceBoxCategoria.setItems( FXCollections.observableArrayList( ECategoria.values()));
    }
}

