package com.example.animalagenda.view.luogo; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi Java*/
import java.util.ArrayList; //Viene importata la classe per la struttura dati ArrayList
import java.util.List;      //Viene importata la classe per l'uso delle liste

/*Vengono importate le classi di Android*/
import android.widget.ArrayAdapter;  //Viene importata la classe da estendere
import androidx.annotation.NonNull;  //Viene importata l'annotazione per gli oggetti che non devono essere nulli
import androidx.annotation.Nullable; //Viene importata l'annotazione per gli oggetti che possono essere nulli
import android.content.Context;      //Viene importata la classe per il contesto di esecuzione
import android.view.View;            //Viene importata la classe per la manipolazione delle viste
import android.view.ViewGroup;       //Viene importata la classe per la manipolazione delle viste
import android.app.Activity;         //Viene importata la classe per la manipolazione delle informazioni dell'activity
import android.widget.TextView;      //Viene importata la classe per la manipolazione delle aree di testo

/*Vengono importate le classi di gestione generale dell'applicazione*/
import com.example.animalagenda.R;                //Viene importata la classe per la gestione delle risorse
import com.example.animalagenda.luoghi.DatoLuogo; //Viene importata la classe ausiliaria per i dati del luogo

/**
 * La classe permette di popolare una ListView personalizzata con gli elementi rappresentanti i dati
 * del luogo scelto.
 */
public class ListaDettagliLuogoAdapter extends ArrayAdapter<DatoLuogo> {
    /*Dichiarazione variabili d'istanza*/
    private final DatoLuogo[] vettoreDati; //Vettore contenente la lista dei dati del luogo

    /*Definizione costruttore*/

    /**
     * Costruttore per creare un nuovo Adapter per la lista delle informazioni da stampare.
     * @param context  Oggetto di tipo Context contenente le informazioni sull'activity.
     * @param resource Intero rappresentante l'identificativo della risorsa per la creazione degli
     *                 elementi della vista.
     * @param objects  Oggetto di tipo List da stampare.
     */
    public ListaDettagliLuogoAdapter(@NonNull Context context,
                                     int resource,
                                     @NonNull List<DatoLuogo> objects) {
        super(context, resource, objects); //Viene richiamato il costruttore della superclasse

        /*Si controlla il tipo della lista degli elementi per la conversione a vettore*/
        if (objects instanceof ArrayList)
            /*Si trasforma la lista in un vettore*/
            this.vettoreDati = objects.toArray(new DatoLuogo[0]);
        /*La conversione non può essere eseguita*/
        else
            /*Viene lanciata un'eccezione per indicare l'impossibilità di effettuare la conversione*/
            throw new IllegalArgumentException(
                    "È consentita solo una struttura dati di tipo ArrayList."
            );
    }

    /*Definizione metodi*/

    /**
     * Override del metodo per la creazione della vista del singolo elemento che compone la lista.
     * @param position    Intero indicante la posizione nella lista.
     * @param convertView Oggetto di tipo Vista con la vista da stampare.
     * @param parent      Oggetto di tipo ViewGroup rappresentante il padre dell'elemento della lista.
     * @return Viene restituita la nuova vista creata rappresentante il singolo elemento della lista.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*Si verifica che la vista in ingresso non sia già valida*/
        if (convertView == null) {
            /*Si inizializza la vista in ingresso con una valida*/
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(
                    R.layout.item_list_bottom_sheet_dettagli_luogo,
                    parent,
                    false
            );
        }

        /*Dichiarazione e inizializzazione dell'area di testo per contenere l'informazione associata
          al luogo*/
        TextView tvbsInformazione = convertView.findViewById(R.id.tvbsInformazione);

        /*Stampa dell'informazione del luogo*/
        tvbsInformazione.setText(this.vettoreDati[position].getValore());

        return convertView; //Viene restituita la vista creata
    }
}