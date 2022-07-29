package com.example.animalagenda; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.util.ArrayList; //Viene importata la classe per la struttura dati ArrayList
import java.util.Arrays;    //Viene importata la classe per la manipolazione dei vettori

/*Vengono importate le classi di Android*/
import androidx.appcompat.app.AppCompatActivity; //Viene importata la classe estesa dall'activity
import android.os.Bundle;                        //Viene importata la classe per lo stato dell'activity
import android.widget.RadioGroup;                //Viene importata la classe per la gestione delle liste a selezione singola

/*Vengono importate la classe per la vista delle opzioni di ripetizione giornaliera e la classe per
  la costruzione delle stesse opzioni da mostrare*/
import com.example.animalagenda.view.ripetizione.OpzioniRipetizioneGiornaliera;
import com.example.animalagenda.view.ripetizione.OpzioniRipetizioneGiornalieraAdapter;

/**
 * La classe gestisce l'activity per la modifica delle opzioni di ripetizione dell'evento.
 */
public class SelezioneRipetizioneActivity extends AppCompatActivity {
    /*Definizione delle variabili d'istanza*/
    private RadioGroup rbgOpzioniRipetizione; //Dichiarazione della proprietà per la lista di opzioni

    /*Dichiarazione della proprietà per le opzioni di ripetizione giornaliera*/
    private OpzioniRipetizioneGiornaliera gvOpzioniRipetizioneGiornaliera;

    /*Dichiarazione della proprietà per l'oggetto che gestisce le opzioni di ripetizione giornaliera*/
    private OpzioniRipetizioneGiornalieraAdapter creatoreOpzioniRipetizioneGiorno;

    /*Definizione dei metodi*/

    /**
     * Override del metodo contenente le istruzioni da eseguire alla creazione dell'activity corrente.
     * @param savedInstanceState Oggetto di tipo Bundle contenente lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                      //Si richiama il metodo corrispondente della superclasse
        setContentView(R.layout.activity_selezione_ripetizione); //Inizializzazione del layout dell'activity

        /*Si collegano gli oggetti alle viste del layout attraverso i rispettivi identificativi*/
        this.gvOpzioniRipetizioneGiornaliera = findViewById(R.id.gvOpzioniRipetizioneGiornaliera);
        this.rbgOpzioniRipetizione = findViewById(R.id.rbgOpzioniRipetizione);

        /*Inizializzazione dell'oggetto che si occupa di creare le opzioni di ripetizione giornaliera*/
        this.creatoreOpzioniRipetizioneGiorno = new OpzioniRipetizioneGiornalieraAdapter(
                this,
                R.id.gvOpzioniRipetizioneGiornaliera,
                new ArrayList<>(
                        Arrays.asList(getResources().getStringArray(R.array.label_giorni_settimana))
                )
        );

        /*Vengono create e mostrate le opzioni di ripetizione giornaliera*/
        this.gvOpzioniRipetizioneGiornaliera.setAdapter(this.creatoreOpzioniRipetizioneGiorno);

        /*Si definisce l'evento per registrare la modifica alle opzioni di ripetizione*/
        this.rbgOpzioniRipetizione.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*Si selezionano le modifiche da apportare alle impostazioni di ripetizione
                  dell'evento per mezzo dell'identificativo della singola opzione*/
                if (checkedId == group.findViewById(R.id.rbRipetizioneSettimanale).getId())
                    /*Si abilitano i pulsanti relativi alle opzioni di ripetizione giornaliera*/
                    creatoreOpzioniRipetizioneGiorno.abilitazionePulsanti();
                else
                    /*Si disabilitano i pulsanti relativi alle opzioni di ripetizione giornaliera*/
                    creatoreOpzioniRipetizioneGiorno.disabilitazionePulsanti();
            }
        });
    }
}