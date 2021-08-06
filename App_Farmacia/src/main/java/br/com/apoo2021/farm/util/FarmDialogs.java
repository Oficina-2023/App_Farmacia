package br.com.apoo2021.farm.util;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.objects.Produto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
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
                FarmApple.dataManager.getFarmManager().clearFarmData();
            }catch(IOException e){
                FarmApple.logger.error("Error ao tentar retornar a tela de login!",e);
            }
        });
        content.setActions(yesButton, noButton);
        dialog.show();

        for(Node node : nodes){
            node.disableProperty().bind(dialog.visibleProperty());
        }
    }

    public static void showLoginError(StackPane pane, Node node){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Error"));
        content.setBody(new Text("Error ao carregar os dados.\nTente novamente mais tarde!"));
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton closeButton = new JFXButton("Fechar");
        closeButton.setOnAction(event -> {
            try{
                dialog.close();
                FarmApple.dataManager.getFarmManager().clearFarmData();
                Parent root = FXMLLoader.load(Objects.requireNonNull(FarmDialogs.class.getClassLoader().getResource("screens/LoginScreen.fxml")));
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(new Scene(root));
                ScreenAdjusts.centerScreen(stage);
                ScreenAdjusts.setDraggable(root,stage);
            }catch(IOException e){
                FarmApple.logger.error("Error ao tentar abrir a tela de login após um error de carregamento!",e);
            }
        });
        content.setActions(closeButton);
        dialog.show();
    }

    public static void showDeleteProductConfirmDialog(StackPane pane, ListView<Produto> listView, Produto produto){
        JFXDialogLayout content = new JFXDialogLayout();
        String produtoName = produto.getNome();
        if(produtoName.length() > 20){
            produtoName = produtoName.substring(0,20) + "...";
        }
        Text head = new Text("Confirma\u00e7\u00e3o de exclus\u00e3o");
        Text body = new Text("Deseja realmente deletar o produto "+ produtoName +"?");
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM);
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                SQLRunner.ExecuteSQLScript.SQLSet("ProductRemove", produto.getLote());
                listView.getItems().remove(produto);
                FarmApple.dataManager.getProductManager().removeProduto(produto.getLote());
            }catch (Exception e){
                FarmApple.logger.error("Error ao remover o produto!", e);
            }
            dialog.close();
        });
        content.setActions(yesButton, noButton);
        dialog.show();
    }

    public static void showDeleteCustomerConfirmDialog(StackPane pane, ListView<Cliente> listView, Cliente cliente){
        JFXDialogLayout content = new JFXDialogLayout();
        String clienteCpf = cliente.getCpf();
        if(clienteCpf.length() > 20){
            clienteCpf = clienteCpf.substring(0,20) + "...";
        }
        Text head = new Text("Confirma\u00e7\u00e3o de exclus\u00e3o");
        Text body = new Text("Deseja realmente deletar o Cliente de CPF : "+ clienteCpf +"?");
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM);
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                SQLRunner.ExecuteSQLScript.SQLSet("ClienteRemove", cliente.getCpf());
                listView.getItems().remove(cliente);
                FarmApple.dataManager.getCostumerManager().removeCliente(cliente.getCpf());
            }catch (Exception e){
                FarmApple.logger.error("Error ao remover o cliente!", e);
            }
            dialog.close();
        });
        content.setActions(yesButton, noButton);
        dialog.show();
    }


}
