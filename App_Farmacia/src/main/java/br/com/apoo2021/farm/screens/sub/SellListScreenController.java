package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.objects.Produto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SellListScreenController implements Initializable {

    @FXML
    private JFXListView<?> vendasListView;

    @FXML
    private JFXTextField nfeTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
        FarmApple.dataManager.setViewVenda(null);
        //updateList();
    }

    @FXML
    void searchChanged(KeyEvent event) {
        //updateList();
    }


}
