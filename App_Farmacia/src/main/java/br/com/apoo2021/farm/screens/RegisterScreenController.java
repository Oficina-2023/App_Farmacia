package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.util.FarmDialogs;
import br.com.apoo2021.farm.util.MD5Cripto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        registerButton.disableProperty().bind(crfTextfield.textProperty().isEmpty().or(nameTextfield.textProperty().isEmpty()
                .or(cpfTextfield.textProperty().isEmpty().or(telTextfield.textProperty().isEmpty().or(usuarioTextfield.textProperty().isEmpty()
                .or(senhaTextfield.textProperty().isEmpty().or(cosenhaTextfield.textProperty().isEmpty())))))));
    }

    @FXML
    void registerPressed(ActionEvent event) {
        try{
            List<Object> crf = SQLRunner.executeSQLScript.SQLSelect("CrfVerify",Integer.parseInt(crfTextfield.getText()));
            if(crf != null && !crf.isEmpty()){
                FarmDialogs.showDialog(registerfaPane,"Erro","Farmac\u00eautico j\u00e1 existente!");
            }else{
                if(senhaTextfield.getText().equals(cosenhaTextfield.getText())){
                    SQLRunner.executeSQLScript.SQLSet("SetFarmData",Integer.parseInt(crfTextfield.getText()),nameTextfield.getText(),
                            Long.parseLong(cpfTextfield.getText()),Long.parseLong(telTextfield.getText()),
                            MD5Cripto.MD5Converter(usuarioTextfield.getText().toLowerCase() + senhaTextfield.getText()));
                    FarmDialogs.showDialog(registerfaPane,"Registrado","Registrado com sucesso!");
                }else{
                    FarmDialogs.showDialog(registerfaPane,"Erro","As senhas s\u00e3o diferentes!");
                }
            }
        }catch(NumberFormatException e){
            FarmDialogs.showDialog(registerfaPane,"Erro","Os campos CPF,CRF e Telefone s\u00f3 aceitam n\u00fameros!");
        }
    }

    @FXML
    void returnPressed(MouseEvent event) {
        try{
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/LoginScreen.fxml")))));
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        }catch(IOException e){
            FarmApp.logger.error("Erro ao clicar em voltar, tela RegisterScreen",e);
        }

    }
}
