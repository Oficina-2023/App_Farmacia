package br.com.apoo2021.farm.objects;

import java.sql.Timestamp;
import java.util.Date;

public class Vendas {

    private long nf;
    private Timestamp data;
    private String cpf;
    private String crf;

    public long getNf() {
        return nf;
    }

    public void setNf(long nf) {
        this.nf = nf;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCrf(String s) {
        return crf;
    }

    public void setCrf(String crf) {
        this.crf = crf;
    }
}
