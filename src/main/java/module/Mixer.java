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
    private Double in1SldAttenuation = 0.0;
    private UnitInputPort in2;
    private Double in2SldAttenuation = 0.0;
    private UnitInputPort in3;
    private Double in3SldAttenuation = 0.0;
    private UnitInputPort in4;
    private Double in4SldAttenuation = 0.0;
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
            // TODO: Corriger la formule
            double outValue = in1Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in1SldAttenuation) / Math.log(2))
                + in2Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in2SldAttenuation) / Math.log(2))
                + in3Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in3SldAttenuation) / Math.log(2))
                + in4Values[i] * AudioMath.decibelsToAmplitude(Math.log(this.in4SldAttenuation) / Math.log(2));

            outValue = (outValue > 1.0) ? 1.0 : outValue;
            outValue = (outValue < -1.0) ? -1.0 : outValue;
            outValues[i] = outValue;

            // System.out.println("Sld1: " + (Math.log(this.in1SldAttenuation) / Math.log(2)) + " sld1: " + this.in1SldAttenuation + " out:" + outValue);
        }
    }

    @Override
    public void update(SubjectMixer o) {
        //if (o.getIn1DbAttenuation() <= 12.0)
            this.in1SldAttenuation = o.getIn1attenuation();
        //if (o.getIn2DbAttenuation() <= 12.0)
            this.in2SldAttenuation = o.getIn2attenuation();
        //if (o.getIn3DbAttenuation() <= 12.0)
            this.in3SldAttenuation = o.getIn3attenuation();
        //if (o.getIn4DbAttenuation() <= 12.0)
            this.in4SldAttenuation = o.getIn4attenuation();
    }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitInputPort getIn1() {
        return in1;
    }

    public Double getIn1SldAttenuation() {
        return in1SldAttenuation;
    }

    public void setIn1SldAttenuation(Double in1SldAttenuation) {
        this.in1SldAttenuation = in1SldAttenuation;
    }

    public UnitInputPort getIn2() {
        return in2;
    }

    public Double getIn2SldAttenuation() {
        return in2SldAttenuation;
    }

    public void setIn2SldAttenuation(Double in2SldAttenuation) {
        this.in2SldAttenuation = in2SldAttenuation;
    }

    public UnitInputPort getIn3() {
        return in3;
    }

    public Double getIn3SldAttenuation() {
        return in3SldAttenuation;
    }

    public void setIn3SldAttenuation(Double in3SldAttenuation) {
        this.in3SldAttenuation = in3SldAttenuation;
    }

    public UnitInputPort getIn4() {
        return in4;
    }

    public Double getIn4SldAttenuation() {
        return in4SldAttenuation;
    }

    public void setIn4SldAttenuation(Double in4SldAttenuation) {
        this.in4SldAttenuation = in4SldAttenuation;
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
