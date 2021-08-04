package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApp;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private StackPane loginPane;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private ProgressIndicator progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.disableProperty().bind(usernameTextField.textProperty().isEmpty().or(passwordTextField.textProperty().isEmpty()));
    }

    @FXML
    void closePressed(ActionEvent event) {
        FarmDialogs.showSoftwareCloseDialog(loginPane,closeButton);
    }

    @FXML
    void loginPressed(ActionEvent event) {
        progressBar.setVisible(true);
        setLockedData(true);
        new Thread(() -> {
            boolean logged = false;
            List<Object> crfList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmCRF",MD5Cripto.MD5Converter(usernameTextField.getText()),MD5Cripto.MD5Converter(passwordTextField.getText()));
            if(crfList != null && !crfList.isEmpty()){
                FarmApp.dataManager.getFarmManager().getFarmaceutico().setCrf((String)crfList.get(0));
                FarmApp.dataManager.getFarmManager().updateFarmData();
                FarmApp.dataManager.getProductManager().updateProductList();
                FarmApp.dataManager.getCostumerManager().updateCostumerList();
                logged = true;
            }
            boolean finalLogged = logged;
            Platform.runLater(() -> {
                progressBar.setVisible(false);
                setLockedData(false);
                if (finalLogged){
                    try{
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/MainScreen.fxml")));
                        Stage stage = (Stage) closeButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        ScreenAdjusts.centerScreen(stage);
                        ScreenAdjusts.setDraggable(root,stage);
                    }catch(IOException e){
                        FarmApp.logger.error("Error ao tentar abrir a tela principal!",e);
                    }
                }else {
                    passwordTextField.clear();
                    FarmDialogs.showDialog(loginPane,"Erro","Falha no Login!\nVerifique se Usu\u00e1rio e Senha est\u00e3o corretos!");
                }
            });
        }).start();
    }

    @FXML
    void signUpPressed(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/RegisterScreen.fxml")));
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            ScreenAdjusts.setDraggable(root,stage);
        }catch(IOException e){
            FarmApp.logger.error("Erro ao clicar em registrar usuário, tela LoginScreen",e);
        }
    }

    private void setLockedData(boolean isProcessing){
        usernameTextField.setDisable(isProcessing);
        passwordTextField.setDisable(isProcessing);
        signUpButton.setDisable(isProcessing);

        if(isProcessing){
            loginButton.disableProperty().unbind();
            loginButton.setDisable(true);
        }else{
            loginButton.setDisable(true);
            loginButton.disableProperty().bind(usernameTextField.textProperty().isEmpty().or(passwordTextField.textProperty().isEmpty()));
        }
    }

}
