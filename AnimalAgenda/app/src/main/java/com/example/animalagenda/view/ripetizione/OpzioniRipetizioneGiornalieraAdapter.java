package com.example.animalagenda.view.ripetizione; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi Java*/
import java.util.ArrayList; //Viene importata la classe per la struttura dati ArrayList
import java.util.List;      //Viene importata la classe per l'uso delle liste
import java.util.Locale;    //Viene importata la classe per le informazioni sull'area geografica

/*Vengono importate le classi di Android*/
import com.example.animalagenda.R;   //Viene importata la classe per la gestione delle risorse
import android.widget.ArrayAdapter;  //Viene importata la classe da estendere
import androidx.annotation.NonNull;  //Viene importata l'annotazione per gli oggetti che non devono essere nulli
import androidx.annotation.Nullable; //Viene importata l'annotazione per gli oggetti che possono essere nulli
import android.content.Context;      //Viene importata la classe per il contesto di esecuzione
import android.view.View;            //Viene importata la classe per la manipolazione delle viste
import android.view.ViewGroup;       //Viene importata la classe per la manipolazione delle viste
import android.app.Activity;         //Viene importata la classe per la manipolazione delle informazioni dell'activity
import android.widget.ToggleButton;  //Viene importata la classe per la manipolazione dei toggle button

/**
 * La classe permette di popolare una GridView personalizzata con le opzioni di ripetizione
 * giornaliera.
 */
public class OpzioniRipetizioneGiornalieraAdapter extends ArrayAdapter<String> {
    /*Dichiarazione delle costanti*/
    private final String[] etichetteGiorniSettimana; //Vettore contenente la lista dei dati del luogo
    private final boolean etichetteMaiuscole;        //Valore logico di controllo per lo stile delle etichette

    /*Dichiarazione variabili d'istanza*/
    private boolean pulsanteAbilitato; //Valore logico di controllo per l'abilitazione dei pulsanti

    /*Definizione costruttore*/

    /**
     * Costruttore per creare un nuovo Adapter per la lista delle opzioni per la selezione della
     * ripetizione settimanale durante la creazione di un nuovo evento.
     * @param context  Oggetto di tipo Context contenente le informazioni sull'activity.
     * @param resource Intero rappresentante l'identificativo della risorsa per la creazione degli
     *                 elementi della vista.
     * @param objects  Oggetto di tipo List da stampare.
     */
    public OpzioniRipetizioneGiornalieraAdapter(@NonNull Context context,
                                                int resource,
                                                @NonNull List<String> objects) {
        super(context, resource, objects); //Viene richiamato il costruttore della superclasse

        /*Si controlla il tipo della lista degli elementi per la conversione a vettore*/
        if (objects instanceof ArrayList)
            /*Si trasforma la lista in un vettore*/
            this.etichetteGiorniSettimana = objects.toArray(new String[0]);
            /*La conversione non può essere eseguita*/
        else
            /*Viene lanciata un'eccezione per indicare l'impossibilità di effettuare la conversione*/
            throw new IllegalArgumentException(
                    "È consentita solo una struttura dati di tipo ArrayList."
            );

        /*Se la lingua impostata al momento dell'apertura dell'applicazione è corrispondente
          all'italiano, si mostrano le etichette dei giorni della settimana maiuscole*/
        this.etichetteMaiuscole = Locale.getDefault().equals(Locale.ITALY);
    }

    /*Definizione metodi*/

    /**
     * Questo metodo si occupa dell'abilitazione dei pulsanti associati alle opzioni di ripetizione
     * giornaliera di un evento.
     */
    public void abilitazionePulsanti() {
        this.pulsanteAbilitato = true; //Si aggiorna il valore per abilitare il pulsante

        notifyDataSetInvalidated(); //Si ricarica la visualizzazione della griglia
    }

    /**
     * Questo metodo si occupa della disabilitazione dei pulsanti associati alle opzioni di
     * ripetizione giornaliera di un evento.
     */
    public void disabilitazionePulsanti() {
        this.pulsanteAbilitato = false; //Si aggiorna il valore per disabilitare il pulsante

        notifyDataSetInvalidated(); //Si ricarica la visualizzazione della griglia
    }

    /**
     * Override del metodo per la creazione della vista del singolo elemento che compone la griglia.
     * @param position    Intero indicante la posizione nella griglia.
     * @param convertView Oggetto di tipo Vista con la vista da stampare.
     * @param parent      Oggetto di tipo ViewGroup rappresentante il padre dell'elemento della griglia.
     * @return Viene restituita la nuova vista creata rappresentante il singolo elemento della griglia.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*Si verifica che la vista in ingresso non sia già valida*/
        if (convertView == null) {
            /*Si inizializza la vista in ingresso con una valida*/
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(
                    R.layout.item_grid_opzione_ripetizione_giorno,
                    parent,
                    false
            );
        }

        /*Dichiarazione e inizializzazione del toggle button associato all'opzione di ripetizione
          corrente*/
        ToggleButton tbOpzioneGiornoSettimana = convertView.findViewById(R.id.tbOpzioneGiornoSettimana);

        /*Si inizializzano le proprietà del toggle button*/
        tbOpzioneGiornoSettimana.setAllCaps(this.etichetteMaiuscole);
        tbOpzioneGiornoSettimana.setText(this.etichetteGiorniSettimana[position]);
        tbOpzioneGiornoSettimana.setTextOn(this.etichetteGiorniSettimana[position]);
        tbOpzioneGiornoSettimana.setTextOff(this.etichetteGiorniSettimana[position]);
        tbOpzioneGiornoSettimana.setEnabled(pulsanteAbilitato);

        return convertView; //Viene restituita la vista creata
    }
}