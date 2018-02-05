package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import filter.AttenuationFilter;
import utils.Tuple;
import Signal.Signal;

public class VCA extends Module implements UnitSource {
    private final String INPUT = "input";

    private UnitInputPort input; // Signal d'entrée
    private UnitInputPort am; // Entrée : Modulation d'amplitude
    private UnitOutputPort output;
    private UnitOscillator currentOsc;
    private AttenuationFilter attenuationFilter;
    private Signal audioSignal;

    /*
        TODO
        une entrée de signal nommée in
        une entrée de modulation d’amplitude nommée am
        une sortie de signal nommée out
        un réglage manuel en façade du gain de base a0, obtenu lorsque am = 5V
     */

    public VCA() {
        this.input = new UnitInputPort(INPUT);
        addPort(this.input, INPUT);
        this.am = new UnitInputPort("am");
        addPort(this.am, "am");
        this.output = new UnitOutputPort();
        addPort(this.output, "output");

        this.attenuationFilter = new AttenuationFilter();
        this.attenuationFilter.input = this.input;
        this.attenuationFilter.output = this.output;
    }

    /**
     * Getters et Setters
     */

    public UnitInputPort getInput() {
        return input;
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
        return this.output;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        /* TODO
        lorsque que l’entrée am est déconnectée ou nulle, le gain du VCA est nul (pas de signal en sortie)
        lorsque am vaut 5 V et a0 vaut 0 dB le signal de sortie est identique au signal d’entrée
        lorsque la tension d’entrée sur am augmente d’1 V, le gain augmente de 12 dB
        lorsque la tension d’entrée sur am diminue d’1 V, le gain diminue de 12 dB
        */
        this.attenuationFilter.generate(start, limit);
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name == "output") return new Tuple(getPortByName(name),PortType.OUTPUT);
        if(name.equals(INPUT)) return new Tuple(getPortByName(name),PortType.INPUT);
        if(name == "am") return new Tuple(getPortByName(name),PortType.INPUT);
        return null;
    }
}

