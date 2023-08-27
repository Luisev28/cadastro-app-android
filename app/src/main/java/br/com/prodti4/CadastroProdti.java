package br.com.prodti4;

import java.util.Map;

public class CadastroProdti {

    String Patrimonio, Modelo, Servico, Sistemaoperacional, Tecnico,value;

    public CadastroProdti() {

    }

    public CadastroProdti(String patrimonio, String modelo, String servico, String sistemaoperacional, String tecnico, Map<String, Object> value) {
        Patrimonio = patrimonio;
        Modelo = modelo;
        Servico = servico;
        Sistemaoperacional = sistemaoperacional;
        Tecnico = tecnico;
    }

    public String getPatrimonio() {
        return Patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        Patrimonio = patrimonio;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getServico() {
        return Servico;
    }

    public void setServico(String servico) {
        Servico = servico;
    }

    public String getSistemaoperacional() {
        return Sistemaoperacional;
    }

    public void setSistemaoperacional(String sistemaoperacional) {
        Sistemaoperacional = sistemaoperacional;
    }

    public String getTecnico() {
        return Tecnico;
    }

    public void setTecnico(String tecnico) {
        Tecnico = tecnico;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

