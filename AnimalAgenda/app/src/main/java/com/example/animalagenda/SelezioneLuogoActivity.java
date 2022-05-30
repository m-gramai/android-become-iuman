package com.example.animalagenda; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.util.HashSet;  //Viene importata la classe per la manipolazione delle tabelle hash
import java.util.Iterator; //Viene importata la classe per gli iteratori

/*Vengono importate le classi di Android*/
import androidx.appcompat.app.AppCompatActivity; //Viene importata la classe estesa dall'activity
import android.os.Bundle;                        //Viene importata la classe per lo stato dell'activity
import android.os.StrictMode;                    //Viene importata la classe per il controllo dei task
import android.preference.PreferenceManager;     //Viene importata la classe per il gestore delle preferenze

/*Vengono importate le classi per la gestione della vista della mappa*/
import org.osmdroid.events.MapEventsReceiver;          //Viene importata l'interfaccia per la gestione degli eventi
import org.osmdroid.views.MapView;                     //Viene importata la classe della vista della mappa
import org.osmdroid.config.Configuration;              //Viene importata la classe per la configurazione
import org.osmdroid.api.IMapController;                //Viene importata la classe per il gestore della mappa
import org.osmdroid.views.CustomZoomButtonsController; //Viene importata la vista per i pulsanti di ingrandimento
import org.osmdroid.views.overlay.MapEventsOverlay;    //Viene importata la classe per la gestione degli eventi
import org.osmdroid.util.GeoPoint;                     //Viene importata la classe per i punti geografici
import org.osmdroid.views.overlay.Marker;              //Viene importata la classe per i segnalini

/*Vengono importate le classi ausiliarie per la gestione dei luoghi*/
import com.example.animalagenda.luoghi.LuoghiSuggeriti;           //Viene importata la classe per ottenere i luoghi nelle vicinanze
import com.example.animalagenda.luoghi.Luogo;                     //Viene importata la classe per le informazioni sul luogo
import com.google.android.material.bottomsheet.BottomSheetDialog; //Viene importata la classe per il bottom sheet fragment

/**
 * La classe gestisce l'activity per la selezione del luogo in cui si svolgerà l'evento.
 */
public class SelezioneLuogoActivity extends AppCompatActivity implements MapEventsReceiver {
    /*Definizione delle costanti*/
    private final static double INGRANDIMENTO_PREDEFINITO = 15.25; //Ingrandimento iniziale della mappa

    /*Definizione delle variabili d'istanza*/
    private IMapController gestoreMappa;                                //Gestore della mappa
    private LuoghiSuggeriti luoghiSuggeriti;                            //Lista dei luoghi suggeriti
    private HashSet<Marker> segnaliniLuoghiSuggeriti = new HashSet<>(); //Lista dei segnalini dei luoghi suggeriti

    /*Definizione delle viste*/
    private MapView osmvMappa;                 //Definizione della proprietà per la mappa
    private BottomSheetDialog bsDettagliLuogo; //Definizione della proprietà per il bottom sheet

    /*Definizione dei metodi*/

    /**
     * Override del metodo contenente le istruzioni da eseguire alla creazione dell'activity corrente.
     * @param savedInstanceState Oggetto di tipo Bundle contenente lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                //Si richiama il metodo corrispondente della superclasse
        setContentView(R.layout.activity_selezione_luogo); //Inizializzazione del layout dell'activity

        /*Viene caricato lo user agent per poter stabilire la connessione ai server che forniscono i
          dati necessari alla visualizzazione della mappa*/
        Configuration.getInstance().load(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );

        /*Disabilitazione delle politiche che bloccano le connessioni di tipo sincrono*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*Si collegano gli oggetti alle viste del layout attraverso i rispettivi identificativi*/
        this.osmvMappa = findViewById(R.id.osmvMappa); //Inizializzazione oggetto

        /*Inizializzazione del bottom sheet che verrà utilizzato per mostrare le informazioni
          associate a un determinato luogo*/
        this.bsDettagliLuogo = new BottomSheetDialog(this, R.style.TemaDettagliLuogo);
        this.bsDettagliLuogo.setContentView(R.layout.bottom_sheet_dettagli_luogo);

        this.gestoreMappa = this.osmvMappa.getController();      //Inizializzazione del gestore della mappa
        this.luoghiSuggeriti = new LuoghiSuggeriti(getAssets()); //Recupero dei luoghi suggeriti

        inizializzazioneSegnapostiLuoghiSuggeriti(); //Inizializzazione della lista delle posizioni suggerite
        inizializzazioneMappa();                     //Inizializzazione della mappa
    }

    /**
     * Override del metodo che ha lo scopo di gestire i singoli tocchi sulla mappa.
     * @param p Posizione geografica associata all'evento catturato.
     * @return Restituisce true se l'evento è stato gestito e non deve propagarsi agli altri oggetti,
     * false altrimenti.
     */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false; //L'evento non viene gestito
    }

    /**
     * Override del metodo che ha lo scopo di gestire le pressioni prolungate sulla mappa.
     * @param p Posizione geografica associata all'evento catturato.
     * @return Restituisce true se l'evento è stato gestito e non deve propagarsi agli altri oggetti,
     * false altrimenti.
     */
    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false; //L'evento non viene gestito
    }

    /**
     * Questo metodo si occupa di inizializzare la mappa attraverso settaggi predefiniti.
     */
    private void inizializzazioneMappa() {
        /*Si verifica che le proprietà associate alla mappa e al suo gestore siano state inizializzate*/
        if (this.osmvMappa != null && this.gestoreMappa != null) {
            /*Si abilitano i gesti per l'ingrandimento*/
            this.osmvMappa.setMultiTouchControls(true);
            this.osmvMappa.getZoomController()
                          .setVisibility(CustomZoomButtonsController.Visibility.NEVER);

            /*Si impostano l'ingrandimento predefinito e il luogo iniziale da visualizzare*/
            this.gestoreMappa.setZoom(INGRANDIMENTO_PREDEFINITO);
            this.gestoreMappa.setCenter(null);

            /*Si impostano gli overlay per la cattura e relativa gestione degli eventi e per la
              visualizzazione dei segnaposti associati ai luoghi nelle vicinanze dell'utente
              suggeriti dal sistema. Infine, si ricarica la mappa per rendere effettive le modifiche*/
            this.osmvMappa.getOverlays().add(0, new MapEventsOverlay(this));
            this.osmvMappa.getOverlays().addAll(segnaliniLuoghiSuggeriti);
            this.osmvMappa.invalidate();
        }
    }

    /**
     * Questo metodo si occupa di inizializzare i segnaposti dei luoghi suggeriti dal sistema.
     */
    private void inizializzazioneSegnapostiLuoghiSuggeriti() {
        /*Si controlla che siano già stati recuperati i luoghi suggeriti*/
        if (this.luoghiSuggeriti != null) {
            /*Dichiarazione e inizializzazione dell'iteratore sui luoghi salvati*/
            Iterator<Luogo> iteratore = this.luoghiSuggeriti.getLuoghiTrovati().iterator();

            Luogo luogo;         //Dichiarazione variabile ausiliaria per il luogo da considerare
            GeoPoint coordinate; //Dichiarazione variabile ausiliaria per le coordinate del luogo
            Marker segnaposto;   //Dichiarazione variabile ausiliaria per il segnaposto

            while (iteratore.hasNext()) {                         //Si scorrono i luoghi salvati
                luogo = iteratore.next();                         //Si recupera il luogo corrente
                coordinate = luogo.getCoordinate();               //Si recuperano le coordinate del luogo
                segnaposto = segnapostoDaPosizionare(coordinate); //Si crea un nuovo segnaposto

                /*Il segnaposto viene aggiunto alla lista di quelli da mostrare in maniera predefinita
                  sulla mappa*/
                this.segnaliniLuoghiSuggeriti.add(segnaposto);
            }
        }
    }

    /**
     * Questo metodo si occupa di creare un nuovo segnaposto che verrà disegnato sulla mappa in un
     * secondo momento.
     * @param coordinate Posizione geografica in cui mostrare il segnaposto.
     * @return Viene restituito un oggetto di tipo Marker rappresentante il segnaposto da mostrare
     * sulla mappa.
     */
    private Marker segnapostoDaPosizionare(GeoPoint coordinate) {
        Marker segnaposto = null; //Dichiarazione e inizializzazione del segnaposto

        /*Si verifica che la proprietà associata alla mappa sia stata inizializzata*/
        if (this.osmvMappa != null) {
            segnaposto = new Marker(this.osmvMappa); //Creazione di un nuovo segnaposto

            segnaposto.setPosition(coordinate);                               //Si impostano le coordinate
            segnaposto.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); //Si imposta l'ancoraggio
            segnaposto.setInfoWindow(null);                                   //Vengono disabilitate le informazioni

            /*Viene impostato un gestore degli eventi per il segnaposto così da poterne mostrare
              le informazioni associate*/
            segnaposto.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    /*Dichiarazione e inizializzazione dei dati associati al luogo*/
                    Luogo dati = ricercaDatiLuogo(marker.getPosition());

                    return true; //Si restituisce un valore che indica che l'evento è stato gestito
                }
            });
        }

        return segnaposto; //Viene restituito il segnaposto creato
    }

    /**
     * Questo metodo si occupa di scansionare i luoghi già salvati al fine di cercare una
     * corrispondenza con quello desiderato.
     * @param coordinate Coordinate geografiche del luogo da ricercare.
     * @return Viene restituito un oggetto contenente le informazioni del luogo se la ricerca ha dato
     * esito positivo, null altrimenti.
     */
    private Luogo ricercaDatiLuogo(GeoPoint coordinate) {
        boolean trovato = false; //Dichiarazione e inizializzazione della variabile di controllo per la ricerca
        Luogo risultato = null;  //Dichiarazione e inizializzazione del risultato della ricerca

        /*Si controlla che siano già stati recuperati i luoghi suggeriti*/
        if (this.luoghiSuggeriti != null) {
            /*Dichiarazione e inizializzazione dell'iteratore sui luoghi salvati*/
            Iterator<Luogo> iteratore = this.luoghiSuggeriti.getLuoghiTrovati().iterator();

            while (iteratore.hasNext() && !trovato) {              //Si scorrono i luoghi salvati
                risultato = iteratore.next();                      //Si recupera l'oggetto corrente
                trovato = risultato.comparaCoordinate(coordinate); //Si confrontano le coordinate dei luoghi
            }
        }

        if (!trovato)         //Il luogo non è stato trovato tra quelli salvati
            risultato = null; //Ripristino del risultato di ricerca

        return risultato; //Viene restituito un oggetto contenente il risultato della ricerca
    }
}