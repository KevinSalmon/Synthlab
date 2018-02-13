package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.softsynth.math.AudioMath;
import controller.Obseurveur;
import controller.SubjectMixer;
import utils.PortType;
import utils.Tuple;

public class Mixer extends Module implements Obseurveur<SubjectMixer> {

    private UnitInputPort in1;
    private Double in1dbAttenuation = 0.0;
    private UnitInputPort in2;
    private Double in2dbAttenuation = 0.0;
    private UnitInputPort in3;
    private Double in3dbAttenuation = 0.0;
    private UnitInputPort in4;
    private Double in4dbAttenuation = 0.0;
    private UnitOutputPort out;

    public Mixer() {
        this.in1 = new UnitInputPort(PortType.INPUT1.getType());
        addPort(in1, PortType.INPUT1.getType());
        this.in2 = new UnitInputPort(PortType.INPUT2.getType());
        addPort(in2, PortType.INPUT2.getType());
        this.in3 = new UnitInputPort(PortType.INPUT3.getType());
        addPort(in3, PortType.INPUT3.getType());
        this.in4 = new UnitInputPort(PortType.INPUT4.getType());
        addPort(in4, PortType.INPUT4.getType());

        this.out = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(out, PortType.OUTPUT.getType());
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {

        if(PortType.INPUT1.getType().equals(name))
            return new Tuple(getPortByName(name),PortType.INPUT1);
        if(PortType.INPUT2.getType().equals(name))
            return new Tuple(getPortByName(name),PortType.INPUT2);
        if(PortType.INPUT3.getType().equals(name))
            return new Tuple(getPortByName(name),PortType.INPUT3);
        if(PortType.INPUT4.getType().equals(name))
            return new Tuple(getPortByName(name),PortType.INPUT4);
        if(PortType.OUTPUT.getType().equals(name))
            return new Tuple(getPortByName(name),PortType.OUTPUT);

        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] in1 = this.in1.getValues();
        double[] in2 = this.in2.getValues();
        double[] in3 = this.in3.getValues();
        double[] in4 = this.in4.getValues();
        double[] out = this.out.getValues();

        for(int i = start; i < limit; i++) {
            out[i] = (in1[i] * AudioMath.decibelsToAmplitude(in1dbAttenuation))
                + (in2[i] * AudioMath.decibelsToAmplitude(in2dbAttenuation))
                + (in3[i] * AudioMath.decibelsToAmplitude(in3dbAttenuation))
                + (in4[i] * AudioMath.decibelsToAmplitude(in4dbAttenuation));
        }
    }

    @Override
    public void update(SubjectMixer o) {
        if (o.getIn1DbAttenuation() <= 12.0)
            this.in1dbAttenuation = o.getIn1DbAttenuation();
        if (o.getIn2DbAttenuation() <= 12.0)
            this.in2dbAttenuation = o.getIn2DbAttenuation();
        if (o.getIn3DbAttenuation() <= 12.0)
            this.in3dbAttenuation = o.getIn3DbAttenuation();
        if (o.getIn4DbAttenuation() <= 12.0)
            this.in4dbAttenuation = o.getIn4DbAttenuation();
    }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitInputPort getIn1() {
        return in1;
    }

    public Double getIn1dbAttenuation() {
        return in1dbAttenuation;
    }

    public UnitInputPort getIn2() {
        return in2;
    }

    public Double getIn2dbAttenuation() {
        return in2dbAttenuation;
    }

    public UnitInputPort getIn3() {
        return in3;
    }

    public Double getIn3dbAttenuation() {
        return in3dbAttenuation;
    }

    public UnitInputPort getIn4() {
        return in4;
    }

    public Double getIn4dbAttenuation() {
        return in4dbAttenuation;
    }

    public UnitOutputPort getOutput() {
        return out;
    }
}
