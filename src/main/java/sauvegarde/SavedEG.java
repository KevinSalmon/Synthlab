package sauvegarde;

public class SavedEG extends SavedModule{

    private double attack;
    private double decay;
    private double sustain;
    private double release;

    public SavedEG() {
    }

    public SavedEG(double xPos, double yPos, double attack, double decay, double sustain, double release) {
        super(xPos, yPos);
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;
    }

    public double getSustain() {
        return sustain;
    }

    public void setSustain(double sustain) {
        this.sustain = sustain;
    }

    public double getRelease() {
        return release;
    }

    public void setRelease(double release) {
        this.release = release;
    }
}
