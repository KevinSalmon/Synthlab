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
import utils.Amplification;
import utils.PortType;
import utils.Tuple;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class OutputModule extends Module implements Obseurveur<SubjectOutput> {

    private Synthesizer synth;
    private UnitInputPort input;
    private Boolean mute;
    private Amplification amplification;
    private LineOut lineOut;
    private File waveFile = null;
    private WaveRecorder recorder;
    private Boolean lastUpdateFail = false;
    private boolean record = false;

    public OutputModule(Synthesizer synth) {
        this.synth = synth;

        this.lineOut = new LineOut();
        add(this.lineOut);

        this.amplification = new Amplification();

        this.input = new UnitInputPort(PortType.INPUT.getType());
        addPort(this.input, PortType.INPUT.getType());
        this.mute = false;
        synth.add(this);
        this.start();
    }

    public UnitInputPort getInput() {
        return this.input;
    }

    public double getDecibelsAttenuation() {
        return this.amplification.getDecibelsAmplification();
    }

    public void setDecibelsAttenuation(double decibels) {
        if (decibels <= 12.0) {
            this.amplification.setDecibelsAmplification(decibels);
        }
    }

    public void changeDecibelsAttenuation(double delta) {
        this.setDecibelsAttenuation(this.getDecibelsAttenuation() + delta);
    }

    public Boolean getMute() {
        return this.mute;
    }

    public void switchMute() {
        setMute(!this.mute);
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
        super.generate(start,limit);

        double[] inputs = input.getValues();
        double[] lineInputs = lineOut.getInput().getValues();

        if(this.mute) {
            for(int i = start; i < limit; i++) {
                lineInputs[i] = 0.0;
            }
        }
        else{
            for(int i = start; i < limit; i++) {
                lineInputs[i] = amplification.applyAmplification(inputs[i]);
            }
        }
        if(record) {
            double[] recordInputs = this.recorder.getInput().getValues();
            System.arraycopy(lineInputs, start, recordInputs, start, limit - start);
        }
    }

    @Override
    public void update(SubjectOutput o) {
        this.setMute(o.getMuteValue());
        this.setDecibelsAttenuation(o.getDecibelValue());

        if(o.getRecordEnabled()) {
            try {
                this.startRecording(o.getRecordFilename());
                this.lastUpdateFail = false;
            } catch (FileNotFoundException e) {
                Logger.getGlobal().severe(e.toString());
                this.lastUpdateFail = true;
            }
        }
        else {
            try {
                this.stopRecording();
            } catch (IOException e) {
                Logger.getGlobal().warning(e.toString());
            }
        }
    }

    @Override
    public Module getReference() {
        return this;
    }

    /**
     * Pour test uniquement
     * @return
     */
    public UnitInputPort getOutputTest() {
        return this.lineOut.getInput();
    }

    /**
     * Pour test uniquement
     * @return
     */
    public Boolean getLastUpdateFail() {
        return this.lastUpdateFail;
    }

    /**
     * Lance l'enregistreur s'il n'est pas déjà en train d'enregistrer
     * @param pathname Nom du fichier de sortie
     * @throws FileNotFoundException Le fichier pathname ne peut pas être écrit (ex: sous-dossier)
     */
    private void startRecording(String pathname) throws FileNotFoundException {
        if(this.waveFile == null) { // Si aucun enregistremnt est en cours
            this.waveFile = new File(pathname);
            this.recorder = new WaveRecorder(this.synth, this.waveFile);
            record=true;
//            for (int i = 0; i < this.output.getNumParts(); i++) {
//                this.output.connect(i, this.recorder.getInput(), i);
//            }
            this.recorder.start();
        }
    }

    /**
     * Arrête l'enregistreur s'il est activé
     * @throws IOException Erreur lors de la fermeture de l'enregistreur
     */
    private void stopRecording() throws IOException {
        if (this.recorder != null) { // Si un enregistrement est en cours
            this.recorder.stop();
            this.recorder.close();
            this.waveFile = null;
            this.record = false;
        }
    }
}
