package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import controller.Obseurveur;
import controller.SubjectSeq;
import utils.PortType;
import utils.Tuple;

public class Sequenceur extends Module implements Obseurveur<SubjectSeq>{
    private UnitInputPort inputPort;
    private UnitOutputPort outputPort;

    public Sequenceur(){
        inputPort = new UnitInputPort(PortType.INPUT.getType());
        outputPort = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(inputPort);
        addPort(outputPort);
    }
    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.contains(PortType.INPUT.getType())) return new Tuple<>(inputPort, PortType.INPUT);
        if(name.contains(PortType.OUTPUT.getType())) return new Tuple<>(outputPort, PortType.OUTPUT);
        return null;
    }

    @Override
    public void update(SubjectSeq o) {

    }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitOutputPort getOutputPort(){
        return outputPort;
    }

    public UnitInputPort getInputPort() {
        return inputPort;
    }
}
