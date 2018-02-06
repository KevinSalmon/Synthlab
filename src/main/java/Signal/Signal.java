package Signal;

public interface Signal {

    /**
     * Ajoute une fréquence à la fréquence existante
     * @param frequency la fréquence à ajouter
     */
    void modifyFrequency(double frequency);

    /**
     * Ajoute une amplitude à l'amplitude existante
     * @param amplitude l'amplitude à ajouter
     */
    void modifyAmplitude(double amplitude);

    /**
     * Met à jour la fréquence
     * @param frequency
     */
    void setFrequency(double frequency);

    /**
     * Met à jour l'amplitude
     * @param amplitude
     */
    void setAmplitude(double amplitude);

    /**
     * Récupère l'amplitude
     * @return l'amplitude
     */
    double getAmplitude();

    /**
     * Récupère la fréquence
     * @return la fréquence
     */
    double getFrequency();

    /**
     * Récupère la bande passante
     * @return la bande passante
     */
    int getBandWidth();

    void setVoltRange(double min, double max);

    double getVolt();
}
