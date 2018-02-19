package ihm.observer;

/**
 * Subject pour le module de sortie
 */
public interface SubjectOutput extends Subject {
    /**
     * retourne la valeur de mute dans l'ihm
     * @return la valeur de mute
     */
    boolean getMuteValue();

    /**
     * retourne la valeur de décibel dans l'ihm
     * @return la valeur de décibels
     */
    double getDecibelValue();

    /**
     * retourne la valeur de la checkbox record dans l'ihm
     * @return la valeur de record
     */
    boolean getRecordEnabled();

    /**
     * retourne le nom du fichier spécifié pour l'enregistrement dans l'ihm
     * @return le nom du fichier
     */
    String getRecordFilename();
}
