package ihm.observer;

public interface SubjectEG extends Subject {

    /**
     * retourne la valeur de l'attack dans l'ihm
     * @return la valeur d'attack
     */
    double getAttackValue();

    /**
     * retourne la valeur du decay dans l'ihm
     * @return la valeur de decay
     */
    double getDecayValue();

    /**
     * retourne la valeur du sustain dans l'ihm
     * @return la valeur du sustain
     */
    double getSustainValue();

    /**
     * retourne la valeur du release dans l'ihm
     * @return la valeur de release
     */
    double getReleaseValue();
}
