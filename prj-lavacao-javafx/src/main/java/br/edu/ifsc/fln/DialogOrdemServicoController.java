package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.domain.*;
import javafx.fxml.Initializable;

import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DialogOrdemServicoController implements Initializable {

    @FXML
    private Button buttonAdicionar;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private ChoiceBox<EStatus> choiceBoxStatus;

    @FXML
    private ComboBox<Servico> comboBoxServicos;

    @FXML
    private ComboBox<Veiculo> comboBoxVeiculos;

    @FXML
    private MenuItem contextMenuItemAtualizarQtd;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    @FXML
    private ContextMenu contextMenuTableView;

    @FXML
    private DatePicker datePickerAgenda;

    @FXML
    private TableColumn<ItemOS, Integer> tableColumnId;

    @FXML
    private TableColumn<ItemOS, String> tableColumnProduto;

    @FXML
    private TableColumn<ItemOS, Float> tableColumnValor;

    @FXML
    private TableView<ItemOS> tableViewItensDeVenda;

    @FXML
    private TextField textFieldDesconto;

    @FXML
    private TextField textFieldObserItemOs;

    @FXML
    private TextField textFieldValor;

    @FXML
    private TextField tfNumeroOrdemServico;


    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;
    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItensOs;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculos();

        carregarComboBoxProdutos();
        carregarChoiceBoxSituacao();
        //setFocusLostHandle();
        tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));
    }

    private void carregarComboBoxVeiculos() {
        listaVeiculos = veiculoDAO.listarSimples();
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        comboBoxVeiculos.setItems(observableListVeiculos);
    }

    private void carregarComboBoxProdutos() {
        /* carrega apenas os produtos  com estoque cuja SITUACAO está em ATIVO para operações */
        listaServicos = servicoDAO.listar();
        observableListServicos = FXCollections.observableArrayList(listaServicos);
        comboBoxServicos.setItems(observableListServicos);
    }


    public void carregarChoiceBoxSituacao() {
        choiceBoxStatus.setItems(FXCollections.observableArrayList(EStatus.values()));
        choiceBoxStatus.getSelectionModel().select(0);
    }

    private void setFocusLostHandle() {
        textFieldDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost
                if (textFieldDesconto.getText() != null && !textFieldDesconto.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    ordemServico.setDesconto(Double.parseDouble(textFieldDesconto.getText()));
                    //textFieldValor.setText(ordemServico.getTotal()/*.toString()*/);

                }
            }
        });
    }

    public void setOrdemServico(OrdemServico os) {
        this.ordemServico = os;
        if (os.getId() != 0) {
            tfNumeroOrdemServico.setText(String.valueOf(os.getNumero()));

            comboBoxVeiculos.getSelectionModel().select(this.ordemServico.getVeiculo());
            datePickerAgenda.setValue(this.ordemServico.getAgenda());

            observableListItensOs = FXCollections.observableArrayList(
                    this.ordemServico.getItensOS());
            tableViewItensDeVenda.setItems(observableListItensOs);

            textFieldValor.setText(String.format("%.2f", this.ordemServico.getTotal()));
            textFieldDesconto.setText(String.format("%.2f", this.ordemServico.getDesconto()));
            choiceBoxStatus.getSelectionModel().select(this.ordemServico.getStatus());

        }
    }

    @FXML // Adicionar do ItemOS
    public void handleButtonAdicionar() {
        Servico servico;
        ItemOS itemOS = new ItemOS();
        if (comboBoxServicos.getSelectionModel().getSelectedItem() != null) {
            //o comboBox possui dados sintetizados de Produto para evitar carga desnecessária de informação
            servico = comboBoxServicos.getSelectionModel().getSelectedItem();
            if (tableColumnProduto.getColumns() != servico) {
                // Criação do ItemOS
                itemOS.setServico(servico);
                itemOS.setObservacoes(textFieldObserItemOs.getText());
                itemOS.setOrdemServico(ordemServico);
                ordemServico.add(itemOS);

                observableListItensOs = FXCollections.observableArrayList(ordemServico.getItensOS());
                tableViewItensDeVenda.setItems(observableListItensOs);

                textFieldValor.setText(String.format("%.2f", ordemServico.getTotal()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto");
                alert.setContentText("Não existe quantidade suficiente de produtos para venda.");
                alert.show();
            }
        }
    }

    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            ordemServico.setNumero(Integer.parseInt(tfNumeroOrdemServico.getText()));
            ordemServico.setVeiculo(comboBoxVeiculos.getSelectionModel().getSelectedItem());
            ordemServico.setAgenda(datePickerAgenda.getValue());
            ordemServico.setStatus((choiceBoxStatus.getSelectionModel().getSelectedItem()));
            ordemServico.setDesconto(Double.parseDouble(textFieldDesconto.getText()));
            ordemServico.setItensOS(observableListItensOs);


            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
//        ItemDeOrdemServico itemDeOrdemServico
//                = tableViewItensDeOrdemServico.getSelectionModel().getSelectedItem();
//        if (itemDeOrdemServico == null) {
//            contextMenuItemAtualizarQtd.setDisable(true);
//            contextMenuItemRemoverItem.setDisable(true);
//        } else {
//            contextMenuItemAtualizarQtd.setDisable(false);
//            contextMenuItemRemoverItem.setDisable(false);
//        }

    }

    @FXML
    private void handleContextMenuItemAtualizarQtd() {
//        ItemDeOrdemServico itemDeOrdemServico
//                = tableViewItensDeOrdemServico.getSelectionModel().getSelectedItem();
//        int index = tableViewItensDeOrdemServico.getSelectionModel().getSelectedIndex();
//
//        int qtdAtualizada = Integer.parseInt(inputDialog(itemDeOrdemServico.getQuantidade()));
//        if (itemDeOrdemServico.getProduto().getEstoque().getQuantidade() >= qtdAtualizada) {
//            itemDeOrdemServico.setQuantidade(qtdAtualizada);
//            //venda.getItensDeOrdemServico().set(venda.getItensDeOrdemServico().indexOf(itemDeOrdemServico),itemDeOrdemServico);
//            venda.getItensDeOrdemServico().set(index, itemDeOrdemServico);
//            itemDeOrdemServico.setValor(itemDeOrdemServico.getProduto().getPreco().multiply(BigDecimal.valueOf(itemDeOrdemServico.getQuantidade())));
//            tableViewItensDeOrdemServico.refresh();
//            textFieldValor.setText(String.format("%.2f", venda.getTotal()));
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("Erro no estoque");
//            alert.setContentText("Não há quantidade suficiente de produtos para venda.");
//            alert.show();
//        }
    }

    private String inputDialog(int value) {
        TextInputDialog dialog = new TextInputDialog(Integer.toString(value));
        dialog.setTitle("Entrada de dados.");
        dialog.setHeaderText("Atualização da quantidade de produtos.");
        dialog.setContentText("Quantidade: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result.get();
    }

    @FXML
    private void handleContextMenuItemRemoverItem() {
//        ItemOS itemOS
//                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
//        int index = tableViewItensDeOrdemServico.getSelectionModel().getSelectedIndex();
//        venda.getItensDeOrdemServico().remove(index);
//        observableListItensDeOrdemServico = FXCollections.observableArrayList(venda.getItensDeOrdemServico());
//        tableViewItensDeOrdemServico.setItems(observableListItensDeOrdemServico);
//
//        textFieldValor.setText(String.format("%.2f", venda.getTotal()));
    }

    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
//        String errorMessage = "";
//
//        if (comboBoxClientes.getSelectionModel().getSelectedItem() == null) {
//            errorMessage += "Cliente inválido!\n";
//        }
//
//        if (datePickerData.getValue() == null) {
//            errorMessage += "Data inválida!\n";
//        }
//
//        if (observableListItensDeOrdemServico == null) {
//            errorMessage += "Itens de venda inválidos!\n";
//        }
//
//        DecimalFormat df = new DecimalFormat("0.00");
//        try {
//            textFieldDesconto.setText(df.parse(textFieldDesconto.getText()).toString());
//        } catch (ParseException ex) {
//            errorMessage += "A taxa de desconto está incorreta! Use \",\" como ponto decimal.\n";
//        }
//
//        if (errorMessage.length() == 0) {
//            return true;
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erro no cadastro");
//            alert.setHeaderText("Campos inválidos, por favor corrija...");
//            alert.setContentText(errorMessage);
//            alert.show();
        return true;
//        }
    }
}
