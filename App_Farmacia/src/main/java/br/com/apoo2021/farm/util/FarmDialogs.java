package br.com.apoo2021.farm.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class FarmDialogs {

    public static void showSoftwareCloseDialog(StackPane pane, JFXButton closeButton){
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

        closeButton.disableProperty().bind(dialog.visibleProperty());
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

}
