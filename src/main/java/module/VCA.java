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

public class VCA extends Module implements UnitSource, Obseurveur<SubjectVCA> {
    private UnitInputPort in; // Signal d'entrée
    private UnitInputPort am; // Entrée : Modulation d'amplitude
    private UnitOutputPort out; // Sortie de signal
    private Double a0 = 0.0; // Gain de base a0 réglé en façade obtenu lorsque am = 5V
    private Amplification attenuationFilter;

    public VCA() {
        this.in = new UnitInputPort(PortType.INPUT.getType());
        addPort(this.in, PortType.INPUT.getType());
        this.am = new UnitInputPort(PortType.AM.getType());
        addPort(this.am, PortType.AM.getType());
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

            outputs[i] = attenuationFilter.applyAmplification(inputs[i] * (amin*12/5));

        }
    }

    private boolean amIsEmpty(double[] amValues) {
        for (double i : amValues) {
            if (i != 0) {
                return false;
            }
        }
        return true;
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
        this.attenuationFilter.setDecibelsAmplification(o.getDecibel());
    }

    @Override
    public Module getReference() {
        return this;
    }

    public double getDecibelsAttenuation() { // Pour le débug
        return this.attenuationFilter.getDecibelsAmplification();
    }

    public class Amplification {

        private double amplification;
        private double decibels;

        public Amplification() {
            setDecibelsAmplification(0);
        }


        public void setDecibelsAmplification(double db) {
            decibels = db;
            this.amplification = AudioMath.decibelsToAmplitude(db);
        }

        public void setAmplification(double amp) {
            decibels = AudioMath.amplitudeToDecibels(amp);
            this.amplification = amp;
        }

        public double getDecibelsAmplification() {
            return this.decibels;
        }

        public double getAmplification() {
            return this.amplification;
        }

        public double applyAmplification(double sig){
            return sig * amplification;
        }
    }
}

