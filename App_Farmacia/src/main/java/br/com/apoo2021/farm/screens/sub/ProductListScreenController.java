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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        JFXButton editButton = new JFXButton("Editar");
        JFXButton addCartButton = new JFXButton("Adicionar ao Carrinho");

        public ProductCell(){
            super();
            box.getChildren().addAll(nome, price, laboratorio, validade, pane, delButton,editButton, addCartButton);
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
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
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
