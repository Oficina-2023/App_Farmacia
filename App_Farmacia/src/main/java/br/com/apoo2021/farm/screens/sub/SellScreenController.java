package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellScreenController implements Initializable {

    private static class SellCell extends ListCell<ProductCart> {
        HBox box = new HBox();
        Label lote = new Label();
        Label nome = new Label();
        Label quantity = new Label();
        Label price = new Label();
        Text totalPrice;
        Pane pane = new Pane();
        JFXButton editQuantityButton = new JFXButton("Alterar Quantidade");
        JFXButton removeCartButton = new JFXButton("Remover do Carrinho");

        public SellCell(Text totalPrice) {
            super();
            setColors();
            setSizes();
            this.totalPrice = totalPrice;
            box.getChildren().addAll(lote, nome,quantity,price,pane,editQuantityButton,removeCartButton);
            HBox.setHgrow(pane, Priority.ALWAYS);

            editQuantityButton.setOnAction(event -> {
                FarmDialogs.showCartEditDialog(EasyFarma.dataManager.getMainPane(), getListView(),getItem(), totalPrice);
            });

            removeCartButton.setOnAction(event -> {
                FarmDialogs.showCartRemoveDialog(EasyFarma.dataManager.getMainPane(), getListView(), getItem(), totalPrice);
            });
        }

        @Override
        protected void updateItem(ProductCart item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if(item != null && !empty){
                if(item.getProduto().getLote().length() > 15){
                    lote.setText(item.getProduto().getLote().substring(0,15) + "...");
                }else{
                    lote.setText(item.getProduto().getLote());
                }

                if(item.getProduto().getNome().length() > 25){
                    nome.setText(item.getProduto().getNome().substring(0,25)+"...");
                }else{
                    nome.setText(item.getProduto().getNome());
                }

                quantity.setText(String.valueOf(item.getQuantity()));
                price.setText("R$ " + String.format("%.2f", item.getProduto().getPreco() * item.getQuantity()).replace(".", ","));
                //price.setText("R$ " + String.valueOf((item.getProduto().getPreco() * item.getQuantity())).replace(".", ","));

                setGraphic(box);
                setPrefHeight(80);
            }
        }

        private void setColors(){
            nome.setStyle("-fx-text-fill: white");
            lote.setStyle("-fx-text-fill: white");
            price.setStyle("-fx-text-fill: white");
            quantity.setStyle("-fx-text-fill: white");
            editQuantityButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
            removeCartButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
        }

        private void setSizes(){
            editQuantityButton.setMinWidth(120);
            editQuantityButton.setMinHeight(50);
            editQuantityButton.setTranslateX(-5);
            editQuantityButton.setFont(new Font(16));
            removeCartButton.setMinWidth(120);
            removeCartButton.setMinHeight(50);
            removeCartButton.setTranslateX(5);
            removeCartButton.setFont(new Font(16));
            nome.setPrefHeight(50);
            nome.setFont(new Font(16));
            nome.setTranslateX(30);
            lote.setPrefHeight(50);
            lote.setFont(new Font(16));
            lote.setTranslateX(0);
            price.setPrefHeight(50);
            price.setFont(new Font(16));
            price.setTranslateX(80);
            quantity.setPrefHeight(50);
            quantity.setFont(new Font(16));
            quantity.setTranslateX(50);
        }
    }

    @FXML
    private JFXListView<ProductCart> productList;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private JFXButton finishSellButton;

    @FXML
    private JFXButton addProductButton;

    @FXML
    private Text totalPriceText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EasyFarma.dataManager.setEditableProduct(null);
        EasyFarma.dataManager.setEditableCustomer(null);
        EasyFarma.dataManager.setViewVenda(null);
        updateList();
        totalPriceText.setText("R$ " + String.format("%.2f", EasyFarma.dataManager.getCartManager().getTotalPrice()).replace(".", ","));
        finishSellButton.disableProperty().bind(Bindings.isEmpty(productList.getItems()));
    }

    @FXML
    void FinishSellPressed(ActionEvent event) {
        FarmDialogs.showFinishSellDialog(EasyFarma.dataManager.getMainPane(), productList, totalPriceText);
    }

    @FXML
    void addProductPressed(ActionEvent event) {
        try{
            EasyFarma.dataManager.getMainPane().getChildren().clear();
            EasyFarma.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/ProductListScreen.fxml"))));
        }catch (IOException e){
            EasyFarma.logger.error("Erro ao abrir a janela ProductAddScreen!", e);
        }
    }

    @FXML
    void searchChanged(KeyEvent event) {
        updateList();
    }

    private void updateList(){
        if(searchTextField.getText().isEmpty()){
            productList.setItems(FXCollections.observableList(EasyFarma.dataManager.getCartManager().getSellList()));
        }else{
            List<ProductCart> filtredList = new ArrayList<>();

            for(ProductCart productCart : EasyFarma.dataManager.getCartManager().getSellList()){
                if(productCart.getProduto().getNome().toLowerCase().contains(searchTextField.getText().toLowerCase())){
                    filtredList.add(productCart);
                }
            }

            productList.setItems(FXCollections.observableList(filtredList));
        }
        productList.setCellFactory(call -> new SellCell(totalPriceText));
    }
}
