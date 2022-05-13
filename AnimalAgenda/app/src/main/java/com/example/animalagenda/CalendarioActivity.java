package com.example.animalagenda; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi di Android*/
import androidx.appcompat.app.AppCompatActivity; //Viene importata la classe estesa dall'activity
import android.os.Bundle;                        //Viene importata la classe per lo stato dell'activity

/**
 * La classe gestisce l'activity che mostra gli eventi salvati sul calendario.
 */
public class CalendarioActivity extends AppCompatActivity {

    /**
     * Override del metodo contenente le istruzioni da eseguire alla creazione dell'activity corrente.
     * @param savedInstanceState Oggetto di tipo Bundle contenente lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           //Si richiama il metodo corrispondente della superclasse
        setContentView(R.layout.activity_calendario); //Inizializzazione del layout dell'activity
    }
}