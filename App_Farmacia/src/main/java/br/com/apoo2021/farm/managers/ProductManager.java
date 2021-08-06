package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.Farmaple;
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

    public void removeProduto(String lote){
        produtosList.removeIf(produto -> produto.getLote().equals(lote));
    }

    public void updateProductList(){
        if(produtosList.isEmpty()){
            generateProducts();
        }
    }

    private void generateProducts(){
        List<Object> loteList = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductId");
        if(loteList != null && !loteList.isEmpty()){
            for(Object lote : loteList){
                Produto produto = new Produto();
                produto.setLote((String) lote);
                produtosList.add(produto);
            }
            updateProductsData();
        }
    }

    private void updateProductsData(){
        if(!produtosList.isEmpty()){
            for(Produto produto : produtosList){
                Thread productName = new Thread(() -> {
                    List<Object> nome = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductNome", produto.getLote());
                    if(nome != null && !nome.isEmpty()){
                        produto.setNome((String) nome.get(0));
                    }else{
                        Farmaple.logger.error("Erro ao carregar o nome do produto!");
                    }
                });

                Thread productLab = new Thread(() -> {
                    List<Object> lab = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductLab", produto.getLote());
                    if(lab != null && !lab.isEmpty()){
                        produto.setLaboratorio((String) lab.get(0));
                    }else{
                        Farmaple.logger.error("Erro ao carregar o laboratorio do produto!");
                    }
                });

                Thread productPrice = new Thread(() -> {
                    List<Object> price = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductPrice", produto.getLote());
                    if(price != null && !price.isEmpty()){
                        produto.setPreco((float) price.get(0));
                    }else{
                        Farmaple.logger.error("Erro ao carregar o preço do produto!");
                    }
                });

                Thread productValidade = new Thread(() -> {
                    List<Object> validade = SQLRunner.ExecuteSQLScript.SQLSelect("GetProductValidade", produto.getLote());
                    if(validade != null && !validade.isEmpty()){
                        produto.setValidade((Date) validade.get(0));
                    }else{
                        Farmaple.logger.error("Erro ao carregar a validade do produto!");
                    }
                });

                productName.start();
                productLab.start();
                productPrice.start();
                productValidade.start();

                try {
                    productName.join();
                    productLab.join();
                    productPrice.join();
                    productValidade.join();
                } catch (InterruptedException e) {
                    Farmaple.logger.error("Error ao aguardar a finalização dos threads de carregamento de produtos!", e);
                }
            }
        }
    }
}
