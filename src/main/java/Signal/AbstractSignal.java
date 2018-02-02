package Signal;

/**
 * Classe abstraite d'un signal
 */
public abstract class AbstractSignal implements Signal {
    protected static final int  BANDWIDTH = 22000;
    protected double amplitude;
    protected double frequency;

    public AbstractSignal(double amplitude, int frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }
    public AbstractSignal() {
        this.amplitude=0.5;
        this.frequency = 440;
    }

    @Override
    public void ModifyFrequency(double frequency) {
        setFrequency(this.frequency+frequency);
    }

    @Override
    public void ModifyAmplitude(double amplitude) {
        setAmplitude(this.amplitude+amplitude);
    }

    @Override
    public void setFrequency(double frequency) {
        if(frequency < 0) this.frequency = 0;
        else if(frequency > BANDWIDTH) this.frequency = BANDWIDTH;
        else this.frequency = frequency;
    }

    @Override
    public void setAmplitude(double amplitude) {
        if(amplitude < -1) this.amplitude = -1;
        else if(amplitude > 1) this.amplitude = 1;
        else this.amplitude = amplitude;
    }

    @Override
    public double getAmplitude() {
        return amplitude;
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    @Override
    public int getBandWidth(){
        return BANDWIDTH;
    }
}
