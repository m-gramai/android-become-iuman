package com.example.animalagenda.luoghi; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi di Android*/
import androidx.annotation.NonNull; //Viene importata la classe per l'annotazione degli oggetti che non possono essere nulli

/**
 * Questa è una classe ausiliaria da utilizzarsi per la creazione della lista dei dettagli del luogo.
 */
public class DatoLuogo {
    /*Dichiarazione delle variabili d'istanza*/
    private final String valore;      //Definizione della proprietà per il valore del dato
    private final TipoDatoLuogo tipo; //Definizione della proprietà che indica il tipo di dato

    /*Definizione del costruttore*/

    /**
     * Costruttore per creare un oggetto da utilizzare durante la creazione della lista di riepilogo
     * delle informazioni di un luogo.
     * @param valore Stringa che si vuole mostrare all'utente.
     * @param tipo   Tipo di dato che si vuole mostrare all'utente.
     */
    public DatoLuogo(@NonNull String valore, @NonNull TipoDatoLuogo tipo) {
        this.valore = valore; //Inizializzazione proprietà
        this.tipo = tipo;     //Inizializzazione proprietà
    }

    /*Definizione dei metodi*/

    /**
     * Questo metodo permette di recuperare il valore del dato salvato.
     * @return Viene restituita una stringa contenente l'esatta informazione passata in ingresso
     * durante la creazione dell'oggetto.
     */
    public String getValore() {
        return this.valore; //Viene restituito il valore della proprietà
    }

    /**
     * Questo metodo permette di ottenere la tipologia di dato associata al valore passato in
     * ingresso.
     * @return Viene restituito il valore di un'enumerazione che rappresenta il tipo di dato.
     */
    public TipoDatoLuogo getTipo() {
        return this.tipo; //Viene restituito il valore della proprietà
    }
}