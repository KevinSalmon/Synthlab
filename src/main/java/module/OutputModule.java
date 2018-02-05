package module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.*;
import controller.Obseurveur;
import controller.SubjectOutput;
import filter.AttenuationFilter;
import utils.Tuple;
import java.util.ArrayList;
import java.util.List;

public class OutputModule extends Module implements UnitSource, Obseurveur<SubjectOutput> {
    private final String INPUT = "input";

    private UnitInputPort input;
    private UnitOutputPort output;
    private Boolean mute;
    private AttenuationFilter attenuationFilter;
    private LineOut lineOut;
    private List<Obseurveur<SubjectOutput>> obseuveurOutputList;

    public OutputModule(Synthesizer synth) {
        this.input = new UnitInputPort(INPUT);
        addPort(this.input, INPUT);
        this.output = new UnitOutputPort("output");
        addPort(this.output, "output");
        this.mute = false;
        this.obseuveurOutputList = new ArrayList<>();

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

    @Override
    public Tuple<UnitPort, PortType> getPort(String name) {
        if(INPUT.equals(name)) {
            return new Tuple(getPortByName(name),PortType.INPUT);
        }
        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);
        this.attenuationFilter.generate(start, limit);
    }

    @Override
    public void update(SubjectOutput o) {
        mute = o.getMuteValue();
        attenuationFilter.setDecibelsAttenuation(o.getDecibelValue());
    }

    @Override
    public UnitOutputPort getOutput() {
        return null;
    }
}

