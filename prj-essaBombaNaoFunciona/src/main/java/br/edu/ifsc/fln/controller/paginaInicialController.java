package br.edu.ifsc.fln.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class paginaInicialController {
    @FXML
    private MenuItem menuItemClientes;

    @FXML
    private MenuItem menuItemCores;

    @FXML
    private MenuItem menuItemVeiculos;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void handleMenuItemCadastroCategoria() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/CadastroCor.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
