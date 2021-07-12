package br.com.apoo2021.farm.objects;

import java.util.Date;

public class Vendas {

    private Date data;
    private long cpf;
    private int crf;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public long getCpf() {
        return cpf;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public int getCrf() {
        return crf;
    }

    public void setCrf(int crf) {
        this.crf = crf;
    }
}
