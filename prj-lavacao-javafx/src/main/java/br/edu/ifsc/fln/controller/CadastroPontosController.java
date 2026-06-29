package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ConfiguracoesDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Configuracoes;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class CadastroPontosController implements Initializable {

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ConfiguracoesDAO configuracoesDAO = new ConfiguracoesDAO();

    @FXML
    private AnchorPane anchorPaneCadastroServico;

    @FXML
    private Label labelPontos;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configuracoesDAO.setConnection(connection);
        try {
            Configuracoes config = configuracoesDAO.buscar();
            labelPontos.setText(String.valueOf(config.getPontos()));
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
    }

    @FXML
    void buttonUpdatePontos(ActionEvent event) throws IOException {
        Configuracoes config = new Configuracoes();
        config.setPontos(Integer.parseInt(labelPontos.getText()));
        boolean buttonServicofirmarClicked = showDialogCadastroPontos(config);
        if (buttonServicofirmarClicked) {
            try{
            configuracoesDAO.alterar(config);
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
            labelPontos.setText(String.valueOf(config.getPontos()));
        }
    }

    private boolean showDialogCadastroPontos(Configuracoes configuracoes) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroPontos.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro pontos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogCadastroPontosController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setPontos(configuracoes);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}