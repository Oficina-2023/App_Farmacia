package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.util.FarmDialogs;
import br.com.apoo2021.farm.util.MD5Cripto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserEditScreenController implements Initializable {

    @FXML
    private JFXPasswordField oldPass;

    @FXML
    private JFXPasswordField newPass;

    @FXML
    private JFXPasswordField ConfirmNewPass;

    @FXML
    private JFXButton updateButton;

    @FXML
    private Text usernameText;

    @FXML
    private Text crfText;

    @FXML
    private Text cpfText;

    @FXML
    private Text phoneText;

    @FXML
    private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateButton.disableProperty().bind(oldPass.textProperty().isEmpty().or(newPass.textProperty().isEmpty().or(ConfirmNewPass.textProperty().isEmpty())));
        updateScreenInfo();
    }

    @FXML
    void updatePressed(ActionEvent event) {
        progressIndicator.setVisible(true);
        updateButton.disableProperty().unbind();
        updateButton.setDisable(true);
        new Thread(() -> {
            boolean success = false;
            boolean notEqual = false;
            List<Object> crfList = null;
            if(!newPass.getText().isEmpty() && !oldPass.getText().isEmpty() && !ConfirmNewPass.getText().isEmpty()) {
                crfList = SQLRunner.ExecuteSQLScript.SQLSelect("VerifyPass", FarmApple.dataManager.getFarmManager().getFarmaceutico().getCrf(), Objects.requireNonNull(MD5Cripto.MD5Converter(oldPass.getText())).toLowerCase());
                if (crfList != null && !crfList.isEmpty()) {
                    if (newPass.getText().equals(ConfirmNewPass.getText())) {
                        SQLRunner.ExecuteSQLScript.SQLSet("UpdatePass", Objects.requireNonNull(MD5Cripto.MD5Converter(newPass.getText())).toLowerCase(), FarmApple.dataManager.getFarmManager().getFarmaceutico().getCrf());
                        success = true;
                    }else{
                        notEqual = true;
                    }
                }
            }

            boolean finalSuccess = success;
            boolean finalNotEqual = notEqual;
            List<Object> finalCrfList = crfList;
            Platform.runLater(() -> {
                if(finalSuccess){
                    FarmDialogs.showDialog(FarmApple.dataManager.getMainPane(), "Senha alterada com sucesso!", "A senha foi alterada com sucesso!");
                }else {
                    if (finalCrfList == null || finalCrfList.isEmpty()) {
                        FarmDialogs.showDialog(FarmApple.dataManager.getMainPane(), "Erro ao alterar a senha!", "Senha incorreta!");
                    } else if (finalNotEqual) {
                        FarmDialogs.showDialog(FarmApple.dataManager.getMainPane(), "Erro ao alterar a senha!", "As senhas n\u00e3o batem");
                    }
                }

                progressIndicator.setVisible(false);
                updateButton.setDisable(false);
                updateButton.disableProperty().bind(oldPass.textProperty().isEmpty().or(newPass.textProperty().isEmpty().or(ConfirmNewPass.textProperty().isEmpty())));
            });
        }).start();
    }

    private void updateScreenInfo(){
        usernameText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getNome());
        cpfText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4"));
        crfText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getCrf().replaceFirst("(\\d{4})(\\d+)", "$1-$2"));

        String phone = FarmApple.dataManager.getFarmManager().getFarmaceutico().getPhone();
        if(phone.length() == 11){ //Alterar para String os dados no banco
            phoneText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getPhone().replaceFirst("(\\d{2})(\\d)(\\d{4})(\\d+)", "($1) $2 $3-$4"));
        }else{
            phoneText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getPhone().replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1) $2-$3"));
        }
    }
}
