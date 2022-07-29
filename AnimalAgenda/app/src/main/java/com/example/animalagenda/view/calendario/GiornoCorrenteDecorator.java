package com.example.animalagenda.view.calendario; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi di Android*/
import android.content.Context;               //Viene importata la classe per il contesto di esecuzione
import android.text.style.TextAppearanceSpan; //Viene importata la classe per la formattazione del testo

/*Vengono importate le classi di gestione generale dell'applicazione*/
import com.example.animalagenda.R; //Viene importata la classe per la gestione delle risorse

/*Vengono importate le classi per la gestione della vista del calendario*/
import com.prolificinteractive.materialcalendarview.DayViewDecorator; //Viene importata l'interfaccia per le modifiche grafiche
import com.prolificinteractive.materialcalendarview.CalendarDay;      //Viene importata la classe per la gestione dei giorni
import com.prolificinteractive.materialcalendarview.DayViewFacade;    //Viene importata la classe per la vista da modificare

/**
 * Questa classe si occupa di implementare i metodi dell'interfaccia per la personalizzazione dello
 * stile grafico dei giorni del calendario associati a un evento. Nello specifico, questa classe si
 * occupa dello stile del giorno corrente.
 */
public class GiornoCorrenteDecorator implements DayViewDecorator {
    /*Dichiarazione delle costanti*/
    private final Context context; //Costante per il contesto di esecuzione dell'applicazione

    /*Definizione del costruttore*/

    /**
     * Costruttore per la personalizzazione dello stile del giorno corrente.
     * @param context Contesto di esecuzione dell'applicazione.
     */
    public GiornoCorrenteDecorator(Context context) {
        this.context = context; //Inizializzazione proprietà
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
        /*Si modifica la formattazione del solo giorno corrente*/
        return day.equals(CalendarDay.today());
    }

    /**
     * Questo metodo effettua l'override per l'implementazione effettiva della personalizzazione
     * dello stile per il giorno corrente.
     * @param view Vista a cui applicare le modifiche grafiche.
     */
    @Override
    public void decorate(DayViewFacade view) {
        /*Dichiarazione e inizializzazione dell'oggetto che contiene la formattazione del giorno
          corrente*/
        TextAppearanceSpan formattazione = new TextAppearanceSpan(context, R.style.StileGiornoCorrente);

        /*Viene applicata la formattazione personalizzata per il giorno corrente*/
        view.addSpan(formattazione);
    }
}