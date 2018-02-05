package controller;

import utils.OscillatorType;

/**
 * Subject pour le module VCO
 */
public interface SubjectVCO extends Subject {
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

    double getFrequency();

    OscillatorType getOscillatorType();
}
