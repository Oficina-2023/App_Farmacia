package br.com.apoo2021.farm.screens;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
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

    }

    @FXML
    void signUpPressed(ActionEvent event) {

    }

}
