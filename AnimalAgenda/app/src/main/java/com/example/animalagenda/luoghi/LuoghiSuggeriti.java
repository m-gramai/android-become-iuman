package com.example.animalagenda.luoghi; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.util.HashSet;   //Viene importata la classe per la manipolazione delle tabelle hash
import java.io.InputStream; //Viene importata la classe per la lettura dei file
import java.io.IOException; //Viene importata la classe per la gestione degli errori di lettura dei file
import java.util.Iterator;  //Viene importata la classe per gli iteratori

/*Vengono importate le classi di Android*/
import android.content.res.AssetManager;      //Viene importata la classe per la gestione delle risorse
import android.util.Xml;                      //Viene importata la classe per la gestione dei file XML
import org.xmlpull.v1.XmlPullParser;          //Viene importata la classe per la lettura dei file XML
import org.xmlpull.v1.XmlPullParserException; //Viene importata l'eccezione per gli errori di lettura

/*Vengono importate le classi per la gestione della vista della mappa*/
import org.osmdroid.util.GeoPoint; //Viene importata la classe per i punti geografici

/**
 * Questa classe si occupa di recuperare le informazioni sui luoghi che si trovano nelle vicinanze
 * dell'utente. Tali luoghi vengono suggeriti in base alla tipologia di attività che si sta salvando.
 * Questa funzionalità è soltanto simulata dal sistema.
 */
public class LuoghiSuggeriti {
    /*Definizione dei tag per la lettura delle informazioni sui luoghi*/
    private final static String TAG_RADICE = "luoghi";               //Nome del tag per la lista di luoghi
    private final static String TAG_LUOGO = "luogo";                 //Nome del tag per i luoghi
    private final static String TAG_LATITUDINE = "latitudine";       //Nome del tag per la latitudine
    private final static String TAG_LONGITUDINE = "longitudine";     //Nome del tag per la longitudine
    private final static String TAG_INDIRIZZO = "indirizzo";         //Nome del tag per l'indirizzo
    private final static String TAG_NOME = "nome";                   //Nome del tag per il nome
    private final static String TAG_TELEFONO = "telefono";           //Nome del tag per il telefono
    private final static String TAG_EMAIL = "email";                 //Nome del tag per l'e-mail
    private final static String TAG_SITO_INTERNET = "sito_internet"; //Nome del tag per il sito Internet

    /*Dichiarazione delle variabili d'istanza*/
    private final GeoPoint geolocalizzazione; //Posizione geografica utilizzata per il suggerimento dei luoghi
    private final HashSet<Luogo> luoghi;      //Struttura dati per contenere la lista dei luoghi trovati

    /*Definizione del costruttore*/

    /**
     * Costruttore per recuperare le informazioni sui luoghi suggeriti.
     * @param risorse          Gestore delle risorse per l'apertura dei file contenenti i dati sui
     *                         luoghi.
     * @param tipologiaCercata Enumerazione che rappresenta la tipologia di suggerimenti da
     *                         visualizzare sulla mappa.
     */
    public LuoghiSuggeriti(AssetManager risorse, Servizio tipologiaCercata) {
        GeoPoint geolocalizzazioneAux; //Definizione variabile ausiliaria
        HashSet<Luogo> luoghiAux;      //Definizione variabile ausiliaria

        /*Vengono catturate le eccezioni che potrebbero verificarsi dalla lettura dei file XML*/
        try {
            /*Viene effettuata la lettura del file che contiene i dati di fantasia sulla
              geolocalizzazione dell'utente*/
            geolocalizzazioneAux = letturaGeolocalizzazione(
                    risorse.open(Servizio.GEOLOCALIZZAZIONE.getFile())
            );
        }
        catch (XmlPullParserException | IOException e) {
            geolocalizzazioneAux = null; //Inizializzazione della posizione geografica con una nulla
        }

        /*Vengono catturate le eccezioni che potrebbero verificarsi dalla lettura dei file XML*/
        try {
            /*Viene effettuata la lettura del file che contiene i dati di fantasia sui luoghi utili
              nelle vicinanze dell'utente*/
            luoghiAux = letturaXml(risorse.open(tipologiaCercata.getFile()));
        }
        catch (XmlPullParserException | IOException e) {
            luoghiAux = new HashSet<>(); //Inizializzazione della struttra dati con una vuota
        }

        this.geolocalizzazione = geolocalizzazioneAux; //Inizializzazione proprietà
        this.luoghi = luoghiAux;                       //Inizializzazione proprietà
    }

    /*Definizione dei metodi*/

    /**
     * Metodo per ottenere la geolocalizzazione utilizzata per il recupero dei luoghi suggeriti.
     * @return Viene restituita la posizione geografica dell'utente.
     */
    public GeoPoint getGeolocalizzazione() {
        return this.geolocalizzazione; //Viene restituito il valore della proprietà
    }

    /**
     * Metodo per ottenere le informazioni sui luoghi utili trovati.
     * @return Restituisce una struttura dati di tipo HashSet contenente le informazioni sui luoghi
     * nelle vicinanze.
     */
    public HashSet<Luogo> getLuoghiTrovati() {
        return this.luoghi; //Viene restituito il valore della proprietà
    }

    /**
     * Questo metodo simula il recupero della geolocalizzazione dell'utente utilizzata durante la
     * ricerca dei luoghi utili da suggerire.
     * @param file File da leggere su cui è riportata la geolocalizzazione dell'utente.
     * @return Viene restituita una tra le posizioni geografiche lette nel file (non necessariamente
     * la prima in ordine), null se il file è vuoto.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     *del file.
     */
    private GeoPoint letturaGeolocalizzazione(InputStream file)
    throws XmlPullParserException, IOException {
        /*Dichiarazione e inizializzazione dei luoghi ottenuti dalla lettura del file contenente i
          dati sulla geolocalizzazione*/
        HashSet<Luogo> risultatoLetturaXml = letturaXml(file);

        /*Dichiarazione e inizializzazione dell'iteratore per lo scorrimento dei risultati ottenuti*/
        Iterator<Luogo> iteratore = risultatoLetturaXml.iterator();

        /*Dichiarazione e inizializzazione della posizione geografica rappresentante la
          geolocalizzazione dell'utente*/
        GeoPoint geolocalizzazione = null;

        /*Si controlla che sia stato trovato almeno un luogo*/
        if (iteratore.hasNext())
            /*Si considerano soltanto i dati di uno dei luoghi della lista*/
            geolocalizzazione = iteratore.next().getCoordinate();

        return geolocalizzazione; //Si restituisce il dato trovato oppure un dato nullo
    }

    /**
     * Questo metodo si occupa di leggere i file contenenti i dati sui luoghi utili nei pressi
     * dell'utente.
     * @param file File da leggere.
     * @return Viene restituita una struttura dati di tipo HashSet contenente i luoghi trovati e le
     * informazioni a essi associate.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private HashSet<Luogo> letturaXml(InputStream file)
    throws XmlPullParserException, IOException {
        /*Dichiarazione e inizializzazione della lista dei luoghi trovati da restituire*/
        HashSet<Luogo> luoghiTrovati;

        /*Vengono catturate eventuali eccezioni che vengono lanciate dalla lettura dei file XML e
          viene eseguito il codice di pulizia per la chiusura dei file*/
        try {
            /*Dichiarazione e inizializzazione dell'oggetto per la lettura dei file XML*/
            XmlPullParser parser = Xml.newPullParser();

            /*Vengono ignorati gli spazi dei nomi*/
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            parser.setInput(file, null); //Viene specificato qual è il file da leggere
            parser.nextTag();            //Viene ignorata la dichiarazione XML

            luoghiTrovati = scansioneLuoghi(parser); //Lettura del contenuto del file
        }
        finally {
            file.close(); //Chiusura del file
        }

        return luoghiTrovati; //Vengono restituiti i dati letti dal file
    }

    /**
     * Questo metodo si occupa della lettura effettiva del file contenente le informazioni dei luoghi.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una struttura dati di tipo HashSet contenente i luoghi trovati e le
     * informazioni a essi associate.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private HashSet<Luogo> scansioneLuoghi(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        /*Dichiarazione e inizializzazione della lista dei luoghi trovati da restituire*/
        HashSet<Luogo> luoghiTrovati = new HashSet<>();

        parser.require(XmlPullParser.START_TAG, null, LuoghiSuggeriti.TAG_RADICE); //Controllo del tag radice

        /*Si itera sugli elementi contenuti all'interno del file*/
        while (parser.next() != XmlPullParser.END_TAG)
            /*Si controlla di essere all'inizio di un elemento che contiene i dati associati a un luogo*/
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(TAG_LUOGO)) {
                /*Eventuali eccezioni vengono ignorate*/
                try {
                    Luogo tmp = letturaDatiLuogo(parser); //Lettura dei dati del luogo
                    luoghiTrovati.add(tmp);               //Salvataggio dei dati del luogo letti
                }
                catch (CoordinateNonTrovate ignored) { }
            }

        return luoghiTrovati; //Vengono restituiti i dati letti dal file
    }

    /**
     * Questo metodo si occupa della lettura dei dati di ogni singolo luogo salvato nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Restituisce tutte le informazioni lette che sono associate al luogo.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private Luogo letturaDatiLuogo(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        /*Dichiarazione e inizializzazione delle variabili per le coordinate geografiche*/
        Double latitudineLetta = null, longitudineLetta = null;

        /*Dichiarazione e inizializzazione delle variabili per le informazioni associate al luogo*/
        String indirizzoLetto = null, nomeLetto = null, telefonoLetto = null,
               emailLetta = null, sitoInternetLetto = null;

        parser.require(XmlPullParser.START_TAG, null, TAG_LUOGO); //Controllo del tag di apertura

        /*Si itera sugli elementi contenuti all'interno del tag che denota un luogo*/
        while (parser.next() != XmlPullParser.END_TAG) {
            /*Si controlla di essere all'inizio dell'elemento corrente*/
            if (parser.getEventType() == XmlPullParser.START_TAG)
                /*Viene selezionata l'opportuno metodo per recuperare le informazioni sulla base del
                  tipo dell'elemento*/
                switch (parser.getName()) {
                    case TAG_LATITUDINE:                                 //L'elemento contiene la latitudine
                        latitudineLetta = letturaLatitudine(parser);     //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    case TAG_LONGITUDINE:                                //L'elemento contiene la longitudine
                        longitudineLetta = letturaLongitudine(parser);   //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    case TAG_INDIRIZZO:                                  //L'elemento contiene l'indirizzo
                        indirizzoLetto = letturaIndirizzo(parser);       //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    case TAG_NOME:                                       //L'elemento contiene il nome
                        nomeLetto = letturaNome(parser);                 //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    case TAG_TELEFONO:                                   //L'elemento contiene il numero di telefono
                        telefonoLetto = letturaTelefono(parser);         //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    case TAG_EMAIL:                                      //L'elemento contiene l'indirizzo e-mail
                        emailLetta = letturaEMail(parser);               //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    case TAG_SITO_INTERNET:                              //L'elemento contiene il sito Internet
                        sitoInternetLetto = letturaSitoInternet(parser); //Si recuperano le informazioni dell'elemento
                        break;                                           //Si passa all'istruzione successiva
                    default:                                             //Tag non valido
                        break;                                           //Si passa all'istruzione successiva
                }
        }

        /*Si verifica la correttezza delle coordinate del luogo*/
        if (latitudineLetta == null || longitudineLetta == null)
            /*Viene lanciata un'eccezione nel caso in cui si verifichino problemi nel recupero
              delle coordinate del luogo*/
            throw new CoordinateNonTrovate(
                    "Non sono state definite le coordinate per il luogo."
            );

        /*Viene restituito un oggetto di tipo Luogo contenente le informazioni recuperate dal file*/
        return new Luogo(
                new GeoPoint(latitudineLetta, longitudineLetta),
                indirizzoLetto,
                nomeLetto,
                telefonoLetto,
                emailLetta,
                sitoInternetLetto
        );
    }

    /**
     * Questo metodo si occupa di leggere la latitudine associata al singolo luogo salvato nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituito un numero in virgola mobile a doppia precisione rappresentante la
     * latitudine letta.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private Double letturaLatitudine(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        Double latitudine = null; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_LATITUDINE); //Controllo del tag di apertura

        /*Eventuali eccezioni di conversione vengono ignorate*/
        try {
            /*Viene effettuata la lettura e l'apposita conversione del valore dell'elemento*/
            latitudine = Double.parseDouble(letturaDato(parser));
        }
        catch (NumberFormatException ignored) { }

        parser.require(XmlPullParser.END_TAG, null, TAG_LATITUDINE); //Controllo del tag di chiusura

        return latitudine; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere la longitudine associata al singolo luogo salvato nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituito un numero in virgola mobile a doppia precisione rappresentante la
     * longitudine letta.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private Double letturaLongitudine(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        Double longitudine = null; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_LONGITUDINE); //Controllo del tag di apertura

        /*Eventuali eccezioni di conversione vengono ignorate*/
        try {
            /*Viene effettuata la lettura e l'apposita conversione del valore dell'elemento*/
            longitudine = Double.parseDouble(letturaDato(parser));
        }
        catch (NumberFormatException ignored) {

        }

        parser.require(XmlPullParser.END_TAG, null, TAG_LONGITUDINE); //Controllo del tag di apertura

        return longitudine; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere l'indirizzo associato al singolo luogo salvato nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una stringa rappresentante l'indirizzo letto.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private String letturaIndirizzo(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        String indirizzo; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_INDIRIZZO); //Controllo del tag di apertura

        indirizzo = letturaDato(parser); //Viene effettuata la lettura del valore dell'elemento

        parser.require(XmlPullParser.END_TAG, null, TAG_INDIRIZZO); //Controllo del tag di chiusura

        return indirizzo; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere il nome associato al singolo luogo salvato nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una stringa rappresentante il nome letto.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private String letturaNome(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        String nome; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_NOME); //Controllo del tag di apertura

        nome = letturaDato(parser); //Viene effettuata la lettura del valore dell'elemento

        parser.require(XmlPullParser.END_TAG, null, TAG_NOME); //Controllo del tag di chiusura

        return nome; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere il numero di telefono associato al singolo luogo salvato
     * nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una stringa rappresentante il numero di telefono letto.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private String letturaTelefono(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        String telefono; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_TELEFONO); //Controllo del tag di apertura

        telefono = letturaDato(parser); //Viene effettuata la lettura del valore dell'elemento

        parser.require(XmlPullParser.END_TAG, null, TAG_TELEFONO); //Controllo del tag di chiusura

        return telefono; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere l'indirizzo e-mail associato al singolo luogo salvato
     * nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una stringa rappresentante l'indirizzo e-mail letto.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private String letturaEMail(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        String email; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_EMAIL); //Controllo del tag di apertura

        email = letturaDato(parser); //Viene effettuata la lettura del valore dell'elemento

        parser.require(XmlPullParser.END_TAG, null, TAG_EMAIL); //Controllo del tag di chiusura

        return email; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere l'indirizzo Internet associato al singolo luogo salvato
     * nel file.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una stringa rappresentante il sito Internet letto.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private String letturaSitoInternet(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        String sitoInternet; //Dichiarazione del valore da leggere e restituire

        parser.require(XmlPullParser.START_TAG, null, TAG_SITO_INTERNET); //Controllo del tag di apertura

        sitoInternet = letturaDato(parser); //Viene effettuata la lettura del valore dell'elemento

        parser.require(XmlPullParser.END_TAG, null, TAG_SITO_INTERNET); //Controllo del tag di chiusura

        return sitoInternet; //Viene restituito il valore letto
    }

    /**
     * Questo metodo si occupa di leggere il dato salvato all'interno di un elemento XML.
     * @param parser Oggetto per la lettura del file.
     * @return Viene restituita una stringa contenente l'informazione letta dal file.
     * @throws XmlPullParserException Può lanciare questa eccezione se si verificano problemi durante
     * la lettura del file.
     * @throws IOException Può lanciare questa eccezione se si verificano problemi durante la lettura
     * del file.
     */
    private String letturaDato(XmlPullParser parser)
    throws XmlPullParserException, IOException {
        /*Dichiarazione e inizializzazione del valore da leggere e restituire*/
        String contenutoElemento = null;

        if (parser.next() == XmlPullParser.TEXT) { //Si verifica che l'elemento contenga del testo
            contenutoElemento = parser.getText();  //Si legge il valore del tag corrente
            parser.nextTag();                      //Si passa alla lettura del tag successivo
        }

        return contenutoElemento; //Viene restituito il valore letto
    }
}
