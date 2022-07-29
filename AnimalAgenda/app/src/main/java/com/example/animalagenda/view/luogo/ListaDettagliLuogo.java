package com.example.animalagenda.view.luogo; //Viene definito il package per le classi dell'applicazione

/*Vengono importate le classi di Android*/
import android.widget.ListView;   //Viene importata la classe estesa dalla vista personalizzata
import android.content.Context;   //Viene importata la classe per il contesto di esecuzione
import android.util.AttributeSet; //Viene importata la classe per impostare gli attributi
import android.view.ViewGroup;    //Viene importata la classe per le dimensioni della vista

/**
 * Questa classe permette di creare una ListView personalizzata per mostrare i dettagli per la
 * selezione del luogo nel bottom sheet.
 */
public class ListaDettagliLuogo extends ListView {

    /*Definizione costruttori*/

    /**
     * Costruttore che richiede solo il contesto dell'applicazione.
     * @param context Oggetto di tipo Context con le informazioni sull'activity che chiama la vista.
     */
    public ListaDettagliLuogo(Context context) {
        super(context); //Viene richiamato il costruttore della superclasse
    }

    /**
     * Costruttore per impostare degli attributi a scelta.
     * @param context Oggetto di tipo Context con le informazioni sull'activity che chiama la vista.
     * @param attrs   Oggetto di tipo AttributeSet con gli attributi da impostare.
     */
    public ListaDettagliLuogo(Context context, AttributeSet attrs) {
        super(context, attrs); //Viene richiamato il costruttore della superclasse
    }

    /**
     * Costruttore per la definizione dello stile della vista.
     * @param context      Oggetto di tipo Context con le informazioni sull'activity che chiama la
     *                     vista.
     * @param attrs        Oggetto di tipo AttributeSet con gli attributi da impostare.
     * @param defStyleAttr Intero identificativo dello stile da attribuire alla vista.
     */
    public ListaDettagliLuogo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); //Viene richiamato il costruttore della superclasse
    }

    /*Definizione metodi*/

    /**
     * Override del metodo che determina le dimensioni su schermo della vista.
     * @param widthMeasureSpec  Intero contenente la larghezza specificata.
     * @param heightMeasureSpec Intero contenente l'altezza specificata.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*Viene determinata l'altezza da attribuire alla vista*/
        int altezza =
                getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT ?
                        MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST) :
                        heightMeasureSpec;

        /*Viene richiamato il metodo corrispondente della superclasse*/
        super.onMeasure(widthMeasureSpec, altezza);
    }
}