package module;

/**
 * Signal de modulation
 */
public class ModulationSignal extends AbstractSignal{


    public ModulationSignal(double volt, double amplitude, int frequency){
        super(volt, amplitude, frequency);
    }

    public ModulationSignal(){
        super();
    }
    @Override
    public void addVoltage(double voltage) {
        volt += voltage;
        if(volt > 10) volt=10;
        if(volt < -10) volt=-10;

    }


}
