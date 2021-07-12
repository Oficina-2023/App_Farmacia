package br.com.apoo2021.farm.objects;

public class UserManager {

    private final Farmaceutico farmaceutico = new Farmaceutico();

    public Farmaceutico getFarmaceutico() {
        return farmaceutico;
    }

    public void setFarmData(int crf, String nome){
        farmaceutico.setCrf(crf);
        farmaceutico.setNome(nome);
    }
}
