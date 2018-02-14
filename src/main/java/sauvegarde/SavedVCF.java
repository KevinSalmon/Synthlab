package sauvegarde;

import java.util.logging.Logger;

public class SavedVCF extends SavedModule{

    private double resonance;
    private double f0;
    private boolean isLP;

    public SavedVCF(double xPos, double yPos, double resonance, double f0, boolean isLP) {
        super(xPos, yPos);
        this.resonance = resonance;
        this.f0 = f0;
        this.isLP = isLP;
    }
    public SavedVCF(){

    }

    public double getResonance() {
        return resonance;
    }

    public double getF0() {
        return f0;
    }


    public void setResonance(double resonance) {
        this.resonance = resonance;
    }

    public void setF0(double f0) {
        this.f0 = f0;
    }

    public boolean getLP() {
        return isLP;
    }

    public void setLP(boolean LP) {
        isLP = LP;
    }
}
