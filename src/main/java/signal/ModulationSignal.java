package signal;

/**
 * Signal de modulation
 */
public class ModulationSignal extends AbstractSignal{

    public ModulationSignal(double amplitude, int frequency){
        super( amplitude, frequency, -10, 10);
    }

    public ModulationSignal(){
        super(-10, 10);
    }


}
