package br.com.apoo2021.farm.objects;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.database.SQLRunner;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final Farmaceutico farmaceutico = new Farmaceutico();

    public Farmaceutico getFarmaceutico() {
        return farmaceutico;
    }

    public void updateFarmData(){
       //List<Object> crfList = SQLRunner.executeSQLScript.SQLSelect("getFarmCRF");
        List<Object> nomeList = SQLRunner.executeSQLScript.SQLSelect("GetFarmName",farmaceutico.getCrf());
        List<Object> telefoneList = SQLRunner.executeSQLScript.SQLSelect("GetFarmFone",farmaceutico.getCrf());
        List<Object> cpfList = SQLRunner.executeSQLScript.SQLSelect("GetFarmCpf",farmaceutico.getCrf());
        if(nomeList!=null && !nomeList.isEmpty() && telefoneList!=null && !telefoneList.isEmpty() && cpfList!=null && !cpfList.isEmpty()){
            farmaceutico.setNome((String)nomeList.get(0));
            farmaceutico.setPhone((long)telefoneList.get(0));
            farmaceutico.setCpf((long)cpfList.get(0));
        }else{
            FarmApp.logger.error("ERRO - NÃO FOI POSSÍVEL CARREGAR OS DADOS DO USUÁRIO");
        }
    }
}
