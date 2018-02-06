package module;

import com.jsyn.ports.UnitInputPort;
import signal.*;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectVCO;
import utils.OscillatorFactory;
import utils.OscillatorType;
import utils.PortType;
import utils.Tuple;

import javax.sound.sampled.Port;

public class VCO extends Module implements UnitSource, Obseurveur<SubjectVCO> {

    private UnitOscillator sqrOsc;
    private UnitOscillator triOsc;
    private UnitOscillator sawOsc;
    private UnitOscillator currentOsc;
    private UnitInputPort fm;
    private UnitOutputPort output;
    private Signal audioSignal;
    private final int f0 = 440;

    private int octave;
    private double reglageFin;

    public VCO() {

        //Crée les différents oscillateurs
        sqrOsc = OscillatorFactory.createOscillator(OscillatorType.SQUARE);
        triOsc = OscillatorFactory.createOscillator(OscillatorType.TRIANGLE);
        sawOsc = OscillatorFactory.createOscillator(OscillatorType.SAWTOOTH);

        add(sqrOsc);
        add(triOsc);
        add(sawOsc);

        currentOsc = triOsc;

        //Crée le port de sortie
        output = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(output);
        fm = new UnitInputPort(PortType.FM.getType());
        addPort(fm);
        audioSignal = new AudioSignal(0.5, f0);
        currentOsc.frequency.set(audioSignal.getFrequency());
        currentOsc.amplitude.set(audioSignal.getAmplitude());
    }

    @Override
    public void generate(int start, int limit) {

        //Récupère les adresses des valeurs des sorties du port de sortie
        // et de la sortie de l'oscillateur courant
        double[] fm = this.fm.getValues();
        double[] out = output.getValues();
        double[] osc = currentOsc.output.getValues();
        double[] freq = currentOsc.frequency.getValues();

        //Calcul la fréquence
        for (int i = start; i < limit; i++) {
            freq[i] = 440.0 *Math.pow(2,octave + reglageFin + (3*fm[i]));
        }

        super.generate(start, limit);

        // Relie l'oscillateur courant à la sortie du VCO en copiant les données
        System.arraycopy(osc, start, out, start, limit - start);
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
        setOctave(octave + o);

    }
    public void setOctave(int o){

        octave = o;

        if(octave < -2) octave = -2;
        else if(octave > 3) octave = 3;

        updateFrequency();
    }

    /**
     * Met à jour la fréquence du signal
     */
    private void updateFrequency() {
        audioSignal.setFrequency(440.0 *Math.pow(2,octave + reglageFin));
        currentOsc.frequency.set(audioSignal.getFrequency()); //Actualise l'oscillateur courant
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
        setReglageFin(reglageFin + r);
    }

    public void setReglageFin(double r){
        reglageFin = r;

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

        //Récupère la valeur de fréquence et d'amplitude de l'oscillateur courant
        //TODO remplacer par AudioSignal si on décide de l'utiliser
        double freq = currentOsc.frequency.getValue();
        double amp = currentOsc.amplitude.getValue();

        //Change l'oscillateur courant
        switch (type){
            case SQUARE: currentOsc = sqrOsc;
            break;
            case TRIANGLE: currentOsc = triOsc;
            break;
            case SAWTOOTH: currentOsc = sawOsc;
            break;
        }

        //Met à jour fréquence et amplitude
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
        if(PortType.OUTPUT.getType().equals(name)){
            Tuple t = new Tuple(getPortByName(name),PortType.OUTPUT);
            return new Tuple(getPortByName(name),PortType.OUTPUT);
        }
        if(PortType.FM.getType().equals(name)){

            Tuple t = new Tuple(getPortByName(name),PortType.FM);
            return new Tuple(getPortByName(name),PortType.FM);
        }
        // TODO Ajouter port de modulation de fréquence
        // if(name == "input") return new Tuple(getPortByName(name),PortType.INPUT);
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
        setOctave(o.getOctaveValue());
        setReglageFin(o.getReglageFinValue());
        changeCurrentOsc(o.getOscillatorType());
    }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitInputPort getInput() {
        return fm;
    }
}
