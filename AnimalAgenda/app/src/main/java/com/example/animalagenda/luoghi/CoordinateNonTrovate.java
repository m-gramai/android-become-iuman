package com.example.animalagenda.luoghi; //Viene definito il package per le classi dell'applicazione

import java.io.IOException; //Viene importata la classe per la gestione degli errori di lettura dei file

/**
 * Questa eccezione viene lanciata in caso di errori di lettura delle coordinate geografiche.
 */
public class CoordinateNonTrovate extends IOException {
    /**
     * Costruttore per lanciare l'eccezione senza dover specificare altre informazioni.
     */
    public CoordinateNonTrovate() {
    }

    /**
     * Questo costruttore permette di associare un messaggio di errore all'eccezione.
     * @param messaggio Stringa rappresentante il messaggio di errore.
     */
    public CoordinateNonTrovate(String messaggio) {
        super(messaggio); //Inizializzazione della superclasse
    }

    /**
     * Questo costruttore permette di associare una causa all'eccezione.
     * @param causa Oggetto rappresentante la causa dell'eccezione.
     */
    public CoordinateNonTrovate(Throwable causa) {
        super(causa); //Inizializzazione della superclasse
    }

    /**
     * Questo costruttore permette di associare un messaggio di errore e una causa all'eccezione.
     * @param messaggio Stringa rappresentante il messaggio di errore.
     * @param causa     Oggetto rappresentante la causa dell'eccezione.
     */
    public CoordinateNonTrovate(String messaggio, Throwable causa) {
        super(messaggio, causa); //Inizializzazione della superclasse
    }
}
