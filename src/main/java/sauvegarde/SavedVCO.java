package sauvegarde;

import utils.OscillatorType;

public class SavedVCO extends SavedModule{

    private double octave;
    private double reglageFin;
    private OscillatorType typeOnde;
    private boolean isLFO;

    public SavedVCO() {
    }

    public SavedVCO(double xPos, double yPos, double octave, double reglageFin, OscillatorType typeOnde, boolean isLFO) {
        super(xPos, yPos);
        this.octave = octave;
        this.reglageFin = reglageFin;
        this.typeOnde = typeOnde;
        this.isLFO = isLFO;
    }

    public double getOctave() {
        return octave;
    }

    public double getReglageFin() {
        return reglageFin;
    }

    public OscillatorType getTypeOnde() {
        return typeOnde;
    }

    public boolean isLFO() {
        return isLFO;
    }

    public void setOctave(double octave) {
        this.octave = octave;
    }

    public void setReglageFin(double reglageFin) {
        this.reglageFin = reglageFin;
    }

    public void setTypeOnde(OscillatorType typeOnde) {
        this.typeOnde = typeOnde;
    }

    public void setLFO(boolean Lfo) {
        isLFO = Lfo;
    }
}
