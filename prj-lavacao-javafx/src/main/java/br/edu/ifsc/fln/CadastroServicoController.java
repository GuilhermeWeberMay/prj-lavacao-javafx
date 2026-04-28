package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.ConfiguracoesDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Configuracoes;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CadastroServicoController implements Initializable {

    // Atributos para manipulação de BDA
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final ConfiguracoesDAO configuracoesDAO = new ConfiguracoesDAO();
    private int pontosGlobais;

    @FXML
    private AnchorPane anchorPaneCadastroServico;

    @FXML
    private Label labelDescServico;

    @FXML
    private Label labelIdServico;

    @FXML
    private Label labelPontosServico;

    @FXML
    private Label labelValorServico;

    @FXML
    private TableColumn<Servico, String> tableColumnDescricao;

    @FXML
    private TableColumn<Servico, Double> tableColumnValor;

    @FXML
    private TableColumn<Servico, ECategoria> tableColumnCategoria;

    @FXML
    private TableView<Servico> tableViewServicos;

    @FXML
    private AnchorPane anchorPane;

    private List<Servico> servicos = new ArrayList<>();

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        servicoDAO.setConnection(connection);
        configuracoesDAO.setConnection(connection);

        carregarTableViewServico();
        configurarTableView();

        configurarTableView();
        carregarTableViewServico();
        tableViewServicos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> selecionarItemTableViewServicos(newValue)
        );
    }

    private void selecionarItemTableViewServicos(Servico servico) {
        if (servico != null) {
            labelIdServico.setText(String.valueOf(servico.getId()));
            labelDescServico.setText(servico.getDescricao());
            labelValorServico.setText(String.valueOf(servico.getValor()));
        } else {
            labelIdServico.setText("");
            labelDescServico.setText("");
            labelValorServico.setText("");
        }

    }

    private void configurarTableView() {
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    }

    private void carregarTableViewServico() {
        try {
            servicoDAO.setConnection(connection);
            List<Servico> servicos = servicoDAO.listar();
            tableViewServicos.setItems(FXCollections.observableArrayList(servicos));

            configuracoesDAO.setConnection(connection);
            Configuracoes configuracao = configuracoesDAO.buscar();

            if (configuracao != null) {
                pontosGlobais = configuracao.getPontos();
                labelPontosServico.setText(String.valueOf(pontosGlobais));
            } else {
                labelPontosServico.setText("0");
                pontosGlobais = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            labelPontosServico.setText("0");
            pontosGlobais = 0;
        }
    }

    @FXML
    void buttonCreateServico(ActionEvent event) throws IOException {
        Servico servico = new Servico();
        boolean buttonConfirmarClicked = showDialogCadastroServico(servico);
        if (buttonConfirmarClicked) {
            servicoDAO.create(servico);
            carregarTableViewServico();
        }
    }

    @FXML
    void buttonDeleteServico(ActionEvent event) throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servico != null) {
            servicoDAO.remover(servico);
            carregarTableViewServico();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(" Por favor escolha uma servico na tabela! ");
            alert.showAndWait();
        }
    }

    @FXML
    void buttonUpdateServico(ActionEvent event) throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        System.out.println(servico);
        if (servico != null) {
            boolean buttonServicofirmarClicked = showDialogCadastroServico(servico);
            if (buttonServicofirmarClicked) {
                servicoDAO.alterar(servico);
                carregarTableViewServico();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" Por favor escolha uma servico na tabela! ");
                alert.showAndWait();
            }
        }
    }

    private boolean showDialogCadastroServico(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroServico.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro servico");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogCadastroServicoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
}
