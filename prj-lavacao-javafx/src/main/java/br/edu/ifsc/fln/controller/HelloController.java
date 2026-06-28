package br.edu.ifsc.fln.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelloController {

    @FXML
    private MenuItem menuItemClientes;

    @FXML
    private MenuItem menuItemCores;

    @FXML
    private MenuItem menuItemVeiculos;

    @FXML
    private MenuItem menuItemServicos;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void handleMenuItemCadastroCores() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroCor.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemCadastroMarcas() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroMarca.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemCadastroServicos() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemCadastroPontos() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroPontos.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemCadastroModelos() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroModelo.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    @FXML
    void handleMenuItemCadastroClientes() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }


    @FXML
    void handleMenuItemCadastroVeiculos() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroVeiculo.fxml"));
        anchorPane.getChildren().setAll(a);
    }


    @FXML
    void handleMenuItemProcessosOrdemServico() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/CadastroOrdemServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemGrafico() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/GraficoLavagensPorMes.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuRelatorioCliente() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/edu/ifsc/fln/view/RelatorioCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
