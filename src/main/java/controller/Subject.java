package controller;

public interface Subject<T> {

    void register(Obseurveur o);
    void remove(Obseurveur o);
    void notifyObseurveur();
}
