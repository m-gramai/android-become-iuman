package com.example.animalagenda;

import com.example.animalagenda.luoghi.Luogo;

import java.util.Calendar;


/**
 * Questa classe contiene le informazioni generali degli eventi programmabili o Attività generiche
 */
public class Attivita {
    private String titolo;

    private Calendar data;
    private int ora;
    private int min;

    private Ripetizione ripetizione;

    private Luogo luogo;

    private Avvisi avvisi;
    private Animale animale;

    private String note;


    // metodi per il campo Titolo
    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }


    // metodi per il campo Data
    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    // metodi per il campo Ora
    public int getOra() {
        return ora;
    }

    public void setOra(int ora) {
        this.ora = ora;
    }

    // metodi per il campo Min
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    // metodi per il campo Luogo
    public Luogo getLuogo() {
        return luogo;
    }
    public void setLuogo(Luogo luogo) {
        this.luogo = luogo;
    }

    // metodi per il campo Note
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // dichiarazione costruttore
    public Attivita(String titolo, Calendar data, int ora, int min, Ripetizione ripetizione,Luogo luogo, Avvisi avvisi, Animale animale, String note ) {
        this.titolo = titolo;
        this.data = data;
        this.ora = ora;
        this.ripetizione = ripetizione;
        this.luogo = luogo;
        this.avvisi = avvisi;
        this.animale = animale;
        this.note = note;
    }
}
