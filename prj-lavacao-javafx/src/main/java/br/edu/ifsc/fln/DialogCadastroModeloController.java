package br.edu.ifsc.fln;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


@Getter
@Setter
public class DialogCadastroModeloController implements Initializable {

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ChoiceBox<ETipoCombustivel> cbTipoCombustivel;

    @FXML
    private TextField textFielModeloDesc;

    @FXML
    private TextField textFielModeloPotencia;

//    private List<Marca> listaMarcas;
//    private ObservableList<Marca> observableListMarcas;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarChoiceBoxCategoria();
        carregarChoiceBoxCombustivel();
    }

//    private void setFocusLostHandle() {
//        tfDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
//            if (!newV) { // focus lost
//                if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
//                    //System.out.println("teste focus lost");
//                    tfDescricao.requestFocus();
//                }
//            }
//        });
//    }

//This works fine too:    
//root.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//    focusState(newValue);
//});
//
//private void focusState(boolean value) {
//    if (value) {
//        System.out.println("Focus Gained");
//    }
//    else {
//        System.out.println("Focus Lost");
//    }
//} 

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;
    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    public void carregarComboBoxMarcas() {
        try{
        listaMarcas = marcaDAO.listar();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        observableListMarcas =
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }
    public void carregarChoiceBoxCategoria() {
        cbCategoria.setItems( FXCollections.observableArrayList(ECategoria.values()));
    }
    public void carregarChoiceBoxCombustivel(){
        cbTipoCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
    }

    public Stage getDialogStage() {
        return dialogStage;
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        textFielModeloDesc.setText(modelo.getDescricao());
        cbMarca.getSelectionModel().select(modelo.getMarca());
        //cbCategoria.getSelectionModel().select(modelo.getCategoria().name());
    }

    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFielModeloDesc.getText() == null || textFielModeloDesc.getText().isEmpty()) {
            errorMessage += "Descricao inválido!\n";
        }

        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    public void handleButtonCancelar(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void handleButtonConfirmar(ActionEvent actionEvent) {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(textFielModeloDesc.getText());
            modelo.setMarca(
                    cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(cbCategoria.getValue());
            modelo.setMotor(Integer.parseInt(textFielModeloPotencia.getText()), cbTipoCombustivel.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
}
