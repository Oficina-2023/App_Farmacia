package br.com.apoo2021.farm.managers;

public class DataManager {

    private final FarmManager farmManager = new FarmManager();
    private final ProductManager productManager = new ProductManager();

    public FarmManager getFarmManager() {
        return farmManager;
    }

    public ProductManager getProductManager() {
        return productManager;
    }
}
