package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.objects.Produto;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductListScreenController implements Initializable {

    private static class ProductCell extends ListCell<Produto>{
        HBox box = new HBox();
        Label nome = new Label();
        Label price = new Label();
        Label laboratorio = new Label();
        Label validade = new Label();
        Pane pane = new Pane();
        JFXButton delButton = new JFXButton("Excluir");
        JFXButton editButton = new JFXButton("Editar");
        JFXButton addCartButton = new JFXButton("Adicionar ao Carrinho");

        public ProductCell(){
            super();
            setColors();
            setSizes();
            box.getChildren().addAll(nome, price, laboratorio, validade, pane,  addCartButton, editButton, delButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
            delButton.setOnAction(event -> {
                FarmDialogs.showDeleteProductConfirmDialog(FarmApple.dataManager.getMainPane(), getListView(), getItem());
            });

            editButton.setOnAction(event -> {
                FarmApple.dataManager.setEditableProduct(getItem());
                try{
                    FarmApple.dataManager.getMainPane().getChildren().clear();
                    FarmApple.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/ProductEditScreen.fxml"))));
                }catch (IOException e){
                    FarmApple.logger.error("Erro ao abrir a janela ProductEditScreen!", e);
                    FarmApple.dataManager.setEditableProduct(null);
                }
            });

            addCartButton.setOnAction(event -> {
                FarmDialogs.showCartAddDialog(FarmApple.dataManager.getMainPane(), getItem());
            });
        }

        @Override
        protected void updateItem(Produto item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if(item != null && !empty){
                if(item.getNome().length() > 15){
                    nome.setText(item.getNome().substring(0,15)+"...");
                }else{
                    nome.setText(item.getNome());
                }

                price.setText("R$ " + item.getPreco());

                if(item.getLaboratorio().length() > 15){
                    laboratorio.setText(item.getLaboratorio().substring(0,15)+"...");
                }else{
                    laboratorio.setText(item.getLaboratorio());
                }
                validade.setText(item.getValidade().toString());

                setGraphic(box);
                setPrefHeight(80);
            }
        }

        private void setColors(){
            nome.setStyle("-fx-text-fill: white");
            validade.setStyle("-fx-text-fill: white");
            price.setStyle("-fx-text-fill: white");
            laboratorio.setStyle("-fx-text-fill: white");
            delButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
            editButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
            addCartButton.setStyle("-fx-text-fill: white;-fx-background-color: #069e5c;-fx-background-radius: 100");
        }

        private void setSizes(){
            delButton.setMinWidth(120);
            delButton.setMinHeight(50);
            delButton.setTranslateX(5);
            delButton.setFont(new Font(16));
            editButton.setMinWidth(120);
            editButton.setMinHeight(50);
            editButton.setTranslateX(0);
            editButton.setFont(new Font(16));
            addCartButton.setMinWidth(120);
            addCartButton.setMinHeight(50);
            addCartButton.setTranslateX(-5);
            addCartButton.setFont(new Font(16));
            nome.setPrefHeight(50);
            nome.setFont(new Font(16));
            nome.setTranslateX(0);
            validade.setPrefHeight(50);
            validade.setFont(new Font(16));
            validade.setTranslateX(30);
            price.setPrefHeight(50);
            price.setFont(new Font(16));
            price.setTranslateX(10);
            laboratorio.setPrefHeight(50);
            laboratorio.setFont(new Font(16));
            laboratorio.setTranslateX(20);
        }
    }

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXListView<Produto> productList;

    @FXML
    private JFXTextField searchTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
        FarmApple.dataManager.setViewVenda(null);
        updateList();
    }

    @FXML
    void addPressed(ActionEvent event) {
        try{
            FarmApple.dataManager.getMainPane().getChildren().clear();
            FarmApple.dataManager.getMainPane().getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/ProductAddScreen.fxml"))));
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
            productList.setItems(FXCollections.observableList(FarmApple.dataManager.getProductManager().getProdutosList()));
        }else{
            List<Produto> filtredList = new ArrayList<>();

            for(Produto produto : FarmApple.dataManager.getProductManager().getProdutosList()){
                if(produto.getNome().toLowerCase().contains(searchTextField.getText().toLowerCase())){
                    filtredList.add(produto);
                }
            }

            productList.setItems(FXCollections.observableList(filtredList));
        }
        productList.setCellFactory(call -> new ProductCell());
    }

}
