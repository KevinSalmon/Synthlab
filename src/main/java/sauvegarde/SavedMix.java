package sauvegarde;

public class SavedMix extends SavedModule{

    private double in1dbAttenuation;
    private double in2dbAttenuation;
    private double in3dbAttenuation;
    private double in4dbAttenuation;

    public SavedMix() {
    }

    public SavedMix(double xPos, double yPos, double in1dbAttenuation, double in2dbAttenuation, double in3dbAttenuation, double in4dbAttenuation) {
        super(xPos, yPos);
        this.in1dbAttenuation = in1dbAttenuation;
        this.in2dbAttenuation = in2dbAttenuation;
        this.in3dbAttenuation = in3dbAttenuation;
        this.in4dbAttenuation = in4dbAttenuation;
    }

    public double getIn1dbAttenuation() {
        return in1dbAttenuation;
    }

    public void setIn1dbAttenuation(double in1dbAttenuation) {
        this.in1dbAttenuation = in1dbAttenuation;
    }

    public double getIn2dbAttenuation() {
        return in2dbAttenuation;
    }

    public void setIn2dbAttenuation(double in2dbAttenuation) {
        this.in2dbAttenuation = in2dbAttenuation;
    }

    public double getIn3dbAttenuation() {
        return in3dbAttenuation;
    }

    public void setIn3dbAttenuation(double in3dbAttenuation) {
        this.in3dbAttenuation = in3dbAttenuation;
    }

    public double getIn4dbAttenuation() {
        return in4dbAttenuation;
    }

    public void setIn4dbAttenuation(double in4dbAttenuation) {
        this.in4dbAttenuation = in4dbAttenuation;
    }
}
