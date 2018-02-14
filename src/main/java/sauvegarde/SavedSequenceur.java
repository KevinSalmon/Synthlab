package sauvegarde;

import module.Module;

public class SavedSequenceur extends SavedModule{

    private double sld1;
    private double sld2;
    private double sld3;
    private double sld4;
    private double sld5;
    private double sld6;
    private double sld7;
    private double sld8;

    public SavedSequenceur(double xPos, double yPos, double sld1, double sld2, double sld3, double sld4, double sld5, double sld6, double sld7, double sld8) {
        super(xPos, yPos);
        this.sld1 = sld1;
        this.sld2 = sld2;
        this.sld3 = sld3;
        this.sld4 = sld4;
        this.sld5 = sld5;
        this.sld6 = sld6;
        this.sld7 = sld7;
        this.sld8 = sld8;
    }

    public double getSld1() {
        return sld1;
    }

    public double getSld2() {
        return sld2;
    }

    public double getSld3() {
        return sld3;
    }

    public double getSld4() {
        return sld4;
    }

    public double getSld5() {
        return sld5;
    }

    public double getSld6() {
        return sld6;
    }

    public double getSld7() {
        return sld7;
    }

    public double getSld8() {
        return sld8;
    }

    public void setSld1(double sld1) {
        this.sld1 = sld1;
    }

    public void setSld2(double sld2) {
        this.sld2 = sld2;
    }

    public void setSld3(double sld3) {
        this.sld3 = sld3;
    }

    public void setSld4(double sld4) {
        this.sld4 = sld4;
    }

    public void setSld5(double sld5) {
        this.sld5 = sld5;
    }

    public void setSld6(double sld6) {
        this.sld6 = sld6;
    }

    public void setSld7(double sld7) {
        this.sld7 = sld7;
    }

    public void setSld8(double sld8) {
        this.sld8 = sld8;
    }
}
