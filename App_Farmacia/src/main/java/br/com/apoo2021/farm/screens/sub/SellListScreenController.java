package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.EasyFarma;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

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
            setColors();
            setSizes();
            box.getChildren().addAll(data,crf,cpf,nf,pane,showAllButton);
            HBox.setHgrow(pane,Priority.ALWAYS);
            showAllButton.setOnAction(event -> {
                EasyFarma.dataManager.setViewVenda(getItem());
                try{
                    EasyFarma.dataManager.getMainPane().getChildren().clear();
                    EasyFarma.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/ProductSellListScreen.fxml"))));
                }catch (IOException e){
                    EasyFarma.logger.error("Erro ao abrir a janela ProductSellListScreen", e);
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
                crf.setText(item.getCrf().replaceFirst("(\\d{4})(\\d+)", "$1-$2"));
                cpf.setText(item.getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4"));
                nf.setText(String.valueOf(item.getNf()));

                setGraphic(box);
                setPrefHeight(80);
            }
        }

        private void setColors(){
            nf.setStyle("-fx-text-fill: white");
            data.setStyle("-fx-text-fill: white");
            crf.setStyle("-fx-text-fill: white");
            cpf.setStyle("-fx-text-fill: white");
            showAllButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
        }

        private void setSizes(){
            showAllButton.setMinWidth(120);
            showAllButton.setMinHeight(50);
            showAllButton.setFont(new Font(16));
            nf.setPrefHeight(50);
            nf.setFont(new Font(16));
            nf.setTranslateX(150);
            data.setPrefHeight(50);
            data.setFont(new Font(16));
            data.setTranslateX(0);
            crf.setPrefHeight(50);
            crf.setFont(new Font(16));
            crf.setTranslateX(200);
            cpf.setPrefHeight(50);
            cpf.setFont(new Font(16));
            cpf.setTranslateX(0);
        }
    }

    @FXML
    private JFXListView<Vendas> vendasListView;

    @FXML
    private JFXTextField nfeTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EasyFarma.dataManager.setEditableProduct(null);
        EasyFarma.dataManager.setEditableCustomer(null);
        EasyFarma.dataManager.setViewVenda(null);
        updateList();
    }

    @FXML
    void searchChanged(KeyEvent event) {
        updateList();
    }

    private void updateList(){
        if(nfeTextField.getText().isEmpty()){
            vendasListView.setItems(FXCollections.observableList(EasyFarma.dataManager.getSellManager().getSellList()));
        }else{
            List<Vendas> filtredList = new ArrayList<>();

            for(Vendas vendas : EasyFarma.dataManager.getSellManager().getSellList()){
               if(String.valueOf(vendas.getNf()).toLowerCase().contains(nfeTextField.getText().toLowerCase())){
                   filtredList.add(vendas);
               }
            }
            vendasListView.setItems(FXCollections.observableList(filtredList));
        }
        vendasListView.setCellFactory(call -> new VendaCell());
    }

}
