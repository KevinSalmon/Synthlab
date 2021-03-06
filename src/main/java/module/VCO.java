package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectVCO;
import utils.OscillatorFactory;
import utils.OscillatorType;
import utils.PortType;
import utils.Tuple;
import java.util.ArrayList;
import java.util.List;
public class VCO extends Module implements UnitSource, Obseurveur<SubjectVCO> {

    private UnitOscillator sqrOsc;
    private UnitOscillator triOsc;
    private UnitOscillator sawOsc;
    private UnitOscillator sinOsc;
    private UnitOscillator currentOsc;
    private UnitInputPort fm;
    private UnitOutputPort output;
    private double audioSignal = 440.0;

    private static final int F0 = 440;
    private static final int OCTAVE_MAX = 3;
    private static final int OCTAVE_MIN = -2;
    private static final double LFO_MAX = 20;
    private static final double LFO_MIN = 0.1;

    private boolean lfo;

    private int octave;
    private double reglageFin;
    private OscillatorType oscillatorType;

    public VCO() {

        //Crée les différents oscillateurs
        sqrOsc = OscillatorFactory.createOscillator(OscillatorType.SQUARE);
        triOsc = OscillatorFactory.createOscillator(OscillatorType.TRIANGLE);
        sawOsc = OscillatorFactory.createOscillator(OscillatorType.SAWTOOTH);
        sinOsc = OscillatorFactory.createOscillator(OscillatorType.SINE);

        add(sqrOsc);
        add(triOsc);
        add(sawOsc);
        add(sinOsc);

        currentOsc = triOsc;
        oscillatorType = OscillatorType.TRIANGLE;

        lfo = false;

        //Crée le port de sortie
        output = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(output);
        fm = new UnitInputPort(PortType.INPUTFM.getType());
        addPort(fm);
        currentOsc.frequency.set(F0);
    }

    @Override
    public void generate(int start, int limit) {

        //Récupère les adresses des valeurs des sorties du port de sortie
        // et de la sortie de l'oscillateur courant
        double[] mod = this.fm.getValues();
        double[] out = output.getValues();
        double[] osc = currentOsc.output.getValues();
        double[] freq = currentOsc.frequency.getValues();

        //Calcul la fréquence
        for (int i = start; i < limit; i++) {
            if(lfo){
                freq[i] = LFO_MIN + ((reglageFin+1.0)/2.0) * (LFO_MAX-LFO_MIN);
            }
            else {freq[i] = 440.0 *Math.pow(2,octave + reglageFin + (12*mod[i]));}
        }

        super.generate(start, limit);

        // Relie l'oscillateur courant à la sortie du VCO en copiant les données
        for (int i = start; i < limit; i++) {
            out[i] = osc[i] * 5.0/12.0;
        }
    }

    @Override
    public UnitOutputPort getOutput() {
        return output;
    }

    /**
     * Modifie la valeur de l'octave de o
     * @param o la valeur de modification de l'octave
     */
    public void modifyOctave(int o) {
        setOctave(octave + o);

    }
    public void setOctave(int o){

        octave = o;

        if(octave < OCTAVE_MIN) octave = OCTAVE_MIN;
        else if(octave > OCTAVE_MAX) octave = OCTAVE_MAX;

        updateFrequency();
    }

    /**
     * Met à jour la fréquence du signal
     */
    private void updateFrequency() {
        if(lfo){
            audioSignal = LFO_MIN + reglageFin * (LFO_MAX-LFO_MIN);
            if (audioSignal < 0.0) {
                audioSignal = 0.0;
            }
        }
        else { audioSignal = 440.0 *Math.pow(2,octave + reglageFin );}

        currentOsc.frequency.set(audioSignal); //Actualise l'oscillateur courant
    }

    /**
     * Augmente l'octave de o
     * @param o la valeur à ajouter à l'octave
     */
    public void increaseOctave(int o) {
        modifyOctave(o);
    }

    /**
     * Diminue l'octave de o
     * @param o la valeur à soustraire à l'octave
     */
    public void decreaseOctave(int o) {
        modifyOctave(-o);
    }

    /**
     * Modifie la valeur du réglage fin de r
     * @param r la valeur de modification du réglage fin
     */
    private void modifyReglageFin(double r) {
        setReglageFin(reglageFin + r);
    }

    public void setReglageFin(double r){
        reglageFin = r;

        if(reglageFin < -1) reglageFin = -1.0;
        else if(reglageFin > 1) reglageFin = 1.0;

        updateFrequency();

    }

    /**
     * Augmente le réglage fin de r
     * @param r la valeur à ajouter au réglage fin
     */
    public void increaseReglageFin(double r) {
        modifyReglageFin(r);
    }


    /**
     * Diminue le réglage fin de r
     * @param r la valeur à soustraire au réglage fin
     */
    public void decreaseReglageFin(double r) {
        modifyReglageFin(-r);
    }

    /**
     * Récupère l'oscillateur actuellement utilisé
     * @return l'oscillateur utilisé
     */
    public UnitOscillator getCurrentOsc() {
        return currentOsc;
    }

    /**
     * Change l'oscillateur utilisé
     * @param type le type su nouvel oscillateur
     */
    public void changeCurrentOsc(OscillatorType type) {

        //Récupère la valeur de fréquence et d'amplitude de l'oscillateur courant
        double freq = currentOsc.frequency.getValue();

        //Change l'oscillateur courant
        switch (type){
            case SQUARE: currentOsc = sqrOsc;
                break;
            case TRIANGLE: currentOsc = triOsc;
                break;
            case SAWTOOTH: currentOsc = sawOsc;
                break;
            case SINE: currentOsc = sinOsc;
                break;

            default: throw new IllegalArgumentException("Oscillator not found");
        }

        oscillatorType = type;

        //Met à jour fréquence et amplitude
        currentOsc.frequency.set(freq);

    }

    
    public void setOutput(UnitOutputPort output) {
        this.output = output;
    }


    public double getAudioSignal() {
        return audioSignal;
    }

    
    public void setAudioSignal(double audioSignal) {
        this.audioSignal = audioSignal;
    }

    @Override
    public Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)){
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        }
        else if(PortType.INPUTFM.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.INPUTFM);
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
        setLFO(o.isLFOActive());
    }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitInputPort getInput() {
        return fm;
    }

    public boolean isLFO() {
        return lfo;
    }

    public void setLFO(boolean lfo) {
        this.lfo = lfo;
        updateFrequency();
    }

    public void switchLFO(){
        setLFO(!isLFO());
    }

    public static int getOctaveMax() {
        return OCTAVE_MAX;
    }

    public static int getOctaveMin() {
        return OCTAVE_MIN;
    }

    public static double getLfoMax() {
        return LFO_MAX;
    }

    public static double getLfoMin() {
        return LFO_MIN;
    }

    public OscillatorType getOscillatorType() {
        return oscillatorType;
    }

    public void setOscillatorType(OscillatorType oscillatorType) {
        this.oscillatorType = oscillatorType;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUTFM);
        list.add(PortType.OUTPUT);
        return list;
    }
}
