package com.example.animalagenda.view.calendario; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.util.Collection; //Viene importata la classe per la manipolazione delle strutture dati Collection
import java.util.HashSet;    //Viene importata la classe per la manipolazione delle tabelle hash

/*Vengono importate le classi di Android*/
import android.content.res.Resources; //Viene importata la classe per la gestione dei file necessari al funzionamento dell'applicazione

/*Vengono importate le classi di gestione generale dell'applicazione*/
import com.example.animalagenda.R; //Viene importata la classe per la gestione delle risorse

/*Vengono importate le classi per la gestione della vista del calendario*/
import com.prolificinteractive.materialcalendarview.DayViewDecorator; //Viene importata l'interfaccia per le modifiche grafiche
import com.prolificinteractive.materialcalendarview.CalendarDay;      //Viene importata la classe per la gestione dei giorni
import com.prolificinteractive.materialcalendarview.DayViewFacade;    //Viene importata la classe per la vista da modificare
import com.prolificinteractive.materialcalendarview.spans.DotSpan;    //Viene importata la classe per la formattazione dei pallini

/**
 * Questa classe si occupa di implementare i metodi dell'interfaccia per la personalizzazione dello
 * stile grafico dei giorni del calendario associati a un evento.
 */
public class EventoDecorator implements DayViewDecorator {
    /*Definizione delle costanti*/
    private final static float raggioSimbolo = 3.5f; //Raggio del cerchio sotto ai giorni che contengono eventi
    private final int colore;                        //Colore da assegnare al simbolo che indica gli eventi
    private final HashSet<CalendarDay> dateEventi;   //Tabella hash per contenere le date in cui sono presenti eventi

    /*Definizione del costruttore*/

    /**
     * Costruttore per la personalizzazione dello stile grafico dei giorni del calendario associati
     * a un evento.
     * @param res        Oggetto per la gestione dei file necessari al funzionamento dell'applicazione.
     * @param dateEventi Collezione che contiene le date in cui sono programmati eventi.
     */
    public EventoDecorator(Resources res, Collection<CalendarDay> dateEventi) {
        this.colore = res.getColor(R.color.teal_200); //Inizializzazione proprietà
        this.dateEventi = new HashSet<>(dateEventi);  //Inizializzazione proprietà
    }

    /*Definizione dei metodi*/

    /**
     * Questo metodo effettua l'override per l'implementazione effettiva del controllo che stabilisce
     * il giorno a cui applicare la formattazione grafica.
     * @param day Giorno per il controllo dell'applicazione dello stile.
     * @return Viene restituito true se il giorno passato in ingresso corrisponde al giorno corrente,
     * false altrimenti.
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        /*Si modifica solo lo stile grafico dei giorni che presentano degli eventi in programma*/
        return this.dateEventi.contains(day);
    }

    /**
     * Questo metodo effettua l'override per l'implementazione effettiva della personalizzazione
     * dello stile per il giorno associato all'evento.
     * @param view Vista a cui applicare le modifiche grafiche.
     */
    @Override
    public void decorate(DayViewFacade view) {
        /*Creazione di un simbolo che evidenzia i giorni in cui sono programmati degli eventi*/
        view.addSpan(new DotSpan(raggioSimbolo, this.colore));
    }
}