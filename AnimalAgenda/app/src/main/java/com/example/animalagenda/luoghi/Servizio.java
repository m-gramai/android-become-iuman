package com.example.animalagenda.luoghi; //Viene definito il package per le classi dell'applicazione

/**
 * Questa enumerazione identifica la tipologia dei luoghi da suggerire all'utente in maniera
 * automatica al momento della selezione del luogo su cui si svolgerà l'attività.
 */
public enum Servizio {
    /*Definizione dei valori dell'enumerazione*/
    GEOLOCALIZZAZIONE("luoghi/geolocalizzazione.xml"),
    VETERINARIO("luoghi/veterinari.xml");

    /*Dichiarazione delle variabili d'istanza*/
    private final String file; //Percorso del file contenenete le informazioni sui luoghi suggeriti

    /*Definizione del costruttore*/

    /**
     * Costruttore per inizializzare le proprietà associate a ciascun valore dell'enumerazione.
     * @param file Stringa rappresentante il percorso del file XML su cui sono salvate le
     *             informazioni sui luoghi suggeriti.
     */
    Servizio(String file) {
        this.file = file; //Inizializzazione proprietà
    }

    /*Definizione dei metodi*/

    /**
     * Questo metodo permette di recuperare il percorso del file XML su cui sono salvate le
     * informazioni associate al servizio.
     * @return Viene restituita una stringa rappresentante il percorso del file.
     */
    public String getFile() {
        return this.file; //Viene restituito il valore della proprietà
    }
}
