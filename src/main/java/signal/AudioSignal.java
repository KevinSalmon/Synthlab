package signal;

/**
 * Signal audio
 */
public class AudioSignal extends AbstractSignal {

    public AudioSignal(double amplitude, int frequency){
        super( amplitude, frequency, -5, 5);
    }

    public AudioSignal(){
        super(-5, 5);
    }


}
