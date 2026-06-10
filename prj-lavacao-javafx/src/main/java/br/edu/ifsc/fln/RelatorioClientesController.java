/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln;


import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class RelatorioClientesController implements Initializable {

    @FXML
    private AnchorPane anchorPaneCadastroMarca;

    @FXML
    private TableColumn<?, ?> tableColumnCelular;

    @FXML
    private TableColumn<?, ?> tableColumnEmail;

    @FXML
    private TableColumn<?, ?> tableColumnNome;

    @FXML
    private TableView<Cliente> tableViewCliente;

    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableView();
    }

    private void carregarTableView() {
        //try {
            listaClientes = clienteDAO.listarClienteEstoque();
//        } catch (DAOException ex) {
//            Logger.getLogger(FXMLAnchorPaneRelatorioQuantidadeClientesController.class.getName()).log(Level.SEVERE, null, ex);
//            AlertDialog.exceptionMessage(ex);
//            return;
//        }

        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewCliente.setItems(observableListClientes);
    }

    @FXML
    public void handleImprimir() throws JRException {
        URL url = getClass().getResource("/br/edu/ifsc/fln/reports/relatorioClientes.jasper");
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(url);

        //null: caso não existam filtros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);

        //false: não deixa fechar a aplicação principal
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
    }

}