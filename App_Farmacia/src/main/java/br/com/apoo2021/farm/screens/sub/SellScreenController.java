package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
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
            this.totalPrice = totalPrice;
            box.getChildren().addAll(lote, nome,quantity,price,pane,editQuantityButton,removeCartButton);
            HBox.setHgrow(pane, Priority.ALWAYS);

            editQuantityButton.setOnAction(event -> {
                FarmDialogs.showCartEditDialog(FarmApple.dataManager.getMainPane(), getListView(),getItem(), totalPrice);
            });

            removeCartButton.setOnAction(event -> {
                FarmDialogs.showCartRemoveDialog(FarmApple.dataManager.getMainPane(), getListView(), getItem(), totalPrice);
            });
        }

        @Override
        protected void updateItem(ProductCart item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if(item != null && !empty){
                if(item.getProduto().getLote().length() > 30){
                    lote.setText(item.getProduto().getLote().substring(0,30) + "...");
                }else{
                    lote.setText(item.getProduto().getLote());
                }

                if(item.getProduto().getNome().length() > 50){
                    nome.setText(item.getProduto().getNome().substring(0,50)+"...");
                }else{
                    nome.setText(item.getProduto().getNome());
                }

                quantity.setText(String.valueOf(item.getQuantity()));
                price.setText("R$ " + String.valueOf((item.getProduto().getPreco() * item.getQuantity())).replace(".", ","));

                setGraphic(box);
                setPrefHeight(80);
            }
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
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
        updateList();
        totalPriceText.setText("R$ " + String.format("%.2f", FarmApple.dataManager.getCartManager().getTotalPrice()).replace(".", ","));
    }

    @FXML
    void FinishSellPressed(ActionEvent event) {

    }

    @FXML
    void addProductPressed(ActionEvent event) {
        try{
            FarmApple.dataManager.getMainPane().getChildren().clear();
            FarmApple.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/ProductListScreen.fxml"))));
        }catch (IOException e){
            FarmApple.logger.error("Erro ao abrir a janela ProductAddScreen!", e);
        }
    }

    @FXML
    void searchChanged(KeyEvent event) {
        updateList();
    }

    private void updateList(){
        if(searchTextField.getText().isEmpty()){
            productList.setItems(FXCollections.observableList(FarmApple.dataManager.getCartManager().getSellList()));
        }else{
            List<ProductCart> filtredList = new ArrayList<>();

            for(ProductCart productCart : FarmApple.dataManager.getCartManager().getSellList()){
                if(productCart.getProduto().getNome().toLowerCase().contains(searchTextField.getText().toLowerCase())){
                    filtredList.add(productCart);
                }
            }

            productList.setItems(FXCollections.observableList(filtredList));
        }
        productList.setCellFactory(call -> new SellCell(totalPriceText));
    }
}
