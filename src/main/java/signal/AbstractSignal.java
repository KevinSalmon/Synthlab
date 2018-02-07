package signal;

/**
 * Classe abstraite d'un signal
 */
public abstract class AbstractSignal implements Signal {
    protected static final int BANDWIDTH = 22000;
    protected double amplitude;
    protected double frequency;
    protected double minVolt;
    protected double maxVolt;

    public AbstractSignal(double amplitude, int frequency, double minVolt, double maxVolt) {
        // TODO: Contrôler que l'amplitude et frequency sont ok
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.minVolt = minVolt;
        this.maxVolt = maxVolt;
    }
    public AbstractSignal() {
        this.amplitude=0.5;
        this.frequency = 440;
        this.minVolt = -5;
        this.maxVolt = 5;
    }

    @Override
    public void modifyFrequency(double frequency) {
        setFrequency(this.frequency+frequency);
    }

    @Override
    public void modifyAmplitude(double amplitude) {
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

    @Override
    public void setVoltRange(double min, double max) {
        this.minVolt = min;
        this.maxVolt = max;
    }

    @Override
    public double getVolt() {
        return ((this.maxVolt - this.minVolt) / 2) * this.amplitude;
    }
}
