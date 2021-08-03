package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateScreenInfo();
    }

    @FXML
    void updatePressed(ActionEvent event) {

    }

    private void updateScreenInfo(){
        usernameText.setText(FarmApp.userManager.getFarmaceutico().getNome());
        crfText.setText(FarmApp.userManager.getFarmaceutico().getCrf().replaceFirst("(\\d{5})(\\d{1})(\\d+)", "$1-$2$3"));
        cpfText.setText(FarmApp.userManager.getFarmaceutico().getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4"));

        String phone = FarmApp.userManager.getFarmaceutico().getPhone();
        if(phone.length() == 11){ //Alterar para String os dados no banco
            phoneText.setText(FarmApp.userManager.getFarmaceutico().getPhone().replaceFirst("(\\d{2})(\\d{1})(\\d{4})(\\d+)", "($1) $2 $3-$4"));
        }else{
            phoneText.setText(FarmApp.userManager.getFarmaceutico().getPhone().replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1) $2-$3"));
        }
    }
}
