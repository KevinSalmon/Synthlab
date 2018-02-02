package controller;

/**
 * Subject pour le module VCO
 */
public interface SubjectVCO extends Subject<SubjectOutput>{
    /**
     * retourne la valeur de l'octave dans l'ihm
     * @return
     */
    int getOctaveValue();

    /**
     * retourne la valeur du réglage fin dans l'ihm
     * @return
     */
    double getReglageFinValue();
}
