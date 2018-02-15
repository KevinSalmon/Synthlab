package ihm.observer;

public interface SubjectEG extends Subject {

    /**
     * retourne la valeur de l'attack dans l'ihm
     * @return
     */
    double getAttackValue();

    /**
     * retourne la valeur du decay dans l'ihm
     * @return
     */
    double getDecayValue();

    /**
     * retourne la valeur du sustain dans l'ihm
     * @return
     */
    double getSustainValue();

    /**
     * retourne la valeur du release dans l'ihm
     * @return
     */
    double getReleaseValue();
}
