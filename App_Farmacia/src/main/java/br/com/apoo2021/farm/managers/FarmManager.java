package br.com.apoo2021.farm.managers;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Farmaceutico;

import java.util.List;

public class FarmManager {

    private final Farmaceutico farmaceutico = new Farmaceutico();

    public Farmaceutico getFarmaceutico() {
        return farmaceutico;
    }

    public void updateFarmData(){
        List<Object> nomeList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmName",farmaceutico.getCrf());
        List<Object> telefoneList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmTelefone",farmaceutico.getCrf());
        List<Object> cpfList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmCpf",farmaceutico.getCrf());
        if(nomeList!=null && !nomeList.isEmpty() && telefoneList!=null && !telefoneList.isEmpty() && cpfList!=null && !cpfList.isEmpty()){
            farmaceutico.setNome((String)nomeList.get(0));
            farmaceutico.setPhone((String)telefoneList.get(0));
            farmaceutico.setCpf((String)cpfList.get(0));
        }else{
            FarmApp.logger.error("Erro ao carregar os dados do usuario!");
        }
    }

    public void clearFarmData(){
        farmaceutico.setCrf(null);
        farmaceutico.setNome(null);
        farmaceutico.setPhone(null);
        farmaceutico.setCpf(null);
    }
}
