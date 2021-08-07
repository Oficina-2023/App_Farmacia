package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.objects.Produto;
import br.com.apoo2021.farm.objects.Vendas;
import javafx.scene.layout.StackPane;

public class DataManager {

    private final FarmManager farmManager = new FarmManager();
    private final ProductManager productManager = new ProductManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final CartManager cartManager = new CartManager();
    private final SellManager sellManager = new SellManager();

    private StackPane mainPane;
    private Produto editableProduct;
    private Cliente editableCustomer;
    private Vendas viewVenda;

    public FarmManager getFarmManager() {
        return farmManager;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public CustomerManager getCostumerManager() {
        return customerManager;
    }

    public CartManager getCartManager() {
        return cartManager;
    }

    public SellManager getSellManager() {
        return sellManager;
    }

    public StackPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(StackPane mainPane) {
        this.mainPane = mainPane;
    }

    public Produto getEditableProduct() {
        return editableProduct;
    }

    public void setEditableProduct(Produto editableProduct) {
        this.editableProduct = editableProduct;
    }

    public Cliente getEditableCustomer() {
        return editableCustomer;
    }

    public void setEditableCustomer(Cliente editableCustomer) {
        this.editableCustomer = editableCustomer;
    }

    public Vendas getViewVenda() {
        return viewVenda;
    }

    public void setViewVenda(Vendas viewVenda) {
        this.viewVenda = viewVenda;
    }
}
