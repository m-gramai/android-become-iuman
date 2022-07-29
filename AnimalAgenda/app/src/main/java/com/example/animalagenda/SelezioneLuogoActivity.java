package com.example.animalagenda; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.util.Locale;    //Viene importata la classe per le informazioni sull'area geografica
import java.util.List;      //Viene importata la classe per l'uso delle liste
import java.util.HashSet;   //Viene importata la classe per la manipolazione delle tabelle hash
import java.util.Iterator;  //Viene importata la classe per gli iteratori
import java.io.IOException; //Viene importata la classe per la gestione degli errori di lettura dei file

/*Vengono importate le classi di Android*/
import androidx.appcompat.app.AppCompatActivity; //Viene importata la classe estesa dall'activity
import android.os.Build;                         //Viene importata la classe per le proprietà di sistema
import android.os.Bundle;                        //Viene importata la classe per lo stato dell'activity
import android.os.StrictMode;                    //Viene importata la classe per il controllo dei task
import android.preference.PreferenceManager;     //Viene importata la classe per il gestore delle preferenze
import android.view.KeyEvent;                    //Viene importata la classe per gli eventi della tastiera
import android.view.View;                        //Viene importata la classe per la manipolazione delle viste
import android.view.inputmethod.EditorInfo;      //Viene importata la classe per le informazioni sulla scrittura
import android.widget.EditText;                  //Viene importata la classe per le view del tipo caselle di testo
import android.widget.TextView;                  //Viene importata la classe per la manipolazione delle aree di testo
import android.widget.Toast;                     //Viene importata la classe per le notifiche a scomparsa
import android.location.Address;                 //Viene importata la classe per gli indirizzi geografici

/*Viene importata la classe per il bottom sheet fragment*/
import com.google.android.material.bottomsheet.BottomSheetDialog;

/*Vengono importate le classi per la gestione della vista della mappa*/
import org.osmdroid.events.MapEventsReceiver;             //Viene importata l'interfaccia per la gestione degli eventi
import org.osmdroid.views.MapView;                        //Viene importata la classe della vista della mappa
import org.osmdroid.config.Configuration;                 //Viene importata la classe per la configurazione
import org.osmdroid.api.IMapController;                   //Viene importata la classe per il gestore della mappa
import org.osmdroid.views.CustomZoomButtonsController;    //Viene importata la vista per i pulsanti di ingrandimento
import org.osmdroid.views.overlay.MapEventsOverlay;       //Viene importata la classe per la gestione degli eventi
import org.osmdroid.util.GeoPoint;                        //Viene importata la classe per i punti geografici
import org.osmdroid.views.overlay.Marker;                 //Viene importata la classe per i segnalini
import org.osmdroid.bonuspack.location.GeocoderNominatim; //Viene importata la classe per la lettura degli indirizzi

/*Vengono importate le classi ausiliarie per la gestione dei luoghi*/
import com.example.animalagenda.luoghi.LuoghiSuggeriti; //Viene importata la classe per ottenere i luoghi nelle vicinanze
import com.example.animalagenda.luoghi.Luogo;           //Viene importata la classe per le informazioni sul luogo
import com.example.animalagenda.luoghi.Servizio;        //Viene importata l'enumerazione per la tipologia dei luoghi suggeriti

/*Vengono importate rispettivamente la classe per la vista della lista dei dettagli del luogo che
  deve essere mostrata nel bottom sheet e la classe per la costruzione degli elementi da mostrare
  all'interno di tale lista*/
import com.example.animalagenda.view.luogo.ListaDettagliLuogo;
import com.example.animalagenda.view.luogo.ListaDettagliLuogoAdapter;

/**
 * La classe gestisce l'activity per la selezione del luogo in cui si svolgerà l'evento.
 */
public class SelezioneLuogoActivity extends AppCompatActivity implements MapEventsReceiver {
    /*Definizione delle costanti*/
    private final static String USER_AGENT = "MyOwnUserAgent/1.0"; //User Agent per le connesioni
    private final static double INGRANDIMENTO_PREDEFINITO = 15.25; //Ingrandimento iniziale della mappa
    private final static int N_RISULTATI = 1;                      //Numero di risultati di ricerca da considerare
    private final static int PRIMO_RISULTATO = 0;                  //Indice del risultato di ricerca da considerare

    /*Definizione delle variabili d'istanza*/
    private IMapController gestoreMappa;                                   //Gestore della mappa
    private LuoghiSuggeriti luoghiConsigliati;                             //Lista dei luoghi suggeriti
    private HashSet<Marker> segnapostiLuoghiConsigliati = new HashSet<>(); //Lista dei segnalini dei luoghi suggeriti
    private Marker posizioneCercata;                                       //Segnaposto della posizione cercata

    /*Definizione delle viste*/
    private MapView osmvMappa;       //Definizione della proprietà per la mappa
    private EditText etRicercaLuogo; //Definizione della proprietà per la casella di ricerca

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
        this.osmvMappa = findViewById(R.id.osmvMappa);           //Inizializzazione oggetto
        this.etRicercaLuogo = findViewById(R.id.etRicercaLuogo); //Inizializzazione oggetto

        this.gestoreMappa = this.osmvMappa.getController(); //Inizializzazione del gestore della mappa

        /*Recupero dei luoghi suggeriti nelle vicinanze dell'utente*/
        this.luoghiConsigliati = new LuoghiSuggeriti(getAssets(), Servizio.VETERINARIO);

        inizializzazioneSegnapostiLuoghiSuggeriti(); //Inizializzazione della lista delle posizioni suggerite
        inizializzazioneMappa();                     //Inizializzazione della mappa

        /*Si collega alla casella di ricerca l'evento per l'esecuzione della ricerca e la
          visualizzazione dei risultati trovati*/
        this.etRicercaLuogo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String testoCercato = v.getText().toString(); //Definizione testo cercato dall'utente
                GeoPoint posizioneTrovata;                    //Dichiarazione posizione trovata
                boolean gestioneEvento = false;               //Definizione risultato evento

                /*Eventuali risultati di ricerca vengono mostrati solo dopo che l'utente conferma
                  l'inserimento*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    posizioneTrovata = ricercaTestuale(testoCercato); //Ricerca del luogo

                    if (aggiornamentoCentroMappa(posizioneTrovata)) {   //La ricerca ha prodotto risultati
                        if (datiLuogoCercato(posizioneTrovata) != null) //Si controlla la corrispondenza a un luogo
                            longPressHelper(posizioneTrovata);          //Si mostrano le informazioni del luogo

                        gestioneEvento = true; //Viene aggiornato il controllo sull'evento
                    }
                    else                                      //Non sono state trovate corrispondenze
                        nessunRisultatoTrovato(testoCercato); //Viene mostrato un avviso all'utente
                }

                /*Si restituisce un valore logico per segnalare la gestione dell'evento*/
                return gestioneEvento;
            }
        });
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
        /*Dichiarazione e inizializzazione della variabile temporanea per il controllo della
          posizione selezionata dall'utente*/
        GeoPoint tmp = this.posizioneCercata == null ? null : this.posizioneCercata.getPosition();
        boolean gestioneEvento = false;

        /*Se la posizione corrisponde a quella di un segnaposto già posizionato allora non si
          gestisce l'evento*/
        if (this.osmvMappa != null && ricercaLuogoConsigliato(p) == null && !p.equals(tmp)) {
            Luogo informazioni = datiLuogoCercato(p); //Dichiarazione e inizializzazione del luogo cercato

            if (informazioni != null) {                                     //Il luogo non risulta valido
                this.osmvMappa.getOverlays().remove(this.posizioneCercata); //Rimozione del vecchio segnaposto
                this.posizioneCercata = segnapostoDaPosizionare(p);         //Salvataggio della selezione
                this.osmvMappa.getOverlays().add(this.posizioneCercata);    //Creazione di un nuovo segnaposto
                this.osmvMappa.invalidate();                                //Ricarica della mappa
            }

            mostraDettagliLuogo(informazioni); //Vengono mostrati i dettagli del luogo
            gestioneEvento = true;             //Viene aggiornato il controllo sull'evento
        }

        return gestioneEvento; //Viene restituito un valore che indica se l'evento è stato gestito
    }

    /**
     * Questo metodo si occupa di inizializzare la mappa attraverso settaggi predefiniti.
     */
    private void inizializzazioneMappa() {
        /*Simulazione del recupero della gelocalizzazione dell'utente per l'inizializzazione della
          posizione iniziale visualizzata mappa*/
        final GeoPoint geolocalizzazione =
                this.luoghiConsigliati == null ? null : this.luoghiConsigliati.getGeolocalizzazione();

        /*Si verifica che le proprietà associate alla mappa e al suo gestore siano state inizializzate*/
        if (this.osmvMappa != null && this.gestoreMappa != null) {
            /*Si abilitano i gesti per l'ingrandimento*/
            this.osmvMappa.setMultiTouchControls(true);
            this.osmvMappa.getZoomController()
                          .setVisibility(CustomZoomButtonsController.Visibility.NEVER);

            this.gestoreMappa.setZoom(INGRANDIMENTO_PREDEFINITO); //Si imposta l'ingrandimento predefinito
            aggiornamentoCentroMappa(geolocalizzazione);          //Si imposta la posizione iniziale

            /*Si impostano gli overlay per la cattura e relativa gestione degli eventi e per la
              visualizzazione dei segnaposti associati ai luoghi nelle vicinanze dell'utente
              suggeriti dal sistema. Infine, si ricarica la mappa per rendere effettive le modifiche*/
            this.osmvMappa.getOverlays().add(0, new MapEventsOverlay(this));
            this.osmvMappa.getOverlays().addAll(this.segnapostiLuoghiConsigliati);
            this.osmvMappa.invalidate();
        }
    }

    /**
     * Questo metodo consente di aggiornare la visualizzazione del centro della mappa.
     * @param coordinate Posizione geografica da centrare sulla mappa.
     * @return Viene restituito true se l'aggiornamento della mappa è stato eseguito senza problemi,
     * false altrimenti.
     */
    private boolean aggiornamentoCentroMappa(GeoPoint coordinate) {
        boolean gestioneEvento = false; //Dichiarazione e inizializzazione controllo da restituire

        if (this.gestoreMappa != null && coordinate != null) { //Si verifica la validità dei riferimenti
            this.gestoreMappa.setCenter(coordinate);           //Si imposta il luogo da visualizzare sulla mappa
            gestioneEvento = true;                             //Si aggiorna il valore da restituire
        }

        return gestioneEvento; //Si restituisce un valore logico per segnalare il corretto aggiornamento
    }

    /**
     * Questo metodo si occupa di inizializzare i segnaposti dei luoghi suggeriti dal sistema.
     */
    private void inizializzazioneSegnapostiLuoghiSuggeriti() {
        /*Si controlla che siano già stati recuperati i luoghi suggeriti*/
        if (this.luoghiConsigliati != null) {
            /*Dichiarazione e inizializzazione dell'iteratore sui luoghi salvati*/
            Iterator<Luogo> iteratore = this.luoghiConsigliati.getLuoghiTrovati().iterator();

            Luogo luogo;         //Dichiarazione variabile ausiliaria per il luogo da considerare
            GeoPoint coordinate; //Dichiarazione variabile ausiliaria per le coordinate del luogo
            Marker segnaposto;   //Dichiarazione variabile ausiliaria per il segnaposto

            while (iteratore.hasNext()) {                         //Si scorrono i luoghi salvati
                luogo = iteratore.next();                         //Si recupera il luogo corrente
                coordinate = luogo.getCoordinate();               //Si recuperano le coordinate del luogo
                segnaposto = segnapostoDaPosizionare(coordinate); //Si crea un nuovo segnaposto

                /*Il segnaposto viene aggiunto alla lista di quelli da mostrare in maniera predefinita
                  sulla mappa*/
                this.segnapostiLuoghiConsigliati.add(segnaposto);
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
                    /*Viene mostrato il bottom sheet con i dati del luogo se le informazioni sono
                      valide, altrimenti viene mostrato un messaggio di errore*/
                    mostraDettagliLuogo(ricercaSegnapostoPosizionato(marker.getPosition()));

                    return true; //Si restituisce un valore che indica che l'evento è stato gestito
                }
            });
        }

        return segnaposto; //Viene restituito il segnaposto creato
    }

    /**
     * Questo metodo si occupa di ricercare un luogo tra quello selezionato dall'utente e quelli
     * consigliati dall'applicazione.
     * @param coordinate Posizione geografica del luogo da cercare.
     * @return Viene restituito un oggetto di tipo Luogo se la ricerca ha prodotto un esito positivo,
     * altrimenti viene restituito un oggetto nullo.
     */
    private Luogo ricercaSegnapostoPosizionato(GeoPoint coordinate) {
        Luogo risultato; //Dichiarazione del risultato della ricerca da restituire

        if (this.posizioneCercata != null) {                    //Si controlla se l'utente ha selezionato già un luogo
            GeoPoint tmp = this.posizioneCercata.getPosition(); //Si recuperano le coordinate

            if (tmp != null && tmp.equals(coordinate))           //Controllo della validità delle coordinate
                risultato = datiLuogoCercato(coordinate);        //Si compara il luogo a quello selezionato
            else                                                 //Il luogo non è quello selezionato dall'utente
                risultato = ricercaLuogoConsigliato(coordinate); //Il luogo potrebbe essere uno di quelli consigliati
        }
        else                                                 //L'utente non ha ancora selezionato dei luoghi
            risultato = ricercaLuogoConsigliato(coordinate); //Il luogo potrebbe essere uno di quelli consigliati

        return risultato; //Viene restituito il risultato della ricerca
    }

    /**
     * Questo metodo permette di ottenere i dati di un luogo a partire dalle sue coordinate.
     * @param coordinate Posizione geografica del luogo.
     * @return Viene restituito un oggetto di tipo Luogo se le coordinate sono associate a un
     * indirizzo valido, altrimenti viene restituito un oggetto nullo.
     */
    private Luogo datiLuogoCercato(GeoPoint coordinate) {
        Luogo trovato = null; //Dichiarazione e inizializzazione dei dati da restituire

        /*Si controlla la compatibilità con il sistema operativo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*Vengono catturate eventuali eccezioni dalla lettura dei dati del luogo*/
            try {
                /*Dichiarazione e inizializzazione dell'oggetto che si occuperà di estrapolare i
                  dati del luogo*/
                GeocoderNominatim lettoreInformazioni = new GeocoderNominatim(
                        Locale.getDefault(),
                        USER_AGENT
                );

                /*Dichiarazione e inizializzazione della lista contenente i risultati della ricerca*/
                List<Address> risultatiRicerca = lettoreInformazioni.getFromLocation(
                        coordinate.getLatitude(),
                        coordinate.getLongitude(),
                        N_RISULTATI
                );

                /*Dichiarazione e inizializzazione della stringa rappresentante l'indirizzo del luogo*/
                String indirizzo =
                        risultatiRicerca.isEmpty()                                     ?
                        null                                                           :
                        Luogo.creazioneIndirizzo(risultatiRicerca.get(PRIMO_RISULTATO));

                /*Se l'indirizzo risulta valido allora si restituisce un oggetto non nullo*/
                if (indirizzo != null)
                    trovato = new Luogo(coordinate, indirizzo, null, null, null, null);
            }
            catch (IOException ignored) { }
        }

        return trovato; //Viene restituito l'oggetto creato
    }

    /**
     * Questo metodo si occupa di scansionare i luoghi già salvati al fine di cercare una
     * corrispondenza con quello desiderato.
     * @param coordinate Coordinate geografiche del luogo da ricercare.
     * @return Viene restituito un oggetto contenente le informazioni del luogo se la ricerca ha dato
     * esito positivo, null altrimenti.
     */
    private Luogo ricercaLuogoConsigliato(GeoPoint coordinate) {
        boolean trovato = false; //Dichiarazione e inizializzazione della variabile di controllo per la ricerca
        Luogo risultato = null;  //Dichiarazione e inizializzazione del risultato della ricerca

        /*Si controlla che siano già stati recuperati i luoghi suggeriti*/
        if (this.luoghiConsigliati != null) {
            /*Dichiarazione e inizializzazione dell'iteratore sui luoghi salvati*/
            Iterator<Luogo> iteratore = this.luoghiConsigliati.getLuoghiTrovati().iterator();

            while (iteratore.hasNext() && !trovato) {              //Si scorrono i luoghi salvati
                risultato = iteratore.next();                      //Si recupera l'oggetto corrente
                trovato = risultato.comparaCoordinate(coordinate); //Si confrontano le coordinate dei luoghi
            }
        }

        if (!trovato)         //Il luogo non è stato trovato tra quelli salvati
            risultato = null; //Ripristino del risultato di ricerca

        return risultato; //Viene restituito un oggetto contenente il risultato della ricerca
    }

    /**
     * Questo metodo permette di ottenere le coordinate di un luogo a partire dall'indirizzo.
     * @param luogo Indirizzo del luogo da cercare.
     * @return Viene restituito un oggetto di tipo GeoPoint se le coordinate sono associate a un
     * indirizzo valido, altrimenti viene restituito un oggetto nullo.
     */
    private GeoPoint ricercaTestuale(String luogo) {
        GeoPoint risultato = null; //Dichiarazione e inizializzazione dei dati da restituire

        /*Si controlla la compatibilità con il sistema operativo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*Vengono catturate eventuali eccezioni dalla lettura dei dati del luogo*/
            try {
                /*Dichiarazione e inizializzazione dell'oggetto che si occuperà di estrapolare i
                  dati del luogo*/
                GeocoderNominatim lettoreInformazioni = new GeocoderNominatim(
                        Locale.getDefault(),
                        USER_AGENT
                );

                /*Dichiarazione e inizializzazione della lista contenente i risultati della ricerca*/
                List<Address> risultatiRicerca = lettoreInformazioni.getFromLocationName(
                        luogo,
                        N_RISULTATI
                );

                /*Dichiarazione e inizializzazione dell'oggetto ausiliario che contiene i dati del luogo*/
                Address tmp = risultatiRicerca.isEmpty() ? null : risultatiRicerca.get(PRIMO_RISULTATO);

                /*Se l'oggetto ausiliario risulta valido allora si restituisce un oggetto non nullo*/
                if (tmp != null)
                    risultato = new GeoPoint(tmp.getLatitude(), tmp.getLongitude());
            }
            catch (IOException ignored) { }
        }

        return risultato; //Viene restituito l'oggetto creato
    }

    /**
     * Questo metodo si occupa di creare il bottom sheet per la selezione del luogo.
     * @param dati Oggetto che contiene i dati del luogo che si desidera selezionare.
     */
    private void mostraDettagliLuogo(Luogo dati) {
        /*Dichiarazione e inizializzazione della variabile per il controllo sulla validità dei dati
          da mostrare*/
        boolean informazioniValide =
                dati != null && (dati.nomeValido() || !dati.listaDati().isEmpty());

        /*Dichiarazione e inizializzazione del riferimento al bottom sheet*/
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this, R.style.TemaDettagliLuogo);
        bottomSheet.setContentView(R.layout.bottom_sheet_dettagli_luogo);

        /*Dichiarazione e inizializzazione degli oggetti per contenere le view interne al bottom sheet*/
        TextView tvbsTitolo = bottomSheet.findViewById(R.id.tvbsTitolo);
        ListaDettagliLuogo lvbsListaInformazioni = bottomSheet.findViewById(R.id.lvbsListaInformazioni);

        /*Si controlla la validità dei riferimenti e quella delle informazioni*/
        if (tvbsTitolo != null && lvbsListaInformazioni != null && informazioniValide) {
            /*Dichiarazione e inizializzazione dell'oggetto che si occupa di creare la lista delle
              informazioni associate al luogo*/
            ListaDettagliLuogoAdapter creatoreListaDettagli = new ListaDettagliLuogoAdapter(
                    this,
                    R.id.lvbsListaInformazioni,
                    dati.listaDati()
            );

            if (dati.nomeValido()) {                    //Si verifica la validità del nome del luogo
                tvbsTitolo.setText(dati.getNome());     //Si imposta il nome come titolo del bottom sheet
                tvbsTitolo.setVisibility(View.VISIBLE); //Si rende visibile l'area di testo
            }

            /*Viene creata e mostrata la lista dei dati del luogo*/
            lvbsListaInformazioni.setAdapter(creatoreListaDettagli);

            bottomSheet.show(); //Viene mostrato il bottom sheet
        }
        /*Il bottom sheet non può essere creato perché sarebbe vuoto*/
        else
            /*Si mostra un avviso in sovraimpressione per segnalare l'errore*/
            informazioniLuogoNonDisponibili();
    }

    /**
     * Questo metodo si occupa di creare un messaggio di errore che indica l'impossibilità di
     * recuperare le informazioni associate a un luogo e che viene mostrato in sovraimpressione
     * per un tempo limitato.
     */
    private void informazioniLuogoNonDisponibili() {
        /*Viene mostrato all'utente un messaggio di errore in sovraimpressione*/
        Toast.makeText(getApplicationContext(), "Nessuna informazione da mostrare", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Questo metodo si occupa di creare un messaggio di errore che indica il fallimento della
     * ricerca testuale.
     * @param valoreCercato Stringa contenente il testo cercato dall'utente.
     */
    private void nessunRisultatoTrovato(String valoreCercato) {
        /*Dichiarazione e inizializzazione del messaggio da mostrare all'utente*/
        String messaggio = "Nessun risultato per \"" + valoreCercato + "\"";

        /*Viene mostrato all'utente un messaggio di errore in sovraimpressione*/
        Toast.makeText(getApplicationContext(), messaggio, Toast.LENGTH_SHORT).show();
    }
}