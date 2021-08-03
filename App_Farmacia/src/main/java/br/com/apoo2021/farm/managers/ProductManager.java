package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Produto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private final List<Produto> produtosList = new ArrayList<>();

    public List<Produto> getProdutosList() {
        return produtosList;
    }

    public void updateProductList(){
        if(produtosList.isEmpty()){
            generateProducts();
        }
    }

    private void generateProducts(){
        List<Object> idList = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductId");
        if(idList != null && !idList.isEmpty()){
            for(Object id : idList){
                Produto produto = new Produto();
                produto.setId((long) id);
                produtosList.add(produto);
            }
            updateProductsData();
        }
    }

    private void updateProductsData(){
        if(!produtosList.isEmpty()){
            for(Produto produto : produtosList){
                List<Object> nome = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductNome", produto.getId());
                List<Object> lab = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductLab", produto.getId());
                List<Object> price = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductPrice", produto.getId());
                List<Object> validade = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductValidade", produto.getId());

                if(nome != null && !nome.isEmpty() && lab != null && !lab.isEmpty() && price != null && !price.isEmpty() && validade != null && !validade.isEmpty()){
                    produto.setNome((String) nome.get(0));
                    produto.setPreco((float) price.get(0));
                    produto.setLaboratorio((String) lab.get(0));
                    produto.setValidade((Date) validade.get(0));
                }
            }
        }
    }
}
