package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.FilterLowPass;
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
    private double f0;


    public VCFLP(){

        this.in = new UnitInputPort(PortType.INPUT.getType());
        this.out = new UnitOutputPort(PortType.OUTPUT.getType());
        this.filterLowPass = new FilterLowPass();
        this.fm = new UnitInputPort(PortType.FM.getType());
        this.filterLowPass.input = this.in;
        this.filterLowPass.output = this.out;
        this.signal = new ModulationSignal();
        this.filterLowPass.frequency.set(Math.pow(2, signal.getVolt()) * signal.getFrequency());
        addPort(this.fm);
        addPort(this.in);
        addPort(this.out);
        add(filterLowPass);
        f0 = 0;
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
        return out;
    }

    public UnitInputPort getInput() { return in;}

    public UnitInputPort getFm() {
        return this.fm;
    }

    /**
     * Mise à jour du VCFLP en fonction de l'ihm
     * @param o le sujet
     */
    @Override
    public void update(SubjectVCFLP o) {
        f0 = o.getFrequency();
        if(o.getResonance() != filterLowPass.Q.get()){
            filterLowPass.Q.set(o.getResonance());
        }
    }

    /**
     * Genere un signal de sortie
     * équivalent au signal d'entrée lorsque la fréquence est infinie (filtre totalement ouvert)
     * nul lorsque la fréquence est nulle
     * doublé ou divisé par deux sinon
     */
    @Override
    public void generate(int start, int limit){
        super.generate(start, limit);
        double [] freqValues = filterLowPass.frequency.getValues();
        double [] fmValues = fm.getValues();
        for(int i = start; i < limit; i++){
            freqValues[i] = Math.pow(2, f0 + fmValues[i]) * signal.getFrequency();

        }
    }

    @Override
    public Module getReference() {
        return this;
    }

    public FilterLowPass getFilterLowPass() {
        return filterLowPass;
    }


    public Double getResonance(){
        return filterLowPass.Q.get();
    }

    public double getFrequency() {
        return filterLowPass.frequency.get();
    }

    public void setResonance(double resonance) {
        this.filterLowPass.Q.set(resonance);
    }
}
