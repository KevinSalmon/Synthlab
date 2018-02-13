package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.EnvelopeDAHDSR;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectEG;
import utils.PortType;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

public class EG extends Module implements UnitSource, Obseurveur<SubjectEG>{

    private final EnvelopeDAHDSR envelope;

    private UnitOutputPort out;

    private UnitInputPort gate;

    private double attack;
    private double decay;
    private double sustain;
    private double release;

    public EG() {
        envelope = new EnvelopeDAHDSR();

        add(envelope);

        attack = 0.1;
        decay = 0.2;
        sustain = 0.5;
        release = 0.3;

        out = new UnitOutputPort();
        addPort(out, PortType.OUTPUT.getType());

        gate = envelope.input;
        addPort(gate, PortType.INPUT.getType());

    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;

        if(this.attack < 0.01) this.attack = 0.01;
        else if(this.attack > 8) this.attack = 8;

        envelope.attack.set(this.attack);
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;

        if(this.decay < 0.0) this.decay = 0.0;
        else if(this.decay > 2) this.decay = 2;

        envelope.decay.set(this.decay);
    }

    public double getSustain() {
        return sustain;
    }

    public void setSustain(double sustain) {
        this.sustain = sustain;

        if(this.sustain < 0.0) this.sustain = 0.0;
        else if(this.sustain > 1) this.sustain = 1;

        envelope.sustain.set(this.sustain);
    }

    public double getRelease() {
        return release;
    }

    public void setRelease(double release) {
        this.release = release;

        if(this.release < 0.01) this.release = 0.01;
        else if(this.release > 8) this.release = 8;

        envelope.release.set(this.release);
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)){
            return new Tuple<>(out,PortType.OUTPUT);
        }
        else if(PortType.INPUT.getType().equals(name)) return new Tuple<>(gate,PortType.INPUT);
        return null;
    }

    @Override
    public UnitOutputPort getOutput() {
        return out;
    }

    public UnitInputPort getInput() {
        return gate;
    }

    @Override
    public void update(SubjectEG o) {
        setAttack(o.getAttackValue());
        setDecay(o.getDecayValue());
        setSustain(o.getSustainValue());
        setRelease(o.getReleaseValue());
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);
        double[] outputs = out.getValues();
        double[] env = envelope.getOutput().getValues();

        for (int i = start; i < limit; i++) {
            outputs[i] = env[i]*(5.0/12.0);
        }
    }

    @Override
    public Module getReference() {
        return this;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUT);
        list.add(PortType.OUTPUT);
        return list;
    }
}
