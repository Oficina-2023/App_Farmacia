package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerAddScreenController implements Initializable {

    @FXML
    private JFXTextField nomeTextfield;

    @FXML
    private JFXTextField cpfTextfield;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXSpinner progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.disableProperty().bind(nomeTextfield.textProperty().isEmpty().or(cpfTextfield.textProperty().isEmpty()));
    }

    @FXML
    void addPressed(ActionEvent event){
        progressIndicator.setVisible(true);
        setLockedData(true);

        new Thread(() -> {

            boolean parseError = false;
            boolean lengthError = false;

            List<Object> cpf = null;

            try{
                if(cpfTextfield.getText().length() == 11) {
                    cpf = SQLRunner.ExecuteSQLScript.SQLSelect("ClienteCpfVerify", Long.parseLong(cpfTextfield.getText()));
                    if(cpf == null || cpf.isEmpty()) {
                        Cliente cliente = new Cliente();
                        cliente.setNome(nomeTextfield.getText());
                        cliente.setCpf(cpfTextfield.getText());
                        SQLRunner.ExecuteSQLScript.SQLSet("SetFarmCliente", nomeTextfield.getText(), Long.parseLong(cpfTextfield.getText()));
                        EasyFarma.dataManager.getCostumerManager().getClienteList().add(cliente);
                    }
                }else{
                    lengthError = true;
                }

            }catch(NumberFormatException e){
                parseError = true;
            }

            boolean finalLengthError = lengthError;
            boolean finalParseError = parseError;
            List<Object> finalCpf = cpf;
            Platform.runLater(() -> {
                if(finalParseError){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Erro","O campo CPF s\u00f3 aceita n\u00fameros!");
                }else if(finalLengthError){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Erro","Insira um CPF valido!");
                }else if(finalCpf != null && !finalCpf.isEmpty()){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Erro","CPF de cliente j\u00e1 registrado!");
                }else{
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Registrado","Registrado com sucesso!");
                }
                progressIndicator.setVisible(false);
                setLockedData(false);
            });
        }).start();
    }

    private void setLockedData(boolean isProcessing){
        nomeTextfield.setDisable(isProcessing);
        cpfTextfield.setDisable(isProcessing);

        if(isProcessing){
            addButton.disableProperty().unbind();
            addButton.setDisable(true);
        }else{
            addButton.setDisable(false);
            addButton.disableProperty().bind(cpfTextfield.textProperty().isEmpty().or(nomeTextfield.textProperty().isEmpty()));
        }
    }
}
