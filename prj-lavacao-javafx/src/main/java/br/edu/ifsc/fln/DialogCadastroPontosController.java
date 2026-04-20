package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.domain.Configuracoes;
import br.edu.ifsc.fln.model.domain.Servico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
@Setter
public class DialogCadastroPontosController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField textFielPontosQuantidade;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Configuracoes configuracoes;

    public void setPontos(Configuracoes configuracoes) {
        this.configuracoes = configuracoes;
        textFielPontosQuantidade.setText(String.valueOf(configuracoes.getPontos()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            configuracoes.setPontos(Integer.parseInt(textFielPontosQuantidade.getText()));

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
        if (textFielPontosQuantidade.getText() == null || textFielPontosQuantidade.getText().length() == 0) {
            errorMessage += "Servico inválida!\n";
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

}

