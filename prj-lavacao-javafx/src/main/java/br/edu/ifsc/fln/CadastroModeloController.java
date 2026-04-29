package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
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
import java.util.List;
import java.util.ResourceBundle;


public class CadastroModeloController implements Initializable {

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    

    @FXML
    private AnchorPane anchorPaneCadastroModelo;

    @FXML
    private Label labelMarcaModelo;

    @FXML
    private Label labelDescModelo;

    @FXML
    private Label labelIdModelo;

    @FXML
    private Label labelCategoriaModelo;

    @FXML
    private TableColumn<Modelo, String> tableColumnCategoria;

    @FXML
    private TableColumn<Modelo, String> tableColumnDescricao;

    @FXML
    private TableColumn<Marca, String> tableColumnMarca;

    @FXML
    private TableView<Modelo> tableViewModelos;

    @FXML
    void buttonCreateModelo(ActionEvent event) throws IOException {
        Modelo modelo = new Modelo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
        if (buttonConfirmarClicked) {
            modeloDAO.create(modelo);
            carregarTableView();
        }

    }

    @FXML
    void buttonDeleteModelo(ActionEvent event) {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modeloDAO.remover(modelo);
            carregarTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }

    }

    @FXML
    void buttonUpdateModelo(ActionEvent event) throws  IOException {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
            if (buttonConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);

        carregarTableView();

        tableViewModelos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tableColumnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));


        listaModelos = modeloDAO.listar();

        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableViewModelos.setItems(observableListModelos);
    }

    public void selecionarItemTableView(Modelo modelo) {
        if (modelo != null) {
            labelIdModelo.setText(Integer.toString(modelo.getId()));
            labelDescModelo.setText(modelo.getDescricao());
            labelMarcaModelo.setText(modelo.getMarca().getNome());
            labelCategoriaModelo.setText(modelo.getCategoria().getDescricao());
        } else {
            labelIdModelo.setText("");
            labelDescModelo.setText("");
            labelMarcaModelo.setText("");
            labelCategoriaModelo.setText("");
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosModelosDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/DialogCadastroModelo.fxml"));
        AnchorPane page = loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de modelos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o modelo ao controller
        DialogCadastroModeloController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}
