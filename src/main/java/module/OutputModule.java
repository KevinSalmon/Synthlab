package module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.util.WaveRecorder;
import controller.Obseurveur;
import controller.SubjectOutput;
import filter.AttenuationFilter;
import utils.PortType;
import utils.Tuple;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class OutputModule extends Module implements UnitSource, Obseurveur<SubjectOutput> {

    private Synthesizer synth;
    private UnitInputPort input;
    private UnitOutputPort output;
    private Boolean mute;
    private AttenuationFilter attenuationFilter;
    private LineOut lineOut;
    private File waveFile = null;
    private WaveRecorder recorder;

    public OutputModule(Synthesizer synth) {
        this.synth = synth;
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
            return new Tuple<>(getPortByName(name),PortType.INPUT);
        }
        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        if(this.mute) {
            double[] outputs = output.getValues();
            for(int i = start; i < limit; i++) {
                outputs[i] = 0.0;
            }
        }
        else {
            this.attenuationFilter.generate(start, limit);
        }
    }

    @Override
    public void update(SubjectOutput o) {
        this.setMute(o.getMuteValue());
        this.setDecibelsAttenuation(o.getDecibelValue());

        if(o.getRecordEnabled()) {
            this.startRecording(o.getRecordFilename());
        }
        else {
            this.stopRecording();
        }
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

    /**
     * Pour test uniquement
     * @return
     */
    public UnitOutputPort getOutputTest() {
        return this.output;
    }

    /**
     * Lance l'enregistrement
     * @param pathname Nom du fichier de sortie
     */
    private void startRecording(String pathname) {
        if(this.waveFile == null) { // Si aucun enregistremnt est en cours
            this.waveFile = new File(pathname);
            try {
                this.recorder = new WaveRecorder(this.synth, this.waveFile);
                for (int i = 0; i < this.output.getNumParts(); i++) {
                    this.output.connect(i, this.recorder.getInput(), i);
                }
                this.recorder.start();
            } catch (FileNotFoundException e) {
                Logger.getGlobal().warning(e.toString());
            }
        }
    }

    public void stopRecording() {
        if (this.recorder != null) { // Si un enregistrement est en cours
            this.recorder.stop();
            try {
                this.recorder.close();
            } catch (IOException e) {
                Logger.getGlobal().severe(e.toString());
            }
            this.waveFile = null;
        }
    }
}
