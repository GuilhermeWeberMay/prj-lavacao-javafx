package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class DialogCadastroClienteController {
    @Getter
    @Setter
    private Stage dialogStage;
    @Getter
    private Cliente cliente;
    @Getter
    private boolean btConfirmarClicked = false;

    @FXML
    private DatePicker dpClienteDataCadastro;

    @FXML
    private DatePicker dpClienteDataNascimento;

    @FXML
    private TextField textFielClienteCelular;

    @FXML
    private TextField textFielClienteCpf;

    @FXML
    private TextField textFielClienteEmail;

    @FXML
    private TextField textFielNomeCliente;

    @FXML
    private TextField textFielClienteCnpj;

    @FXML
    private TextField textFielClienteInscEstadual;

    @FXML
    private RadioButton rbPessoaFisica;

    @FXML
    private RadioButton rbPessoaJuridica;

    @FXML
    void handleButtonCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(textFielNomeCliente.getText());
            cliente.setEmail(textFielClienteEmail.getText());
            cliente.setCelular(textFielClienteCelular.getText());
            cliente.setDataCadastro(dpClienteDataCadastro.getValue());
            if (cliente instanceof PessoaFisica) {
                ((PessoaFisica) cliente).setCpf(textFielClienteCpf.getText());
                ((PessoaFisica) cliente).setDataNascimento(dpClienteDataCadastro.getValue());
            } else {
                ((PessoaJuridica) cliente).setCnpj(textFielClienteCnpj.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(textFielClienteInscEstadual.getText());
            }
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleRbPessoaFisica() {
        this.textFielClienteCnpj.setDisable(true);
        this.textFielClienteInscEstadual.setDisable(true);
//        this.textFielClienteCpf.setDisable(true);
//        this.dpClienteDataNascimento.setDisable(true);
    }

    @FXML
    public void handleRbPessoaJuridica() {
//        this.textFielClienteCnpj.setDisable(true);
//        this.textFielClienteInscEstadual.setDisable(true);
        this.textFielClienteCpf.setDisable(true);
        this.dpClienteDataNascimento.setDisable(true);
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente.getId() != 0) {
            this.textFielNomeCliente.setText(this.cliente.getNome());
            this.textFielClienteCelular.setText(this.cliente.getCelular());
            this.textFielClienteEmail.setText(this.cliente.getEmail());
            this.dpClienteDataCadastro.setValue(this.cliente.getDataCadastro());
            //this.dpClienteDataCadastro.setDisable(true);
        }
        if (cliente instanceof PessoaFisica) {
            handleRbPessoaFisica();
            textFielClienteCpf.setText(((PessoaFisica) this.cliente).getCpf());
            dpClienteDataNascimento.setValue(((PessoaFisica) this.cliente).getDataNascimento());
        } else {
            handleRbPessoaJuridica();
            textFielClienteCnpj.setText(((PessoaJuridica) this.cliente).getCnpj());
            textFielClienteInscEstadual.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
        }
        this.textFielNomeCliente.requestFocus();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.textFielNomeCliente.getText() == null || this.textFielNomeCliente.getText().isEmpty()) {
            errorMessage += "Nome inválido.\n";
        }

        if (this.textFielClienteCelular.getText() == null || this.textFielClienteCelular.getText().isEmpty()) {
            errorMessage += "Telefone inválido.\n";
        }

        if (this.textFielClienteEmail.getText() == null || this.textFielClienteEmail.getText().isEmpty() /*|| !this.textFielClienteEmail.getText().contains("@")*/) {
            errorMessage += "Email inválido.\n";
        }

        if (this.dpClienteDataCadastro.getValue() == null /*|| !this.textFielClienteEmail.getText().contains("@")*/) {
            errorMessage += "Data inválida.\n";
        }

        if (cliente instanceof PessoaFisica) {
            if (this.textFielClienteCpf.getText() == null || this.textFielClienteCpf.getText().isEmpty()) {
                errorMessage += "CPF inválido.\n";
            }
        } else {
            if (this.textFielClienteCnpj.getText() == null || this.textFielClienteCnpj.getText().length() == 0) {
                errorMessage += "CNPJ inválido.\n";
            }
            if (this.textFielClienteInscEstadual.getText() == null || this.textFielClienteInscEstadual.getText().length() == 0) {
                errorMessage += "Inscrição estadual inválida.\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
