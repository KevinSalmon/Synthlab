package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import utils.Tuple;

public class VCO extends Circuit implements UnitSource, Module {

    private SquareOscillator sqrOsc;
    private UnitOscillator currentOsc;
    private UnitOutputPort output;
    private UnitInputPort input;
    private Signal audioSignal;


    public VCO() {
        add(sqrOsc = new SquareOscillator());
        currentOsc = sqrOsc;

        addPort(output = sqrOsc.output, "output");
        audioSignal = new AudioSignal(1.5, 0.5, 440);
        sqrOsc.frequency.set((440) * Math.pow(2, (audioSignal.getOctave() + audioSignal.getReglageFin()))); //1 kHz
    }

    @Override
    public UnitOutputPort getOutput() {
        return output;
    }

    
    public void increaseFrequency(float v){
        audioSignal.setReglageFin(v);
        sqrOsc.frequency.set((440) *(audioSignal.getOctave() + audioSignal.getReglageFin()));

    }

    public void increaseOctave(int amp) {
        audioSignal.setOctave(amp);
        sqrOsc.frequency.set(440 *(audioSignal.getOctave() + audioSignal.getReglageFin()));
    }

    public void decreaseFrequency(float v){
        audioSignal.setReglageFin(-v);
        sqrOsc.frequency.set((440) /(audioSignal.getOctave() + audioSignal.getReglageFin()));

    }

    public void decreaseOctave(int amp) {
        audioSignal.setOctave(-amp);
        sqrOsc.frequency.set(440 /(audioSignal.getOctave() + audioSignal.getReglageFin()));
    }

    public SquareOscillator getSqrOsc() {
        return sqrOsc;
    }

    public void setSqrOsc(SquareOscillator sqrOsc) {
        this.sqrOsc = sqrOsc;
    }

    public UnitOscillator getCurrentOsc() {
        return currentOsc;
    }

    public void setCurrentOsc(UnitOscillator currentOsc) {
        this.currentOsc = currentOsc;
    }

    
    public void setOutput(UnitOutputPort output) {
        this.output = output;
    }

    
    public UnitInputPort getInput() {
        return input;
    }

    
    public void setInput(UnitInputPort input) {
        this.input = input;
    }

    
    public Signal getAudioSignal() {
        return audioSignal;
    }

    
    public void setAudioSignal(Signal audioSignal) {
        this.audioSignal = audioSignal;
    }

    @Override
    public Tuple<UnitPort, PortType> getPort(String name) {
        if(name == "output") return new Tuple(getPortByName(name),PortType.OUTPUT);
        return null;
    }
}
