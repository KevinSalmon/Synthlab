package signal;

/**
 * Signal de modulation
 */
public class ModulationSignal extends AbstractSignal{


    public ModulationSignal(double amplitude, int frequency, double minVolt, double maxVolt){
        super( amplitude, frequency, minVolt, maxVolt);
    }

    public ModulationSignal(){
        super();
    }


}
