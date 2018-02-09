package controller;

/**
 * Subject pour le module de sortie
 */
public interface SubjectOutput extends Subject {
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

    /**
     * @return retourne la valeur de la checkbox record dans l'ihm
     */
    boolean getRecordEnabled();

    /**
     * @return retourne le nom du fichier spécifié pour l'enregistrement dans l'ihm
     */
    String getRecordFilename();
}
