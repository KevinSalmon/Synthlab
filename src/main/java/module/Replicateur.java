package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import controller.Obseurveur;
import controller.SubjectReplicateur;
import utils.PortType;
import utils.Tuple;

public class Replicateur extends Module implements Obseurveur<SubjectReplicateur> {

    private UnitInputPort in;
    private UnitOutputPort out1;
    private UnitOutputPort out2;
    private UnitOutputPort out3;

    public Replicateur(){
        this.in = new UnitInputPort(PortType.INPUT.getType());
        addPort(in, PortType.INPUT.getType());
        this.out1 = new UnitOutputPort(PortType.OUTPUT1.getType());
        addPort(out1, PortType.OUTPUT1.getType());
        this.out2 = new UnitOutputPort(PortType.OUTPUT2.getType());
        addPort(out2, PortType.OUTPUT2.getType());
        this.out3 = new UnitOutputPort(PortType.OUTPUT3.getType());
        addPort(out3, PortType.OUTPUT3.getType());
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {

        if(PortType.INPUT.getType().equals(name))
            return new Tuple(getPortByName(name),PortType.INPUT);
        if(name.equals(PortType.OUTPUT1.getType()))
            return new Tuple(getPortByName(name),PortType.OUTPUT1);
        if(name.equals(PortType.OUTPUT2.getType()))
            return new Tuple(getPortByName(name),PortType.OUTPUT2);
        if(name.equals(PortType.OUTPUT3.getType()))
            return new Tuple(getPortByName(name),PortType.OUTPUT3);

        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] inputs = in.getValues();
        double[] outputs1 = out1.getValues();
        double[] outputs2 = out2.getValues();
        double[] outputs3 = out3.getValues();

        System.arraycopy(inputs, start, outputs1, start, limit);
        System.arraycopy(inputs, start, outputs2, start, limit);
        System.arraycopy(inputs, start, outputs3, start, limit);
    }

    /**
     * Getters et Setters
     */

    public UnitInputPort getInput() {
        return in;
    }

    @Override
    public void update(SubjectReplicateur o) { }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitInputPort getIn() {
        return in;
    }

    public void setIn(UnitInputPort in) {
        this.in = in;
    }

    public UnitOutputPort getOut1() {
        return out1;
    }

    public void setOut1(UnitOutputPort out1) {
        this.out1 = out1;
    }

    public UnitOutputPort getOut2() {
        return out2;
    }

    public void setOut2(UnitOutputPort out2) {
        this.out2 = out2;
    }

    public UnitOutputPort getOut3() {
        return out3;
    }

    public void setOut3(UnitOutputPort out3) {
        this.out3 = out3;
    }
}
