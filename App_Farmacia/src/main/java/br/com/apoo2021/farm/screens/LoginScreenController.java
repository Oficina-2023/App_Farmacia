package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.UserManager;
import br.com.apoo2021.farm.util.FarmDialogs;
import br.com.apoo2021.farm.util.MD5Cripto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
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
    void loginPressed(ActionEvent event) {
        progressBar.setVisible(true);
        setLockedData(true);
        new Thread(() -> {
            boolean logged = false;
            List<Object> crfList = SQLRunner.executeSQLScript.SQLSelect("GetFarmCRF",MD5Cripto.MD5Converter(usernameTextField.getText()),MD5Cripto.MD5Converter(passwordTextField.getText()));
            if(crfList != null && !crfList.isEmpty()){
                FarmApp.userManager.getFarmaceutico().setCrf((int)crfList.get(0));
                FarmApp.userManager.updateFarmData();
                logged = true;
            }
            boolean finalLogged = logged;
            Platform.runLater(() -> {
                progressBar.setVisible(false);
                setLockedData(false);
                if (finalLogged){
                    //Próxima tela
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
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/RegisterScreen.fxml")))));
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
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
