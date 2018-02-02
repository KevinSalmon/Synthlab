package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import utils.Tuple;
import Signal.Signal;

public class VCA extends Module implements UnitSource{

    private UnitInputPort input;
    private UnitInputPort am;
    private UnitOutputPort output;
    private UnitOscillator currentOsc;

    private Signal audioSignal;

    public VCA(){
        this.input = null;
        addPort(this.input, "input");
        this.am = null;
        addPort(this.am, "am");
        this.output = null;
        addPort(this.output, "output");
        this.currentOsc = null;
        this.audioSignal = null;
    }

    /**
     * Getters et Setters
     */

    public UnitInputPort getInput() {
        return input;
    }

    public void setInput(UnitInputPort input) {
        this.input = input;
    }

    public UnitInputPort getAm() {
        return am;
    }

    public void setAm(UnitInputPort am) {
        this.am = am;
    }

    public UnitOscillator getCurrentOsc() {
        return currentOsc;
    }

    public void setCurrentOsc(UnitOscillator currentOsc) {
        this.currentOsc = currentOsc;
    }

    public Signal getAudioSignal() {
        return audioSignal;
    }

    public void setAudioSignal(Signal audioSignal) {
        this.audioSignal = audioSignal;
    }

    @Override
    public UnitOutputPort getOutput() {
        return output;
    }

    public void setOutput(UnitOutputPort output) {
        this.output = output;
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name == "output") return new Tuple(getPortByName(name),PortType.OUTPUT);
        if(name == "input") return new Tuple(getPortByName(name),PortType.INPUT);
        if(name == "am") return new Tuple(getPortByName(name),PortType.INPUT);
        return null;
    }
}
