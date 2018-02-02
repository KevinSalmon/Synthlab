package module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.*;
import controller.Obseurveur;
import controller.SubjectOutput;
import filter.AttenuationFilter;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

public class OutputModule extends Module implements Obseurveur<SubjectOutput> {
    private UnitOutputPort input;
    private Boolean mute;
    private AttenuationFilter attenuationFilter;
    private LineOut lineOut;
    private List<Obseurveur<SubjectOutput>> obseuveurOutputList;

    public OutputModule(Synthesizer synth) {
        this.input = null;
        this.mute = false;
        this.attenuationFilter = new AttenuationFilter();
        synth.add(this.lineOut = new LineOut());
        obseuveurOutputList = new ArrayList<>();
    }

    public UnitOutputPort getInput() {
        return this.input;
    }

    /**
     * Brancher un port de sortie d'un module sur l'entrée du module de sortie
     * @param input Entrée du module. null si rien n'est branchée
     */
    public void setInput(UnitOutputPort input) {
        if (this.input != null) {
            this.input.disconnectAll();
        }
        this.input = input;
        if (this.input != null) {
            this.attenuationFilter.input.connect(this.input);
            this.attenuationFilter.output.connect(0, this.lineOut.input, 0);
            this.attenuationFilter.output.connect(0, this.lineOut.input, 1);
            this.lineOut.start();
        }
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
        if(name.equals("input")) {
            return new Tuple(getPortByName(name),PortType.INPUT);
        }
        return null;
    }

    @Override
    public void update(SubjectOutput o) {

        mute = o.getMuteValue();
        attenuationFilter.setDecibelsAttenuation(o.getDecibelValue());
    }
}
