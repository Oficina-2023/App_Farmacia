package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.util.MD5Cripto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton signUpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void loginPressed(ActionEvent event) {
        String hash = MD5Cripto.MD5Converter(usernameTextField.getText()+passwordTextField.getText());
        if (hash != null) {
            List<Object> crf = SQLRunner.executeSQLScript.SQLSelect("GetFarmCRF", hash);
            if(crf != null && !crf.isEmpty()) {
                List<Object> name = SQLRunner.executeSQLScript.SQLSelect("GetFarmName", hash);
                if (name != null && !name.isEmpty()) {
                    FarmApp.userManager.setFarmData((int) crf.get(0), (String) name.get(0));
                }
            } else {
                // login/senha não encontrados
                System.out.println("?");
            }
        }
    }

    @FXML
    void signUpPressed(ActionEvent event) {

    }

}
