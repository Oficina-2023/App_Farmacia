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
        Thread farmName = new Thread(() -> {
            List<Object> nomeList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmName",farmaceutico.getCrf());
            if(nomeList != null && !nomeList.isEmpty()){
                farmaceutico.setNome((String)nomeList.get(0));
            }else{
                FarmApp.logger.error("Erro ao carregar o nome do farmaceutico!");
            }
        });

        Thread farmPhone = new Thread(() -> {
            List<Object> telefoneList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmTelefone",farmaceutico.getCrf());
            if(telefoneList != null && !telefoneList.isEmpty()){
                farmaceutico.setPhone((String)telefoneList.get(0));
            }else{
                FarmApp.logger.error("Erro ao carregar o telefone do farmaceutico!");
            }
        });

        Thread farmCpf = new Thread(() -> {
            List<Object> cpfList = SQLRunner.ExecuteSQLScript.SQLSelect("GetFarmCpf",farmaceutico.getCrf());
            if(cpfList != null && !cpfList.isEmpty()){
                farmaceutico.setCpf((String)cpfList.get(0));
            }else{
                FarmApp.logger.error("Erro ao carregar o cpf do farmaceutico!");
            }
        });

        farmName.start();
        farmPhone.start();
        farmCpf.start();

        try {
            farmName.join();
            farmPhone.join();
            farmCpf.join();
        } catch (InterruptedException e) {
            FarmApp.logger.error("Error ao aguardar a finalização dos threads de carregamento do farmaceutico!", e);
        }
    }

    public void clearFarmData(){
        farmaceutico.setCrf(null);
        farmaceutico.setNome(null);
        farmaceutico.setPhone(null);
        farmaceutico.setCpf(null);
    }
}
