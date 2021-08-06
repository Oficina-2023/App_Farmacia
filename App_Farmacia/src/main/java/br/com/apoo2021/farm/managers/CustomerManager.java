package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.Farmaple;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    private final List<Cliente> clienteList = new ArrayList<>();

    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void updateCostumerList(){
        if(clienteList.isEmpty()){
            generateCustomerList();
        }
    }


    private void generateCustomerList(){
        List<Object>cpfList = SQLRunner.ExecuteSQLScript.SQLSelect("GetClienteCpf");
        if(cpfList != null && !cpfList.isEmpty()){
            for(Object cpf : cpfList){
                Cliente cliente = new Cliente();
                cliente.setCpf((String)cpf);
                clienteList.add(cliente);
            }
            updateCostumerData();
        }
    }

    private void updateCostumerData(){
        if(!clienteList.isEmpty()){
            for(Cliente cliente : clienteList){
                Thread customerName = new Thread(() -> {
                    List<Object> nome = SQLRunner.ExecuteSQLScript.SQLSelect("GetClienteNome",cliente.getCpf());

                    if (nome != null && !nome.isEmpty()){
                        cliente.setNome((String) nome.get(0));
                    }
                });
                customerName.start();

                try {
                    customerName.join();

                }catch (InterruptedException e){
                    Farmaple.logger.error("Error ao aguardar a finalização dos threads de carregamento de clientes!", e);
                }
            }
        }
    }
}
