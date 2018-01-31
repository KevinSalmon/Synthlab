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

    public AbstractSignal(double volt, double amplitude, int frequency) {
        this.volt = volt;
        this.amplitude = amplitude;
        this.frequencyMin = frequency;
        this.frequencyMax = frequencyMin + BANDWIDTH;
    }
    public AbstractSignal() {
        this.volt =0;
        this.amplitude=0.5;
        this.frequencyMin = 440;
        this.frequencyMax = frequencyMin + BANDWIDTH;
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
    public void changeOctave(int i) {
        this.frequencyMin = (int) (this.frequencyMin * Math.pow(2, i));

    }

    @Override
    public int getBandWidth(){
        return BANDWIDTH;
    }

}
