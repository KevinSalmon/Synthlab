package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.UnitFilter;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectVCFLP;
import signal.ModulationSignal;
import signal.Signal;
import utils.PortType;
import utils.Tuple;


public class VCFLP extends Module implements UnitSource, Obseurveur<SubjectVCFLP>{

    private FilterLowPass filterLowPass;
    private UnitInputPort fm;
    private UnitInputPort in;
    private UnitOutputPort out;
    private Signal signal;
    private double freq;


    public VCFLP(){

        this.in = new UnitInputPort(PortType.INPUT.getType());
        this.out = new UnitOutputPort(PortType.OUTPUT.getType());
        this.filterLowPass = new FilterLowPass();
        this.fm = new UnitInputPort(PortType.FM.getType());
        this.filterLowPass.frequency = this.fm;
        this.filterLowPass.input = this.in;
        this.filterLowPass.output = this.out;
        addPort(this.in, PortType.INPUT.getType());
        addPort(this.out, PortType.OUTPUT.getType());
        addPort(this.fm, PortType.FM.getType());
        this.signal = new ModulationSignal();
        this.filterLowPass.frequency.set(Math.pow(2, signal.getVolt()) * signal.getFrequency());
        add(filterLowPass);
        this.freq = this.filterLowPass.frequency.get();
    }




    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.equals(PortType.OUTPUT.getType())) return new Tuple<>(getPortByName(name), PortType.OUTPUT);
        if(name.equals(PortType.INPUT.getType())) return new Tuple<>(getPortByName(name), PortType.INPUT);
        if(name.equals(PortType.FM.getType())) return new Tuple<>(getPortByName(name), PortType.FM);
        return null;
    }

    @Override
    public UnitOutputPort getOutput() {
        return this.out;
    }

    public UnitInputPort getInput() { return this.in;}

    public UnitInputPort getFm() {
        return this.fm;
    }

    /**
     * Mise à jour du VCFLP en fonction de l'ihm
     * @param o le sujet
     */
    @Override
    public void update(SubjectVCFLP o) {
        if(o.getFrequency() == 0){
            filterLowPass.frequency.set(0);
        }
        else if(o.getFrequency() == Double.MAX_VALUE){
            filterLowPass.frequency.set(Double.MAX_VALUE);
        }
        else {
            filterLowPass.frequency.set(o.getFrequency());
            filterLowPass.updateCoefficients();
            freq = Math.pow(2, signal.getVolt()) * filterLowPass.frequency.get();
        }
    }

    /**
     * Genere un signal de sortie
     * équivalent au signal d'entrée lorsque la fréquence est infinie (filtre totalement ouvert)
     * nul lorsque la fréquence est nulle
     * doublé ou divisé par deux sinon
     */
    @Override
    public void generate(){
        super.generate();
        double[] in = filterLowPass.input.getValues();
        double[] out = filterLowPass.output.getValues();
        if(filterLowPass.frequency.get() == Double.MAX_VALUE){
            System.arraycopy(in, 0, out, 0, out.length);
        }
        else if(filterLowPass.frequency.get() == 0.0){
            for(int i=0;i < out.length; i ++){
                out[i] = 0.0;
            }
        }
        else{
            filterLowPass.frequency.set(freq);
        }


    }

    @Override
    public Module getReference() {
        return this;
    }

    public FilterLowPass getFilterLowPass() {
        return filterLowPass;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

}
