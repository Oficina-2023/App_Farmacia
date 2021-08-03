package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.objects.Farmaceutico;
import br.com.apoo2021.farm.util.FarmDialogs;
import br.com.apoo2021.farm.util.ScreenAdjusts;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerListScreenController implements Initializable {

    @FXML
    private JFXButton novoClienteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void novoClientePressed(ActionEvent event) {
        try{
            FarmApp.dataManager.getMainPane().getChildren().clear();
            FarmApp.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/CustomerAddScreen.fxml"))));
        }catch (IOException e){
            FarmApp.logger.error("Erro ao abrir a janela CustomerAddScreen!", e);
        }
    }
}
