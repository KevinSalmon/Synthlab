package sauvegarde;

public class SavedMix extends SavedModule{

    private double in1attenuation;
    private double in2attenuation;
    private double in3attenuation;
    private double in4attenuation;

    public SavedMix() {
    }

    public SavedMix(double xPos, double yPos, double in1attenuation, double in2attenuation, double in3attenuation, double in4attenuation) {
        super(xPos, yPos);
        this.in1attenuation = in1attenuation;
        this.in2attenuation = in2attenuation;
        this.in3attenuation = in3attenuation;
        this.in4attenuation = in4attenuation;
    }

    public double getIn1attenuation() {
        return in1attenuation;
    }

    public void setIn1attenuation(double in1attenuation) {
        this.in1attenuation = in1attenuation;
    }

    public double getIn2attenuation() {
        return in2attenuation;
    }

    public void setIn2attenuation(double in2attenuation) {
        this.in2attenuation = in2attenuation;
    }

    public double getIn3attenuation() {
        return in3attenuation;
    }

    public void setIn3attenuation(double in3attenuation) {
        this.in3attenuation = in3attenuation;
    }

    public double getIn4attenuation() {
        return in4attenuation;
    }

    public void setIn4attenuation(double in4attenuation) {
        this.in4attenuation = in4attenuation;
    }
}
