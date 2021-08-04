package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApp;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;


public class CustomerListScreenController implements Initializable {

    static class ClientCell extends ListCell<Cliente> {
        HBox box = new HBox();
        Label nome = new Label();
        Label cpf = new Label();
        Pane pane = new Pane();
        JFXButton delButton = new JFXButton("Excluir");
        JFXButton editButton = new JFXButton("Editar");

        public ClientCell() {
            super();
            box.getChildren().addAll(nome, cpf, pane, delButton, editButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
            delButton.setOnAction(event -> {

            });
            editButton.setOnAction(event -> {


            });
        }

        @Override
        protected void updateItem(Cliente item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                if (item.getNome().length() > 50) {
                    nome.setText(item.getNome().substring(0, 50) + "...");
                } else {
                    nome.setText(item.getNome());
                }
                cpf.setText(item.getCpf());
                setGraphic(box);
                setPrefHeight(80);
            }
        }
    }

    @FXML
    private JFXListView<Cliente> cliList;

    @FXML
    private JFXTextField searchCliTextField;

    @FXML
    private JFXButton newClientButton;

    @FXML
    private JFXButton novoClienteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateList();
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

    public void updateList(){
        if(searchCliTextField.getText().isEmpty()){
            cliList.setItems(FXCollections.observableList(FarmApp.dataManager.getCostumerManager().getClienteList()));
        }else{
            List<Cliente> filtredList = new ArrayList<>();
            for (Cliente cliente : FarmApp.dataManager.getCostumerManager().getClienteList()){
                if(cliente.getCpf().toLowerCase().contains(searchCliTextField.getText().toLowerCase())){
                    filtredList.add(cliente);

                }
            }
            cliList.setItems(FXCollections.observableList(filtredList));
        }
        cliList.setCellFactory(call -> new ClientCell());
    }
}
