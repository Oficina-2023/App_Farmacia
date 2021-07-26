package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.objects.Farmaceutico;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private JFXButton productButton;

    @FXML
    private JFXButton vendaButton;

    @FXML
    private JFXButton clienteButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Text usernameField;

    @FXML
    private ImageView settingsButton;

    @FXML
    private ImageView logoutButton;

    @FXML
    private StackPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUsername();
    }

    @FXML
    void configClicked(MouseEvent event) {

    }

    @FXML
    void logoutClicked(MouseEvent event) {

    }

    @FXML
    void closePressed(ActionEvent event) {
        FarmDialogs.showSoftwareCloseDialog(mainPane,closeButton);
    }

    private void updateUsername(){
        Farmaceutico farmaceutico = FarmApp.userManager.getFarmaceutico();
        if(farmaceutico.getNome().length() > 30){
            usernameField.setText(farmaceutico.getNome().substring(0,30) + "...");
        }else{
            usernameField.setText(farmaceutico.getNome());
        }
    }
}
