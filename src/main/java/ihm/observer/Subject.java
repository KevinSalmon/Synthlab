package ihm.observer;

/**
 * Subject entre la partie graphique d'un module et son implémentation
 */
public interface Subject {
    /**
     * Permet à l'obseurveur o de s'abonner à ce Subject
     * @param o
     */
    void register(Obseurveur o);

    /**
     * Permet à l'obseurveur o de se désabonner de ce Subject
     * @param o
     */
    void remove(Obseurveur o);

    /**
     * Notifie tous les obseurveur abonnés à ce Subject
     */
    void notifyObseurveur();
}
