package controller;

import module.Keyboard;

/**
 * Subject pour le module de sortie
 */
public interface SubjectKeyboard extends Subject {

    void receiveNote(Keyboard.Note note);

    void receiveOctave(int octave);

}
