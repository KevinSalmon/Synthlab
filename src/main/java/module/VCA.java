package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitSource;
import com.softsynth.math.AudioMath;
import controller.Obseurveur;
import controller.SubjectVCA;
import utils.PortType;
import utils.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class VCA extends Module implements UnitSource, Obseurveur<SubjectVCA> {
    private UnitInputPort in; // Signal d'entrée
    private UnitInputPort am; // Entrée : Modulation d'amplitude
    private UnitOutputPort out; // Sortie de signal
    private Amplification attenuationFilter;

    public VCA() {
        this.in = new UnitInputPort(PortType.INPUT.getType());
        addPort(this.in, PortType.INPUT.getType());
        this.am = new UnitInputPort(PortType.INPUTAM.getType());
        addPort(this.am, PortType.INPUTAM.getType());
        this.out = new UnitOutputPort();
        addPort(this.out, PortType.OUTPUT.getType());

        this.attenuationFilter = new Amplification();
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
        return attenuationFilter.getDecibelsAmplification();
    }

    public void setA0(Double a0) {
        this.attenuationFilter.setDecibelsAmplification(a0);
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

            outputs[i] = attenuationFilter.applyAmplification(inputs[i] * (amin*12/5));

        }
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        if(PortType.INPUT.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.INPUT);
        if(PortType.INPUTAM.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.INPUT);
        return null;
    }

    @Override
    public void update(SubjectVCA o) {
        this.setA0(o.getDecibel());
    }

    @Override
    public Module getReference() {
        return this;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUT);
        list.add(PortType.INPUTAM);
        list.add(PortType.OUTPUT);
        return list;
    }

    public class Amplification {

        private double amplificationVal;
        private double decibels;

        public Amplification() {
            setDecibelsAmplification(0);
        }


        public void setDecibelsAmplification(double db) {
            decibels = db;
            this.amplificationVal = AudioMath.decibelsToAmplitude(db);
        }

        public void setAmplificationVal(double amp) {
            decibels = AudioMath.amplitudeToDecibels(amp);
            this.amplificationVal = amp;
        }

        public double getDecibelsAmplification() {
            return this.decibels;
        }

        public double getAmplificationVal() {
            return this.amplificationVal;
        }

        public double applyAmplification(double sig){
            return sig * amplificationVal;
        }
    }
}

