package ihm.observer;

import utils.OscillatorType;

/**
 * Subject pour le module VCO
 */
public interface SubjectVCO extends Subject {
    /**
     * retourne la valeur de l'octave dans l'ihm
     * @return la valeur d'octave
     */
    int getOctaveValue();

    /**
     * retourne la valeur du réglage fin dans l'ihm
     * @return la valeur de réglage fin
     */
    double getReglageFinValue();

    OscillatorType getOscillatorType();

    boolean isLFOActive();
}
