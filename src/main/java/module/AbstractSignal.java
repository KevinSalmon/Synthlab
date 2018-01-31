package module;

/**
 * Classe abstraite d'un signal
 */
public abstract class AbstractSignal implements Signal {
    protected static final int  BANDWIDTH = 22000;
    protected double volt;
    protected double amplitude;
    protected int frequencyMin;
    protected int frequencyMax;
    protected int octave;
    protected float reglageFin;

    public AbstractSignal(double volt, double amplitude, int frequency) {
        this.volt = volt;
        this.amplitude = amplitude;
        this.frequencyMin = frequency;
        this.frequencyMax = frequencyMin + BANDWIDTH;
        this.octave = 0;
        this.reglageFin = 0;
    }
    public AbstractSignal() {
        this.volt =0;
        this.amplitude=0.5;
        this.frequencyMin = 440;
        this.frequencyMax = frequencyMin + BANDWIDTH;
        this.octave = 0;
        this.reglageFin = 0;
    }
    @Override
    public double getVolt(){
        return volt;
    }

    @Override
    public double getAmplitude(){
        return amplitude;
    }

    @Override
    public int getFrequencyMin(){
        return frequencyMin;
    }

    @Override
    public int getFrequencyMax(){
        return frequencyMax;
    }

    @Override
    public void addFrequency(double frequency) {
        this.frequencyMin += frequency;
    }

    @Override
    public void setOctave(int i) {
        octave+=i;
        if(octave < -3) octave = -3;
        if(octave > 2) octave = 2;


    }

    @Override
    public int getBandWidth(){
        return BANDWIDTH;
    }

    @Override
    public int getOctave() {
        return octave;
    }

    @Override
    public float getReglageFin() {
        return reglageFin;
    }

    @Override
    public void setReglageFin(float reglageFin) {
        if(this.reglageFin < -1) this.reglageFin = -1;
        if(this.reglageFin > 1) this.reglageFin = 1;
        else
            this.reglageFin = reglageFin;
    }
}