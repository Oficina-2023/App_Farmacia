package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.objects.Produto;

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


    public void generateCustomerList(){
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

    public void updateCostumerData(){
        if(!clienteList.isEmpty()){
            for(Cliente cliente : clienteList){
                List<Object> nome = SQLRunner.ExecuteSQLScript.SQLSelect("GetClienteNome",cliente.getCpf());

                if (nome != null && !nome.isEmpty()){
                    cliente.setNome((String) nome.get(0));
                }
            }
        }

    }
}
