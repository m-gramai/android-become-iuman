package com.example.animalagenda;

import java.util.Calendar;

public class Animale {
    private String specie;
    private String nome;
    private String razza;
    private String sesso;
    private Calendar dataNascita;
    private int eta;

    // metodi per il campo Specie
    public String getSpecie() {
        return specie;
    }
    public void setSpecie(String specie) {
        this.specie = specie;
    }

    // metodi per il campo Nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    // metodi per il campo razza
    public String getRazza() {
        return razza;
    }
    public void setRazza(String razza) {
        this.razza = razza;
    }

    // metodi per il campo Sesso
    public String getSesso() {
        return sesso;
    }
    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    // metodi per il campo DataNascita
    public Calendar getDataNascita() {
        return dataNascita;
    }
    public void setDataNascita(Calendar dataNascita) {
        this.dataNascita = dataNascita;
    }

    // metodi per il campo Età
    public int getEta() {
        return eta;
    }
    public void setEta(int eta) {
        this.eta = eta;
    }

    public Animale(String specie, String nome, String razza, String sesso, Calendar dataNascita, int eta) {
        this.specie = specie;
        this.nome = nome;
        this.razza = razza;
        this.sesso = sesso;
        this.dataNascita = dataNascita;
        this.eta = eta;
    }


}
