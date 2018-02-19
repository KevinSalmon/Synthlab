package ihm.observer;

import module.Module;

/**
 * Observer entre la partie graphique d'un module et son implémentation
 * @param <T> type du Subject
 */
public interface Obseurveur<T> {
    /**
     * Méthode appelée pour mettre à jour l'obseurveur
     * @param o l'obseurveur à mettre à jour
     */
    void update(T o);

    Module getReference();
}
