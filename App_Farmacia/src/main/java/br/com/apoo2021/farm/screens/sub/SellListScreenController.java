package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.objects.Produto;
import br.com.apoo2021.farm.objects.Vendas;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SellListScreenController implements Initializable {

    private static class VendaCell extends ListCell<Vendas>{
        HBox box = new HBox();
        Label nf = new Label();
        Label data = new Label();
        Label crf = new Label();
        Label cpf = new Label();
        Pane pane = new Pane();
        JFXButton showAllButton = new JFXButton("Ver mais");

        public VendaCell() {
            super();
            box.getChildren().addAll(data,crf,cpf,nf,pane,showAllButton);
            HBox.setHgrow(pane,Priority.ALWAYS);
            showAllButton.setOnAction(event -> {
                FarmApple.dataManager.setViewVenda(getItem());
                try{
                    FarmApple.dataManager.getMainPane().getChildren().clear();
                    FarmApple.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/ProductSellListScreen.fxml"))));
                }catch (IOException e){
                    FarmApple.logger.error("Erro ao abrir a janela ProductSellListScreen", e);
                }
            });
        }

        @Override
        protected void updateItem(Vendas item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if(item != null && !empty){
                data.setText(item.getData().toString());
                crf.setText(item.getCrf());
                cpf.setText(item.getCpf());
                nf.setText(String.valueOf(item.getNf()));

                setGraphic(box);
                setPrefHeight(80);
            }
        }
    }

    @FXML
    private JFXListView<Vendas> vendasListView;

    @FXML
    private JFXTextField nfeTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
        FarmApple.dataManager.setViewVenda(null);
        updateList();
    }

    @FXML
    void searchChanged(KeyEvent event) {
        updateList();
    }

    private void updateList(){
        if(nfeTextField.getText().isEmpty()){
            vendasListView.setItems(FXCollections.observableList(FarmApple.dataManager.getSellManager().getSellList()));
        }else{
            List<Vendas> filtredList = new ArrayList<>();

            for(Vendas vendas : FarmApple.dataManager.getSellManager().getSellList()){
               if(String.valueOf(vendas.getNf()).toLowerCase().contains(nfeTextField.getText().toLowerCase())){
                   filtredList.add(vendas);
               }
            }
            vendasListView.setItems(FXCollections.observableList(filtredList));
        }
        vendasListView.setCellFactory(call -> new VendaCell());
    }

}
