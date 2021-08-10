package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import br.com.apoo2021.farm.objects.Cliente;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;


public class CustomerListScreenController implements Initializable {

    private static class ClientCell extends ListCell<Cliente> {
        HBox box = new HBox();
        Label nome = new Label();
        Label cpf = new Label();
        Pane pane = new Pane();
        JFXButton delButton = new JFXButton("Excluir");
        JFXButton editButton = new JFXButton("Editar");

        public ClientCell() {
            super();
            setColors();
            setSizes();
            box.getChildren().addAll(nome, cpf, pane,  editButton, delButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
            delButton.setOnAction(event -> {
                FarmDialogs.showDeleteCustomerConfirmDialog(FarmApple.dataManager.getMainPane(), getListView(), getItem());
            });
            editButton.setOnAction(event -> {
                FarmApple.dataManager.setEditableCustomer(getItem());
                try{
                    FarmApple.dataManager.getMainPane().getChildren().clear();
                    FarmApple.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/CustomerEditScreen.fxml"))));


                }catch (Exception e){
                    FarmApple.logger.error("Erro ao abrir a janela CustomerEditScreen! ", e);
                    FarmApple.dataManager.setEditableProduct(null);
                }

            });
        }

        @Override
        protected void updateItem(Cliente item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                if (item.getNome().length() > 25) {
                    nome.setText(item.getNome().substring(0, 25) + "...");
                } else {
                    nome.setText(item.getNome());
                }
                cpf.setText(item.getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4"));
                setGraphic(box);
                setPrefHeight(80);
            }
        }

        private void setColors(){
            nome.setStyle("-fx-text-fill: white");
            cpf.setStyle("-fx-text-fill: white");
            delButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
            editButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
        }

        private void setSizes(){
            delButton.setMinWidth(120);
            delButton.setMinHeight(50);
            delButton.setTranslateX(5);
            editButton.setMinWidth(120);
            editButton.setMinHeight(50);
            editButton.setFont(new Font(16));
            delButton.setFont(new Font(16));
            nome.setPrefHeight(50);
            nome.setFont(new Font(16));
            nome.setTranslateX(10);
            cpf.setPrefHeight(50);
            cpf.setFont(new Font(16));
            cpf.setTranslateX(50);
        }
    }

    @FXML
    private JFXListView<Cliente> cliList;

    @FXML
    private JFXTextField searchCliTextField;

    @FXML
    private JFXButton newClientButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
        FarmApple.dataManager.setViewVenda(null);
        updateList();
    }

    @FXML
    void novoClientePressed(ActionEvent event) {
        try{
            FarmApple.dataManager.getMainPane().getChildren().clear();
            FarmApple.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/CustomerAddScreen.fxml"))));
        }catch (IOException e){
            FarmApple.logger.error("Erro ao abrir a janela CustomerAddScreen!", e);
        }
    }

    @FXML
    void searchChanged(KeyEvent event) {
        updateList();
    }

    public void updateList(){
        if(searchCliTextField.getText().isEmpty()){
            cliList.setItems(FXCollections.observableList(FarmApple.dataManager.getCostumerManager().getClienteList()));
        }else{
            List<Cliente> filtredList = new ArrayList<>();
            for (Cliente cliente : FarmApple.dataManager.getCostumerManager().getClienteList()){
                if(cliente.getCpf().toLowerCase().contains(searchCliTextField.getText().toLowerCase())){
                    filtredList.add(cliente);

                }
            }
            cliList.setItems(FXCollections.observableList(filtredList));
        }
        cliList.setCellFactory(call -> new ClientCell());
    }
}
