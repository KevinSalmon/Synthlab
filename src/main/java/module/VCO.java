package module;

import Signal.*;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectVCO;
import utils.OscillatorFactory;
import utils.OscillatorType;
import utils.Tuple;

public class VCO extends Module implements UnitSource, Obseurveur<SubjectVCO> {

    private UnitOscillator sqrOsc;
    private UnitOscillator triOsc;
    private UnitOscillator sawOsc;
    private UnitOscillator currentOsc;
    private UnitOutputPort output;
    private Signal audioSignal;

    private int octave;
    private double reglageFin;

    public VCO() {
        sqrOsc = OscillatorFactory.createOscillator(OscillatorType.SQUARE);
        triOsc = OscillatorFactory.createOscillator(OscillatorType.TRIANGLE);
        sawOsc = OscillatorFactory.createOscillator(OscillatorType.SAWTOOTH);

        add(sqrOsc);
        add(triOsc);
        add(sawOsc);

        currentOsc = sawOsc;

        addPort(output = new UnitOutputPort(), "output");
        audioSignal = new AudioSignal(0.5, 440);
        currentOsc.frequency.set(audioSignal.getFrequency());
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] out = output.getValues();
        double[] osc = currentOsc.output.getValues();

        for (int i = start; i < limit; i++) {
            out[i] = osc[i];
        }
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
        audioSignal.setFrequency(440.0 *Math.pow(2,octave + reglageFin));
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

    /**
     * Récupère l'oscillateur actuellement utilisé
     * @return
     */
    public UnitOscillator getCurrentOsc() {
        return currentOsc;
    }

    /**
     * Change l'oscillateur utilisé
     * @param type
     */
    public void changeCurrentOsc(OscillatorType type) {

        double freq = currentOsc.frequency.getValue();
        double amp = currentOsc.amplitude.getValue();

        switch (type){
            case SQUARE: currentOsc = sqrOsc;
            break;
            case TRIANGLE: currentOsc = triOsc;
            break;
            case SAWTOOTH: currentOsc = sawOsc;
            break;
        }

        currentOsc.frequency.set(freq);
        currentOsc.amplitude.set(amp);

    }

    
    public void setOutput(UnitOutputPort output) {
        this.output = output;
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
        return this.currentOsc.frequency.get();
    }

    public int getOctave() {
        return octave;
    }

    public double getReglageFin() {
        return reglageFin;
    }

    @Override
    public void update(SubjectVCO o) {
        octave = o.getOctaveValue();
        reglageFin = o.getReglageFinValue();
    }
}
