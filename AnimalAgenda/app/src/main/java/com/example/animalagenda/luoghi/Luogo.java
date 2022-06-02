package com.example.animalagenda.luoghi; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi ausiliarie Java*/
import java.io.Serializable; //Viene importata l'interfaccia per rendere l'oggetto Serializable
import java.util.ArrayList;  //Viene importata la classe per la struttura dati ArrayList

/*Vengono importate le classi di Android*/
import androidx.annotation.NonNull; //Viene importata la classe per l'annotazione degli oggetti che non possono essere nulli
import android.location.Address;    //Viene importata la classe per gli indirizzi geografici

/*Vengono importate le classi per la gestione della vista della mappa*/
import org.osmdroid.util.GeoPoint; //Viene importata la classe per i punti geografici

/**
 * Lo scopo di questa classe è quello di contenere le informazioni associate a un luogo.
 * Gli oggetti di questo tipo sono immutabili.
 */
public class Luogo implements Serializable {
    /*Dichiarazione delle variabili d'istanza*/
    private final GeoPoint coordinate; //Dichiarazione della proprietà per le coordinate geografiche
    private final String indirizzo;    //Dichiarazione della proprietà per l'indirizzo
    private final String nome;         //Dichiarazione della proprietà per il nome
    private final String telefono;     //Dichiarazione della proprietà per il numero di telefono
    private final String email;        //Dichiarazione della proprietà per l'indirizzo e-mail
    private final String sitoInternet; //Dichiarazione della proprietà per il sito Internet

    /*Definizione del costruttore*/

    /**
     * Costruttore per salvare i dati associati a un nuovo luogo.
     * @param coordinate   Oggetto di tipo GeoPoint contenente le coordinate geografiche.
     * @param indirizzo    Stringa rappresentante l'indirizzo associato alla posizione.
     * @param nome         Stringa contenente il nome del luogo.
     * @param telefono     Stringa rappresentante il numero di telefono associato al luogo.
     * @param email        Stringa rappresentante l'indirizzo e-mail.
     * @param sitoInternet Stringa rappresentante il sito Internet.
     */
    public Luogo(@NonNull GeoPoint coordinate,
                 String indirizzo, String nome, String telefono, String email, String sitoInternet) {
        this.coordinate = coordinate;     //Inizializzazione proprietà
        this.indirizzo = indirizzo;       //Inizializzazione proprietà
        this.nome = nome;                 //Inizializzazione proprietà
        this.telefono = telefono;         //Inizializzazione proprietà
        this.email = email;               //Inizializzazione proprietà
        this.sitoInternet = sitoInternet; //Inizializzazione proprietà
    }

    /*Definizione dei metodi*/

    /**
     * Metodo per ottenere le coordinate geografiche del luogo.
     * @return Viene restituito un oggetto di tipo GeoPoint contenente le coordinate del luogo.
     */
    public GeoPoint getCoordinate() {
        return this.coordinate; //Viene restituito il valore della proprietà
    }

    /**
     * Metodo per ottenere l'indirizzo associato alle coordinate geografiche.
     * @return Viene restituita una stringa rappresentante l'indirizzo.
     */
    public String getIndirizzo() {
        return this.indirizzo; //Viene restituito il valore della proprietà
    }

    /**
     * Metodo per ottenere il nome associato al luogo.
     * @return Viene restituita una stringa rappresentante il nome.
     */
    public String getNome() {
        return this.nome; //Viene restituito il valore della proprietà
    }

    /**
     * Metodo per ottenere il numero di telefono associato.
     * @return Viene restituita una stringa rappresentante il numero di telefono.
     */
    public String getTelefono() {
        return this.telefono; //Viene restituito il valore della proprietà
    }

    /**
     * Metodo per ottenere l'indirizzo e-mail associato.
     * @return Viene restituita una stringa rappresentante l'indirizzo e-mail.
     */
    public String getEmail() {
        return this.email; //Viene restituito il valore della proprietà
    }

    /**
     * Metodo per ottenere il sito Internet associato al luogo.
     * @return Viene restituita una stringa rappresentante il sito Internet.
     */
    public String getSitoInternet() {
        return this.sitoInternet; //Viene restituito il valore della proprietà
    }

    /**
     * Override del metodo che si occupa del confronto tra due oggetti di tipo Luogo.
     * @param obj Oggetto da confrontare.
     * @return Viene restituito true se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        Luogo tmp = (Luogo) obj; //Cast esplicito per il controllo

        if (!(obj instanceof Luogo)) //Si controlla il tipo del parametro in ingresso
            /*Viene restituito un valore negativo per segnalare che il parametro in ingresso è errato*/
            return false;

        /*Viene restituito il risultato del controllo*/
        return this.coordinate.equals(tmp.getCoordinate())    &&
               this.indirizzo.equals(tmp.getIndirizzo())      &&
               this.nome.equals(tmp.getNome())                &&
               this.telefono.equals(tmp.getTelefono())        &&
               this.email.equals(tmp.getEmail())              &&
               this.sitoInternet.equals(tmp.getSitoInternet());
    }

    /**
     * Override del metodo che genera il codice hash dell'oggetto istanziato.
     * @return Viene restituito un intero rappresentante il codice hash dell'oggetto.
     */
    @Override
    public int hashCode() {
        final int NUMERO_PRIMO = 31; //Definizione del numero primo per la generazione del codice hash
        int hash = 7;                //Dichiarazione e inizializzazione del codice hash da restituire

        /*Creazione del codice hash da restituire*/
        hash = NUMERO_PRIMO * hash + this.coordinate.hashCode();
        hash = NUMERO_PRIMO * hash + (this.indirizzo == null ? 0 : this.indirizzo.hashCode());
        hash = NUMERO_PRIMO * hash + (this.nome == null ? 0 : this.nome.hashCode());
        hash = NUMERO_PRIMO * hash + (this.telefono == null ? 0 : this.telefono.hashCode());
        hash = NUMERO_PRIMO * hash + (this.email == null ? 0 : this.email.hashCode());
        hash = NUMERO_PRIMO * hash + (this.sitoInternet == null ? 0 : this.sitoInternet.hashCode());

        return hash; //Viene restituito il codice hash generato
    }

    /**
     * Override del metodo per ottenere la stringa associata all'oggetto istanziato.
     * @return Viene restituita una stringa che contiene tutti i dati del luogo.
     */
    @Override
    public String toString() {
        /*Viene restituita una rappresentazione dell'oggetto sotto forma di testo comprensibile*/
        return "Latitudine: " + this.coordinate.getLatitude() + " " +
               "Longitudine: " + this.coordinate.getLongitude() + " " +
               "Indirizzo: " + (this.indirizzo == null ? "" : this.indirizzo) + " " +
               "Nome: " + (this.nome == null ? "" : this.nome) + " " +
               "Numero di telefono: " + (this.telefono == null ? "" : this.telefono) + " " +
               "Indirizzo e-mail: " + (this.email == null ? "" : this.email) + " " +
               "Sito Internet: " + (this.sitoInternet == null ? "" : this.sitoInternet);
    }

    /**
     * Questo metodo si occupa di comparare due luoghi prendendo in esame solamente le loro
     * coordinate geografiche.
     * @param punto Posizione geografica da comparare.
     * @return Restituisce true se entrambe le posizioni hanno coordinate uguali, false altrimenti.
     */
    public boolean comparaCoordinate(GeoPoint punto) {
        return this.coordinate.equals(punto); //Viene restituito il risultato del controllo
    }

    /**
     * Questo metodo si occupa di controllare se un dato testuale è valido.
     * @param dato Stringa rappresentante il dato da controllare.
     * @return Viene restituito false se la stringa è nulla, vuota o formata da soli spazi.
     * Viene restituito true altrimenti.
     */
    private boolean datoTestualeValido(String dato) {
        /*Si controlla che la stringa sia nulla, vuota o formata da soli spazi*/
        return dato != null && !dato.isEmpty() && !dato.trim().isEmpty();
    }

    /**
     * Questo metodo controlla la validità dell'indirizzo associato al luogo.
     * @return Restituisce true se l'indirizzo è corretto, false altrimenti.
     */
    public boolean indirizzoValido() {
        return datoTestualeValido(this.indirizzo); //Si restituisce il risultato del controllo
    }

    /**
     * Questo metodo controlla la validità del nome associato al luogo.
     * @return Restituisce true se il nome è corretto, false altrimenti.
     */
    public boolean nomeValido() {
        return datoTestualeValido(this.nome); //Si restituisce il risultato del controllo
    }

    /**
     * Questo metodo controlla la validità del numero di telefono associato al luogo.
     * @return Restituisce true se il numero di telefono è corretto, false altrimenti.
     */
    public boolean telefonoValido() {
        return datoTestualeValido(this.telefono); //Si restituisce il risultato del controllo
    }

    /**
     * Questo metodo controlla la validità dell'indirizzo e-mail associato al luogo.
     * @return Restituisce true se l'indirizzo e-mail è corretto, false altrimenti.
     */
    public boolean emailValida() {
        return datoTestualeValido(this.email); //Si restituisce il risultato del controllo
    }

    /**
     * Questo metodo controlla la validità del sito Internet associato al luogo.
     * @return Restituisce true se il sito Internet è corretto, false altrimenti.
     */
    public boolean sitoInternetValido() {
        return datoTestualeValido(this.sitoInternet); //Si restituisce il risultato del controllo
    }

    /**
     * Questo metodo crea una lista di dati che può essere utilizzata durante la stampa per l'utente.
     * Questa lista non comprende il nome del luogo e le coordinate geografiche.
     * @return Viene restituita una struttura dati di tipo ArrayList contenente i dati non vuoti
     * del luogo.
     */
    public ArrayList<DatoLuogo> listaDati() {
        /*Dichiarazione e inizializzazione della lista di dati da restituire*/
        ArrayList<DatoLuogo> lista = new ArrayList<>();

        if (indirizzoValido()) //Controllo della validità del dato
            /*L'indirizzo associato al luogo viene aggiunto alla lista di dati*/
            lista.add(new DatoLuogo(this.indirizzo, TipoDatoLuogo.INDIRIZZO));

        if (telefonoValido()) //Controllo della validità del dato
            /*Il numero di telefono associato al luogo viene aggiunto alla lista di dati*/
            lista.add(new DatoLuogo(this.telefono, TipoDatoLuogo.TELEFONO));

        if (emailValida()) //Controllo della validità del dato
            /*L'indirizzo e-mail associato al luogo viene aggiunto alla lista di dati*/
            lista.add(new DatoLuogo(this.email, TipoDatoLuogo.EMAIL));

        if (sitoInternetValido()) //Controllo della validità del dato
            /*Il sito Internet associato al luogo viene aggiunto alla lista di dati*/
            lista.add(new DatoLuogo(this.sitoInternet, TipoDatoLuogo.SITO_INTERNET));

        return lista; //Viene restituita una lista di soli dati non vuoti
    }

    /**
     * Questo metodo si occupa della costruzione dell'indirizzo a partire dai dati geografici grezzi.
     * @param datiMappa Dati geografici ottenuti dalle API.
     * @return Se valido, viene restituito un indirizzo coerente con quelli utilizzati nella
     * simulazione; altrimenti viene restituito un oggetto nullo.
     */
    public static String creazioneIndirizzo(Address datiMappa) {
        /*Dichiarazione e inizializzazione dell'oggetto ausiliario per la composizione
          dell'indirizzo*/
        String tmp = datiMappa == null ? null : datiMappa.getThoroughfare();

        String indirizzo = null; //Dichiarazione e inizializzazione dell'indirizzo da restituire

        if (tmp != null) {   //Si controlla la validità dell'indirizzo
            indirizzo = tmp; //Si costruisce la prima parte dell'indirizzo, corrispondente alla via

            tmp = datiMappa.getSubThoroughfare();   //Si recupera il numero della via
            if (tmp != null)                        //Si controlla la validità
                indirizzo = indirizzo + ", " + tmp; //Si costruisce la seconda parte

            tmp = datiMappa.getLocality();                    //Si recupera la città
            if (tmp != null)                                  //Si controlla la validità
                indirizzo = indirizzo + " (" + tmp + ")";     //Si costruisce la terza parte
            else {                                            //Si usa il codice postale in alternativa
                tmp = datiMappa.getPostalCode();              //Si recupera il codice postale
                if (tmp != null)                              //Si controlla la validità
                    indirizzo = indirizzo + " (" + tmp + ")"; //Si costruisce la terza parte
            }
        }

        return indirizzo; //Viene restituito il metodo creato
    }
}
