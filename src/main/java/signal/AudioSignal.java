package signal;

/**
 * Signal audio
 */
public class AudioSignal extends AbstractSignal {


    public AudioSignal(double amplitude, int frequency, double minVolt, double maxVolt){
        super( amplitude, frequency, minVolt, maxVolt);
    }

    public AudioSignal(double amplitude, int frequency){
        super( amplitude, frequency, -10, 10);
    }

    public AudioSignal(){
        super();
    }


}
