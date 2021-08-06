package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.objects.Produto;

import java.util.ArrayList;
import java.util.List;

public class SellManager {

    private final List<ProductCart> sellList = new ArrayList<>();

    private float totalPrice = 0;

    public List<ProductCart> getSellList() {
        return sellList;
    }

    public void addProductToCart(Produto produto, int quantity){
        ProductCart productCart = new ProductCart(produto,quantity);
        sellList.add(productCart);
        updateProductPrice();
    }

    public void editProductQuantity(Produto produto, int quantity){
        for(ProductCart productCart : sellList){
            if(productCart.getProduto().getLote().equals(produto.getLote())){
                productCart.setQuantity(quantity);
                updateProductPrice();
            }
        }
    }

    public void removeProductOfCart(Produto produto){
        for(ProductCart productCart : sellList){
            if(productCart.getProduto().getLote().equals(produto.getLote())){
                sellList.remove(productCart);
                updateProductPrice();
            }
        }
    }

    private void updateProductPrice(){
        totalPrice = 0;
        for(ProductCart product : sellList){
            totalPrice += (product.getProduto().getPreco() * product.getQuantity());
        }
    }

    public void clearSellList(){
        sellList.clear();
        totalPrice = 0;
    }

    public float getTotalPrice() {
        return totalPrice;
    }
}
