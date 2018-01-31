package module;

public interface Signal {

    /** Ajoute un voltage au voltage existant
     *
     * @param voltage Le voltage à ajouter
     */
    void addVoltage(double voltage);

    /**
     * Récupère le voltage
     * @return le voltage
     */
    double getVolt();

    /**
     * Ajoute une fréquence à la fréquence existante
     * @param frequency la fréquence à ajouter
     */
    void addFrequency(double frequency);


    /**
     * Récupère l'amplitude
     * @return l'amplitude
     */
    double getAmplitude();

    /**
     * Récupère la fréquence minimale
     * @return la fréquence minimale
     */
    int getFrequencyMin();

    /**
     * Récupère la fréquence maximale
     * @return la fréquence maximale
     */
    int getFrequencyMax();

    /**
     * Récupère la bande passante
     * @return la bande passante
     */
    int getBandWidth();

    /**
     * Récupère l'octave
     * @return l'octave actuelle
     */

    int getOctave();

    /**
     * Change l'octave actuelle de i octave
     * @param octave le nombre d'octave à augmenter
     */
    void setOctave(int octave);

    /**
     * Récupère le réglage fin
     * @return le réglage fin
     */
    float getReglageFin();

    /**
     * Met à jour le réglage fin
     * @param reglageFin le réglage à ajouter
     */
    void setReglageFin(float reglageFin);

}