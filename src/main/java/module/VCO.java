package module;

import Signal.*;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import utils.Tuple;

public class VCO extends Module implements UnitSource {

    private SquareOscillator sqrOsc;
    private UnitOscillator currentOsc;
    private UnitOutputPort output;
    private UnitInputPort input;
    private Signal audioSignal;

    private int octave;
    private double reglageFin;



    public VCO() {
        add(sqrOsc = new SquareOscillator());
        currentOsc = sqrOsc;

        addPort(output = currentOsc.output, "output");
        addPort(input = currentOsc.frequency, "input");
        audioSignal = new AudioSignal(0.5, 440);
        currentOsc.frequency.set(audioSignal.getFrequency()); //1 kHz
    }

    @Override
    public UnitOutputPort getOutput() {
        return output;
    }

    /**
     * Modifie la valeur de l'octave de o
     * @param o
     */
    public void modifyOctave(int o) {
        octave += o;

        if(octave < -2) octave = -2;
        else if(octave > 3) octave = 3;

        updateFrequency();
    }

    /**
     * Met à jour la fréquence du signal
     */
    private void updateFrequency() {
        audioSignal.setFrequency(440.0 *(octave + reglageFin));
        currentOsc.frequency.set(audioSignal.getFrequency());
    }

    /**
     * Augmente l'octave de o
     * @param o
     */
    public void increaseOctave(int o) {
        modifyOctave(o);
    }

    /**
     * Diminue l'octave de o
     * @param o
     */
    public void decreaseOctave(int o) {
        modifyOctave(-o);
    }

    /**
     * Modifie la valeur du réglage fin de r
     * @param r
     */
    private void modifyReglageFin(double r) {
        reglageFin += r;

        if(reglageFin < 0) reglageFin = 0.0;
        else if(reglageFin > 1) reglageFin = 1.0;

        updateFrequency();

    }

    /**
     * Augmente le réglage fin de r
     * @param r
     */
    public void increaseReglageFin(double r) {
        modifyReglageFin(r);
    }


    /**
     * Diminue le réglage fin de r
     * @param r
     */
    public void decreaseReglageFin(double r) {
        modifyReglageFin(-r);
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
        if(name == "input") return new Tuple(getPortByName(name),PortType.INPUT);
        return null;
    }

    public double getFrequency(){
        return this.currentOsc.frequency.getValue();
    }

    public int getOctave() {
        return octave;
    }

    public double getReglageFin() {
        return reglageFin;
    }
}
