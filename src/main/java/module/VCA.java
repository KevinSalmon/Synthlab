package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectVCA;
import utils.Amplification;
import utils.PortType;
import utils.Tuple;

import static jdk.nashorn.internal.objects.Global.Infinity;

public class VCA extends Module implements UnitSource, Obseurveur<SubjectVCA> {
    private UnitInputPort in; // Signal d'entrée
    private UnitInputPort am; // Entrée : Modulation d'amplitude
    private UnitOutputPort out; // Sortie de signal
    private Double a0 = 0.0; // Gain de base a0 réglé en façade obtenu lorsque am = 5V
    private Amplification amAmplification;
    private Amplification amplification;
    private Double initialVoltage = null;

    public VCA() {
        this.in = new UnitInputPort(PortType.INPUT.getType());
        addPort(this.in, PortType.INPUT.getType());
        this.am = new UnitInputPort(PortType.AM.getType());
        addPort(this.am, PortType.AM.getType());
        this.out = new UnitOutputPort();
        addPort(this.out, PortType.OUTPUT.getType());

        this.amplification = new Amplification();
        this.amAmplification = new Amplification();
//        this.amplification.input = this.in;
//        this.amplification.output = this.out;
    }

    /**
     * Getters et Setters
     */

    public UnitInputPort getInput() {
        return in;
    }

    public UnitInputPort getAm() {
        return am;
    }

    public Double getA0() {
        return a0;
    }

    public void setA0(Double a0) {
        this.a0 = a0;
    }

    @Override
    public UnitOutputPort getOutput() {
        return this.out;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] inputs = in.getValues();
        double[] ams = am.getValues();
        double[] outputs = out.getValues();

        for (int i = start; i < limit; i++) {
            double amin = ams[i];

            amAmplification.setDecibelsAmplification(amin==0?-Infinity:(Math.abs(amin*12)-5)*12);//amin>=0?-(amin*12 - 5)*12:1000);

            outputs[i] = amplification.applyAmplification(amAmplification.applyAmplification(inputs[i]));
        }
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        if(PortType.INPUT.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.INPUT);
        if(PortType.AM.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.INPUT);
        return null;
    }

    @Override
    public void update(SubjectVCA o) {
        this.setA0(o.getDecibel());
        this.amplification.setDecibelsAmplification(o.getDecibel());
    }

    @Override
    public Module getReference() {
        return this;
    }

    public double getDecibelsAmplification() { // Pour le débug
        return this.amplification.getDecibelsAmplification();
    }

    public double getAMDecibelsAmplification() { // Pour le débug
        return this.amAmplification.getDecibelsAmplification();
    }
}

