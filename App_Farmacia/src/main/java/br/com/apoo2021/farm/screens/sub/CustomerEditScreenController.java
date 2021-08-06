package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerEditScreenController implements Initializable {

    @FXML
    private JFXTextField ClienteNome;

    @FXML
    private JFXButton attButton;

    @FXML
    private JFXTextField ClienteCpf;

    @FXML
    private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        attButton.disableProperty().bind(ClienteNome.textProperty().isEmpty().or(ClienteCpf.textProperty().isEmpty()));

    }

    @FXML
    void AttPressed(ActionEvent event) {
        progressIndicator.setVisible(true);
        setLockedData(true);
        new Thread(()-> {
            boolean parseError = false;
            try {
                Cliente editableCliente = FarmApple.dataManager.getEditableCustomer();
                editableCliente.setNome(ClienteNome.getText());
                SQLRunner.ExecuteSQLScript.SQLSet("ClienteUpdate", editableCliente.getNome());
            }catch (Exception e) {
                FarmApple.logger.error("Erro ao editar o cliente!", e);
                parseError = true;
            }
            boolean finalParseError = parseError;
            Platform.runLater(() -> {
                if (finalParseError) {
                    FarmDialogs.showDialog(FarmApple.dataManager.getMainPane(), "Erro", "Erro ao editar o cliente.\nTente novamente mais tarde!");
                } else {
                    FarmDialogs.showDialog(FarmApple.dataManager.getMainPane(), "Sucesso", "O cliente foi editado com sucesso!");
                    updateClienteData();
                }
                progressIndicator.setVisible(false);
                setLockedData(false);
            });
        }).start();

    }

    private void updateClienteData(){
        Cliente editableCLiente = FarmApple.dataManager.getEditableCustomer();

        ClienteNome.setText(editableCLiente.getNome());
    }

    private void setLockedData(boolean isProcessing){
        ClienteNome.setDisable(isProcessing);
        if(isProcessing){
            attButton.disableProperty().unbind();
            attButton.setDisable(true);
        }else{
            attButton.setDisable(false);
            attButton.disableProperty().bind(ClienteNome.textProperty().isEmpty().or(ClienteCpf.textProperty().isEmpty()));
        }
    }

}


