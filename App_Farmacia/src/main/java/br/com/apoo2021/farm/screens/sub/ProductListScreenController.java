package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.objects.Produto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductListScreenController implements Initializable {

    static class ProductCell extends ListCell<Produto>{
        HBox box = new HBox();
        Label nome = new Label();
        Label price = new Label();
        Label laboratorio = new Label();
        Label validade = new Label();
        Pane pane = new Pane();
        JFXButton delButton = new JFXButton("Excluir");
        JFXButton addCartButton = new JFXButton("Adicionar ao Carrinho");

        public ProductCell(){
            super();
            box.getChildren().addAll(nome, price, laboratorio, validade, pane, delButton, addCartButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
            delButton.setOnAction(event -> {

            });

            addCartButton.setOnAction(event -> {

            });
        }

        @Override
        protected void updateItem(Produto item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if(item != null && !empty){
                if(item.getNome().length() > 50){
                    nome.setText(item.getNome().substring(0,50)+"...");
                }else{
                    nome.setText(item.getNome());
                }
                price.setText("R$ " + item.getPreco());
                laboratorio.setText(item.getLaboratorio());
                validade.setText(item.getValidade().toString());

                setGraphic(box);
                setPrefHeight(80);
            }
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
        updateList();
    }

    @FXML
    void addPressed(ActionEvent event) {

    }

    private void updateList(){
        if(searchTextField.getText().isEmpty()){
            productList.setItems(FXCollections.observableList(FarmApp.dataManager.getProductManager().getProdutosList()));
        }else{
            List<Produto> filtredList = new ArrayList<>();

            for(Produto produto : FarmApp.dataManager.getProductManager().getProdutosList()){
                if(produto.getNome().toLowerCase().contains(searchTextField.getText().toLowerCase())){
                    filtredList.add(produto);
                }
            }

            productList.setItems(FXCollections.observableList(filtredList));
        }
        productList.setCellFactory(call -> new ProductCell());
    }

}
