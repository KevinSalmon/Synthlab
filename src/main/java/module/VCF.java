package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.FilterBiquadCommon;
import com.jsyn.unitgen.FilterHighPass;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectVCF;
import signal.ModulationSignal;
import signal.Signal;
import utils.PortType;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;


public class VCF extends Module implements UnitSource, Obseurveur<SubjectVCF>{

    private boolean isLowPass;
    private FilterBiquadCommon filterPass;
    private UnitInputPort fm;
    private UnitInputPort in;
    private UnitOutputPort out;
    private Signal signal;
    private double f0;


    public VCF(boolean initAsLowPass){
        this.isLowPass = initAsLowPass;

        if(isLowPass){
            this.filterPass = new FilterLowPass();
        }else{
            this.filterPass = new FilterHighPass();
        }

        this.in = new UnitInputPort(PortType.INPUT.getType());
        this.out = new UnitOutputPort(PortType.OUTPUT.getType());

        this.fm = new UnitInputPort(PortType.INPUTFM.getType());
        this.filterPass.input = this.in;
        this.filterPass.frequency.set(440);
        this.filterPass.output = this.out;
        this.signal = new ModulationSignal();
        addPort(this.fm);
        addPort(this.in);
        addPort(this.out);
        add(filterPass);
        f0 = 0;
    }


    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.equals(PortType.OUTPUT.getType())) return new Tuple<>(getPortByName(name), PortType.OUTPUT);
        if(name.equals(PortType.INPUT.getType())) return new Tuple<>(getPortByName(name), PortType.INPUT);
        if(name.equals(PortType.INPUTFM.getType())) return new Tuple<>(getPortByName(name), PortType.INPUTFM);
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
    public void update(SubjectVCF o) {
        f0 = o.getFrequency();
        if(o.getResonance() != filterPass.Q.get()){
            filterPass.Q.set(o.getResonance());
        }
//        o.receiveFrequency(getFrequency());
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
        double [] freqValues = filterPass.frequency.getValues();
        double [] fmValues = fm.getValues();
        for(int i = start; i < limit; i++){
            freqValues[i] = Math.pow(2, f0 + fmValues[i]) * signal.getFrequency();
        }

    }


    @Override
    public Module getReference() {
        return this;
    }

    public FilterBiquadCommon getFilterPass() {
        return filterPass;
    }


    public Double getResonance(){
        return filterPass.Q.get();
    }

    public double getFrequency() {
        return filterPass.frequency.getValues()[0];
    }

    public void setResonance(double resonance) {
        this.filterPass.Q.set(resonance);
    }

    public void setFrequency(Integer frequency) {
        filterPass.frequency.set(frequency);
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUT);
        list.add(PortType.INPUTFM);
        list.add(PortType.OUTPUT);
        return list;
    }
}
