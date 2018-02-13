package sauvegarde;

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

    public double getResonance() {
        return resonance;
    }

    public double getF0() {
        return f0;
    }

    public boolean isLP() {
        return isLP;
    }
}
