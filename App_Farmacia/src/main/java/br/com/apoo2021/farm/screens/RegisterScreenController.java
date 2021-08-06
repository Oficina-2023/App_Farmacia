package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.util.FarmDialogs;
import br.com.apoo2021.farm.util.MD5Cripto;
import br.com.apoo2021.farm.util.ScreenAdjusts;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterScreenController implements Initializable {


    @FXML
    private StackPane registerfaPane;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXTextField nameTextfield;

    @FXML
    private JFXTextField cpfTextfield;

    @FXML
    private JFXTextField crfTextfield;

    @FXML
    private JFXTextField telTextfield;

    @FXML
    private JFXTextField usuarioTextfield;

    @FXML
    private JFXPasswordField senhaTextfield;

    @FXML
    private JFXPasswordField cosenhaTextfield;

    @FXML
    private JFXButton registerButton;

    @FXML
    private ImageView returnButton;

    @FXML
    private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButton.disableProperty().bind(crfTextfield.textProperty().isEmpty().or(nameTextfield.textProperty().isEmpty()
                .or(cpfTextfield.textProperty().isEmpty().or(telTextfield.textProperty().isEmpty().or(usuarioTextfield.textProperty().isEmpty()
                .or(senhaTextfield.textProperty().isEmpty().or(cosenhaTextfield.textProperty().isEmpty())))))));
    }

    @FXML
    void registerPressed(ActionEvent event) {
        progressIndicator.setVisible(true);
        setLockedData(true);
        new Thread(() -> {
            boolean parseError = false;
            boolean lengthError = false;
            List<Object> crf = null;
            List<Object> username = null;
            try{
                crf = SQLRunner.ExecuteSQLScript.SQLSelect("CrfVerify",Integer.parseInt(crfTextfield.getText()));
                username = SQLRunner.ExecuteSQLScript.SQLSelect("UsernameVerify",MD5Cripto.MD5Converter(usuarioTextfield.getText()));
                if((crf == null || crf.isEmpty()) && (username == null || username.isEmpty())){
                    if(senhaTextfield.getText().equals(cosenhaTextfield.getText())) {
                        if(crfTextfield.getText().length() == 5 && cpfTextfield.getText().length() == 11 && (telTextfield.getText().length() == 10 || telTextfield.getText().length() == 11)) {
                            SQLRunner.ExecuteSQLScript.SQLSet("SetFarmData", Integer.parseInt(crfTextfield.getText()), nameTextfield.getText(),
                                    Long.parseLong(cpfTextfield.getText()), Long.parseLong(telTextfield.getText()),
                                    MD5Cripto.MD5Converter(usuarioTextfield.getText().toLowerCase()), MD5Cripto.MD5Converter(senhaTextfield.getText()));
                        }else{
                            lengthError = true;
                        }
                    }
                }
            }catch(NumberFormatException e){
                parseError = true;
            }

            List<Object> finalUsername = username;
            List<Object> finalCrf = crf;
            boolean finalParseError = parseError;
            boolean finalLengthError = lengthError;
            Platform.runLater(() -> {
                if(finalParseError){
                    FarmDialogs.showDialog(registerfaPane,"Erro","Os campos CPF,CRF e Telefone s\u00f3 aceitam n\u00fameros!");
                }else if(finalLengthError){
                    FarmDialogs.showDialog(registerfaPane,"Erro","Quantidade de n\u00fameros inv\u00e1lida");
                }else if(finalCrf != null && !finalCrf.isEmpty()){
                    FarmDialogs.showDialog(registerfaPane,"Erro","CRF j\u00e1 registrado!");
                }else if(finalUsername != null && !finalUsername.isEmpty()){
                    FarmDialogs.showDialog(registerfaPane,"Erro","Nome de usu\u00e1rio j\u00e1 registrado!");
                }else if(!senhaTextfield.getText().equals(cosenhaTextfield.getText())){
                    FarmDialogs.showDialog(registerfaPane,"Erro","As senhas s\u00e3o diferentes!");
                } else{
                    FarmDialogs.showDialog(registerfaPane,"Registrado","Registrado com sucesso!");
                }
                progressIndicator.setVisible(false);
                setLockedData(false);
            });
        }).start();


    }

    @FXML
    void returnPressed(MouseEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/LoginScreen.fxml")));
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            ScreenAdjusts.setDraggable(root, stage);
        }catch(IOException e){
            FarmApple.logger.error("Erro ao clicar em voltar, tela RegisterScreen",e);
        }

    }

    @FXML
    void closePressed(ActionEvent event) {
        FarmDialogs.showSoftwareCloseDialog(registerfaPane,closeButton);
    }

    private void setLockedData(boolean isProcessing){
        nameTextfield.setDisable(isProcessing);
        cpfTextfield.setDisable(isProcessing);
        crfTextfield.setDisable(isProcessing);
        telTextfield.setDisable(isProcessing);
        usuarioTextfield.setDisable(isProcessing);
        senhaTextfield.setDisable(isProcessing);
        cosenhaTextfield.setDisable(isProcessing);

        if(isProcessing){
            registerButton.disableProperty().unbind();
            registerButton.setDisable(true);
        }else{
            registerButton.setDisable(false);
            registerButton.disableProperty().bind(crfTextfield.textProperty().isEmpty().or(nameTextfield.textProperty().isEmpty()
                    .or(cpfTextfield.textProperty().isEmpty().or(telTextfield.textProperty().isEmpty().or(usuarioTextfield.textProperty().isEmpty()
                            .or(senhaTextfield.textProperty().isEmpty().or(cosenhaTextfield.textProperty().isEmpty())))))));
        }
    }
}
