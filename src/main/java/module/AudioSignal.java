package module;

/**
 * Signal audio
 */
public class AudioSignal extends AbstractSignal {


    public AudioSignal(double volt, double amplitude, int frequency){
        super(volt, amplitude, frequency);
    }

    public AudioSignal(){
        super();
    }

    public void addVoltage(double voltage) {
        volt += voltage;
        if(volt > 5) volt = 5;
        if (volt < -5) volt = -5;

    }




}
