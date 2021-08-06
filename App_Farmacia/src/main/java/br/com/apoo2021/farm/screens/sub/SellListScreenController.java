package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.FarmApple;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SellListScreenController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FarmApple.dataManager.setEditableProduct(null);
        FarmApple.dataManager.setEditableCustomer(null);
    }
}
