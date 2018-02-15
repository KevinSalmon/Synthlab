package controller;

public interface SubjectVCF extends Subject{
    double getFrequency();
    double getResonance();

    void receiveFrequency(double frequency);
}
