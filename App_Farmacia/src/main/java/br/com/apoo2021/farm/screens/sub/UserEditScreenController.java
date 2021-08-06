package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
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
        usernameText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getNome());
        crfText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getCrf().replaceFirst("(\\d{5})(\\d{1})(\\d+)", "$1-$2$3"));
        cpfText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4"));

        String phone = FarmApple.dataManager.getFarmManager().getFarmaceutico().getPhone();
        if(phone.length() == 11){ //Alterar para String os dados no banco
            phoneText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getPhone().replaceFirst("(\\d{2})(\\d{1})(\\d{4})(\\d+)", "($1) $2 $3-$4"));
        }else{
            phoneText.setText(FarmApple.dataManager.getFarmManager().getFarmaceutico().getPhone().replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1) $2-$3"));
        }
    }
}
