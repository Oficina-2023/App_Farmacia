package br.com.apoo2021.farm.managers;

import javafx.scene.layout.StackPane;

public class DataManager {

    private final FarmManager farmManager = new FarmManager();
    private final ProductManager productManager = new ProductManager();
    private final CustomerManager customerManager = new CustomerManager();

    private StackPane mainPane;

    public FarmManager getFarmManager() {
        return farmManager;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public StackPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(StackPane mainPane) {
        this.mainPane = mainPane;
    }

    public CustomerManager getCostumerManager() {
        return customerManager;
    }
    
}
