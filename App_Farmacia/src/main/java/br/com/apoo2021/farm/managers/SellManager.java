package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Vendas;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SellManager {

    private final List<Vendas> sellList = new ArrayList<>();

    public List<Vendas> getSellList() {
        return sellList;
    }

    public void addVenda(Vendas vendas){
        boolean founded = false;
        for(Vendas venda : sellList){
            if(venda.getNf() == vendas.getNf()){
               founded = true;
               break;
            }
        }

        if(!founded){
            sellList.add(vendas);
        }
    }

    public void updateSellList(){
        if(sellList.isEmpty()){
            generateSells();
        }
    }

    private void generateSells(){
        List<Object> notaFiscalList = SQLRunner.ExecuteSQLScript.SQLSelect("GetSellsNF");
        if(notaFiscalList != null && !notaFiscalList.isEmpty()){
            for(Object notaFiscal : notaFiscalList){
                Vendas vendas = new Vendas();
                vendas.setNf((long) notaFiscal);
                addVenda(vendas);
            }
            updateSellData();
        }
    }

    private void updateSellData(){
        if(!sellList.isEmpty()){
            for(Vendas vendas : sellList){
                Thread data = new Thread(() -> {
                    List<Object> dataList = SQLRunner.ExecuteSQLScript.SQLSelect("GetSellData", vendas.getNf());
                    if(dataList != null && !dataList.isEmpty()){
                        vendas.setData((Timestamp) dataList.get(0));
                    }else{
                        EasyFarma.logger.error("Erro ao carregar a data da venda!");
                    }
                });

                Thread crf = new Thread(() -> {
                    List<Object> crfList = SQLRunner.ExecuteSQLScript.SQLSelect("GetSellCrf", vendas.getNf());
                    if(crfList != null && !crfList.isEmpty()){
                        vendas.setCrf((String) crfList.get(0));
                    }else{
                        EasyFarma.logger.error("Erro ao carregar o crf da venda!");
                    }
                });

                Thread cpf = new Thread(() -> {
                    List<Object> cpfList = SQLRunner.ExecuteSQLScript.SQLSelect("GetSellCpf", vendas.getNf());
                    if(cpfList != null && !cpfList.isEmpty()){
                        vendas.setCpf((String) cpfList.get(0));
                    }else{
                        EasyFarma.logger.error("Erro ao carregar o cpf da venda!");
                    }
                });

                data.start();
                cpf.start();
                crf.start();

                try {
                    data.join();
                    cpf.join();
                    crf.join();
                } catch (InterruptedException e) {
                    EasyFarma.logger.error("Error ao aguardar a finalização dos threads de carregamento de vendas!", e);
                }
            }
        }
    }
}
