package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Marca;
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
public class DialogCadastroMarcaController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField textFielMarcaNome;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Marca marca;

    public void setMarca(Marca marca) {
        this.marca = marca;
        textFielMarcaNome.setText(marca.getNome());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void handleButtonConfirmar(){
        if (validarEntradaDeDados()){
        marca.setNome(textFielMarcaNome.getText());

        buttonConfirmarClicked = true;
        dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar(){
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (textFielMarcaNome.getText() == null || textFielMarcaNome.getText().length() == 0) {
            errorMessage += "Marca inválida!";
        }
        if (errorMessage.length() == 0) {
            return true;
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}

