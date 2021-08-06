package br.com.apoo2021.farm.objects;

public class ProductCart {

    private final Produto produto;
    private int quantity;

    public ProductCart(Produto produto, int quantity) {
        this.produto = produto;
        this.quantity = quantity;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
