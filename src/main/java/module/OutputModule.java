package module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.*;
import controller.Obseurveur;
import controller.SubjectOutput;
import filter.AttenuationFilter;
import utils.PortType;
import utils.Tuple;

public class OutputModule extends Module implements UnitSource, Obseurveur<SubjectOutput> {

    private UnitInputPort input;
    private UnitOutputPort output;
    private Boolean mute;
    private AttenuationFilter attenuationFilter;
    private LineOut lineOut;

    public OutputModule(Synthesizer synth) {
        this.input = new UnitInputPort(PortType.INPUT.getType());
        addPort(this.input, PortType.INPUT.getType());
        this.output = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(this.output, PortType.OUTPUT.getType());
        this.mute = false;

        this.attenuationFilter = new AttenuationFilter();
        this.attenuationFilter.input = this.input;
        this.attenuationFilter.output = this.output;

        this.lineOut = new LineOut();
        synth.add(this.lineOut);
        this.output.connect(0, lineOut.input, 0);
        this.lineOut.start();
    }

    public UnitInputPort getInput() {
        return this.input;
    }

    public double getDecibelsAttenuation() {
        return this.attenuationFilter.getDecibelsAttenuation();
    }

    public void setDecibelsAttenuation(double decibels) {
        if (decibels <= 12.0) {
            this.attenuationFilter.setDecibelsAttenuation(decibels);
        }
    }

    public void changeDecibelsAttenuation(double delta) {
        this.setDecibelsAttenuation(this.getDecibelsAttenuation() + delta);
    }

    public Boolean getMute() {
        return this.mute;
    }

    public void switchMute() {
        lineOut.setEnabled(this.mute);
        this.mute = !this.mute;
    }
    public void setMute(boolean val){
        this.mute = val;
        lineOut.setEnabled(!this.mute);
    }

    @Override
    public Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.INPUT.getType().equals(name)) {
            return new Tuple(getPortByName(name),PortType.INPUT);
        }
        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);
        this.attenuationFilter.generate(start, limit);

        /*if(this.mute) { // S'il y a besoin de réellement passer le son en muet et non pas désactivé lineOut
            double[] outputs = output.getValues();
            for(int i = start; i < limit; i++) {
                outputs[i] = 0.0;
            }
        }*/
    }

    @Override
    public void update(SubjectOutput o) {
        this.setMute(o.getMuteValue());
        this.setDecibelsAttenuation(o.getDecibelValue());
    }

    @Override
    public Module getReference() {
        return this;
    }

    /**
     * @return Null car pas de sortie
     */
    @Override
    public UnitOutputPort getOutput() {
        return null;
    }
}

