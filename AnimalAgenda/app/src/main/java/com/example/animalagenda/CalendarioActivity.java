package com.example.animalagenda; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.util.Locale; //Viene importata la classe per le informazioni sull'area geografica

/*Vengono importate le classi di Android*/
import androidx.appcompat.app.AppCompatActivity; //Viene importata la classe estesa dall'activity
import android.os.Bundle;                        //Viene importata la classe per lo stato dell'activity

/*Vengono importate le classi per la gestione della vista del calendario*/
import com.prolificinteractive.materialcalendarview.MaterialCalendarView; //Viene importata la classe per il calendario
import com.prolificinteractive.materialcalendarview.CalendarDay;          //Viene importata la classe per la gestione dei giorni

/**
 * La classe gestisce l'activity che mostra gli eventi salvati sul calendario.
 */
public class CalendarioActivity extends AppCompatActivity {
    /*Definizione delle viste*/
    private MaterialCalendarView mcvCalendario; //Definizione della proprietà per il calendario

    /*Definizione dei metodi*/

    /**
     * Override del metodo contenente le istruzioni da eseguire alla creazione dell'activity corrente.
     * @param savedInstanceState Oggetto di tipo Bundle contenente lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           //Si richiama il metodo corrispondente della superclasse
        setContentView(R.layout.activity_calendario); //Inizializzazione del layout dell'activity

        /*Si collegano gli oggetti alle viste del layout attraverso i rispettivi identificativi*/
        this.mcvCalendario = findViewById(R.id.mcvCalendario); //Inizializzazione oggetto

        inizializzazioneCalendario(); //Inizializzazione grafica del calendario
    }

    /**
     * Questa classe si occupa di personalizzare la formattazione delle etichette dei giorni della
     * settimana visualizzate nel calendario. La personalizzazione dipende dalla lingua selezionata
     * e richiede il riavvio dell'applicazione per mostrare eventuali modifiche.
     */
    private void formattazioneGiorniSettimana() {
        /*Si verifica che la proprietà associata alla vista del calendario sia stata assegnata
          e si controlla che la lingua impostata al momento dell'apertura dell'applicazione sia
          l'italiano*/
        if (this.mcvCalendario != null && Locale.getDefault().equals(Locale.ITALY)) {
            /*Formattazione delle etichette associate ai giorni della settimana del calendario*/
            this.mcvCalendario.setWeekDayLabels(R.array.label_giorni_settimana);
        }
    }

    /**
     * Questo metodo si occupa della personalizzazione della formattazione del giorno corrente.
     */
    private void formattazioneGiornoCorrente() {
        /*Si controlla che la proprietà associata alla vista del calendario sia stata assegnata*/
        if (this.mcvCalendario != null)
            /*Modifica della formattazione per il giorno corrente*/
            this.mcvCalendario.addDecorator(new GiornoCorrenteDecorator(getApplicationContext()));
    }

    /**
     * Questa funzione si occupa di inizializzare la grafica del calendario.
     */
    private void inizializzazioneCalendario() {
        /*Si controlla che la proprietà associata alla vista del calendario sia stata assegnata*/
        if (this.mcvCalendario != null) {
            formattazioneGiorniSettimana(); //Formattazione delle etichette dei giorni della settimana
            formattazioneGiornoCorrente();  //Formattazione del giorno corrente

            /*Selezione del giorno corrente*/
            this.mcvCalendario.setDateSelected(CalendarDay.today(), true);
        }
    }
}