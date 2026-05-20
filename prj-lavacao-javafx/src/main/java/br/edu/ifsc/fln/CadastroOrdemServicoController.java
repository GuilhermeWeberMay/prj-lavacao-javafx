package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.ItemOsDAO;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CadastroOrdemServicoController implements Initializable {

    @FXML
    private AnchorPane anchorPaneCadastroOs;

    @FXML
    private Label labelAgendaOs;

    @FXML
    private Label labelDescontoOs;

    @FXML
    private Label labelNumeroOs;

    @FXML
    private Label labelPlacaOs;

    @FXML
    private Label labelStatudOs;

    @FXML
    private Label labelTotalOs;

    @FXML
    private TableColumn<OrdemServico, LocalDate> tableColumnAgenda;

    @FXML
    private TableColumn<OrdemServico, Long> tableColumnNumero;

    @FXML
    private TableColumn<OrdemServico, EStatus> tableColumnStatus;

    @FXML
    private TableColumn<OrdemServico, Double> tableColumnTotal;

    @FXML
    private TableColumn<OrdemServico, Veiculo> tableColumnVeiculo;

    @FXML
    private TableView<OrdemServico> tableViewOs;

    private List<OrdemServico> listaOrdemServicos;
    private ObservableList<OrdemServico> observableListOrdemServicos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
    private final ItemOsDAO itemOsDAO = new ItemOsDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemServicoDAO.setConnection(connection);

        carregarTableView();

        tableViewOs.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableColumnAgenda.setCellValueFactory(new PropertyValueFactory<>("agenda"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnVeiculo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));

        listaOrdemServicos = ordemServicoDAO.listar();

        observableListOrdemServicos = FXCollections.observableArrayList(listaOrdemServicos);
        tableViewOs.setItems(observableListOrdemServicos);
    }

    public void selecionarItemTableView(OrdemServico os) {
        if (os != null) {
            labelNumeroOs.setText(Long.toString(os.getNumero()));
            labelTotalOs.setText("R$"+String.format("%.2f",os.getTotal()));
            labelAgendaOs.setText(
                    os.getAgenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            labelDescontoOs.setText((String.format("%.2f", os.getDesconto())) + "%");
            labelStatudOs.setText(os.getStatus().name());
            labelPlacaOs.setText(os.getVeiculo().getPlaca());
        } else {
            labelNumeroOs.setText("");
            labelTotalOs.setText("");
            labelAgendaOs.setText("");
            labelDescontoOs.setText("");
            labelStatudOs.setText("");
            labelPlacaOs.setText("");
        }
    }

    @FXML
    private void buttonCreateOs() throws IOException, SQLException {
        OrdemServico os = new OrdemServico();
        List<ItemOS> itemOS = new ArrayList<>();
        os.setItensOS(itemOS);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(os);
        if (buttonConfirmarClicked) {
            //O código comentado a seguir (bloco try..catch) evidencia uma má prática de programação, haja vista que o boa parte da lógica de negócio está implementada no controller
            //PROBLEMA: caso haja necessidade de levar esta aplicação para outro nível (uma aplicação web, por exemplo), todo esse código deverá ser repetido no controller, o que
            //de fato pode se tornar inconsistente caso uma nova lógica seja necessária, implicando na necessidade de rever todos os controllers das aplicações, mas, o que garante
            // que todas equipes farão isso?
            //SOLUÇÃO: levar a lógica de negócio para o OrdemServicoDAO, afinal, estamos tratando de uma venda. É ela que deve resolver o problema
//            try {
//                connection.setAutoCommit(false);
//                vendaDAO.setConnection(connection);
//                vendaDAO.inserir(venda);
//                itemDeOrdemServicoDAO.setConnection(connection);
//                produtoDAO.setConnection(connection);
//                estoqueDAO.setConnection(connection);
//                for (ItemDeOrdemServico itemDeOrdemServico: venda.getItensDeOrdemServico()) {
//                    Servico produto = itemDeOrdemServico.getServico();
//                    itemDeOrdemServico.setOrdemServico(vendaDAO.buscarUltimaOrdemServico());
//                    itemDeOrdemServicoDAO.inserir(itemDeOrdemServico);
//                    produto.getEstoque().setQuantidade(
//                            produto.getEstoque().getQuantidade() - itemDeOrdemServico.getQuantidade());
//                    estoqueDAO.atualizar(produto.getEstoque());
//                }
//                connection.commit();
//                carregarTableView();
//            } catch (SQLException exc) {
//                try {
//                    connection.rollback();
//                } catch (SQLException exc1) {
//                    Logger.getLogger(FXMLAnchorPaneProcessoOrdemServicoController.class.getName()).log(Level.SEVERE, null, exc1);
//                }
//                Logger.getLogger(FXMLAnchorPaneProcessoOrdemServicoController.class.getName()).log(Level.SEVERE, null, exc);
//            }
//        }
            ordemServicoDAO.setConnection(connection);
            ordemServicoDAO.inserir(os);
            carregarTableView();
        }
    }

    @FXML
    private void buttonUpdateOs() throws IOException {
        OrdemServico venda = tableViewOs.getSelectionModel().getSelectedItem();
        if (venda != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(venda);
            if (buttonConfirmarClicked) {
                ordemServicoDAO.alterar(venda);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um venda na Tabela.");
            alert.show();
        }
    }

    @FXML
    private void buttonDeleteOs() throws SQLException {
        OrdemServico os = tableViewOs.getSelectionModel().getSelectedItem();
        if (os != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a venda " + os.getNumero())) {
                ordemServicoDAO.setConnection(connection);
                ordemServicoDAO.remover(os);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma venda na tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneProcessoOrdemServicoDialog(OrdemServico os) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrdemServico.class.getResource(
                "/br/edu/ifsc/fln/view/DialogCadastroOrdemServico.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        DialogOrdemServico controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(os);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
}
