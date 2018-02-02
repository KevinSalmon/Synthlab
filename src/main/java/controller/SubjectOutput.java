package controller;

/**
 * Subject pour le module de sortie
 */
public interface SubjectOutput extends Subject<SubjectOutput>{
    /**
     * retourne la valeur de mute dans l'ihm
     * @return
     */
    boolean getMuteValue();

    /**
     * retourne la valeur de décibel dans l'ihm
     * @return
     */
    double getDecibelValue();
}
