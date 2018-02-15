package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.softsynth.math.AudioMath;
import controller.Obseurveur;
import controller.SubjectMixer;
import utils.PortType;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

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
            return new Tuple<>(getPortByName(name),PortType.INPUT1);
        if(PortType.INPUT2.getType().equals(name))
            return new Tuple<>(getPortByName(name),PortType.INPUT2);
        if(PortType.INPUT3.getType().equals(name))
            return new Tuple<>(getPortByName(name),PortType.INPUT3);
        if(PortType.INPUT4.getType().equals(name))
            return new Tuple<>(getPortByName(name),PortType.INPUT4);
        if(PortType.OUTPUT.getType().equals(name))
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);

        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] in1Values = this.in1.getValues();
        double[] in2Values = this.in2.getValues();
        double[] in3Values = this.in3.getValues();
        double[] in4Values = this.in4.getValues();
        double[] outValues = this.out.getValues();

        for(int i = start; i < limit; i++) {
            double in1amp = (in1dbAttenuation <= -100) ? 0.0 : AudioMath.decibelsToAmplitude(in1dbAttenuation);
            double in2amp = (in2dbAttenuation <= -100) ? 0.0 : AudioMath.decibelsToAmplitude(in2dbAttenuation);
            double in3amp = (in3dbAttenuation <= -100) ? 0.0 : AudioMath.decibelsToAmplitude(in3dbAttenuation);
            double in4amp = (in4dbAttenuation <= -100) ? 0.0 : AudioMath.decibelsToAmplitude(in4dbAttenuation);

            double outValue = (in1Values[i] * in1amp)
                    + (in2Values[i] * in2amp)
                    + (in3Values[i] * in3amp)
                    + (in4Values[i] * in4amp);

            /*
            double outValue = in1Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in1SldAttenuation) / Math.log(2))
                + in2Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in2SldAttenuation) / Math.log(2))
                + in3Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in3SldAttenuation) / Math.log(2))
                + in4Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in4SldAttenuation) / Math.log(2));
             */

            outValue = (outValue > 1.0) ? 1.0 : outValue;
            outValue = (outValue < -1.0) ? -1.0 : outValue;
            outValues[i] = outValue;
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

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUT1);
        list.add(PortType.INPUT2);
        list.add(PortType.INPUT3);
        list.add(PortType.INPUT4);
        list.add(PortType.OUTPUT);
        return list;
    }
}