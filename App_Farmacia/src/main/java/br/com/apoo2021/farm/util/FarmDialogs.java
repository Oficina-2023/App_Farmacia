package br.com.apoo2021.farm.util;

import br.com.apoo2021.farm.FarmApp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FarmDialogs {

    public static void showSoftwareCloseDialog(StackPane pane, Node... nodes){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Confirma\u00e7\u00e3o de Sa\u00edda"));
        content.setBody(new Text("Deseja realmente sair?"));
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> System.exit(0));
        content.setActions(yesButton, noButton);
        dialog.show();

        for(Node node : nodes){
            node.disableProperty().bind(dialog.visibleProperty());
        }
    }

    public static void showDialog(StackPane pane,String title, String message){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton closeButton = new JFXButton("Fechar");
        closeButton.setOnAction(event -> dialog.close());
        content.setActions(closeButton);
        dialog.show();
    }

    public static void showSoftwareLogoutDialog(StackPane pane, JFXButton closeButton, Node... nodes){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Confirma\u00e7\u00e3o de Sa\u00edda"));
        content.setBody(new Text("Deseja realmente realizar logout?"));
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                Parent root = FXMLLoader.load(Objects.requireNonNull(FarmDialogs.class.getClassLoader().getResource("screens/LoginScreen.fxml")));
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                ScreenAdjusts.centerScreen(stage);
                ScreenAdjusts.setDraggable(root,stage);
                FarmApp.dataManager.getFarmManager().clearFarmData();
            }catch(IOException e){
                FarmApp.logger.error("Error ao tentar retornar a tela de login!",e);
            }
        });
        content.setActions(yesButton, noButton);
        dialog.show();

        for(Node node : nodes){
            node.disableProperty().bind(dialog.visibleProperty());
        }
    }

}
