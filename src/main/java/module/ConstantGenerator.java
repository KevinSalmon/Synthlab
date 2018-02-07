package module;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectConstantGenerator;
import utils.PortType;
import utils.Tuple;

public class ConstantGenerator extends Module implements UnitSource, Obseurveur<SubjectConstantGenerator> {
    private UnitOutputPort out;
    private double voltage = 0.0;

    public ConstantGenerator() {
        this.out = new UnitOutputPort();
        addPort(this.out, PortType.OUTPUT.getType());
    }

    @Override
    public UnitOutputPort getOutput() {
        return this.out;
    }

    public double getVoltage() {
        return this.voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] outputs = out.getValues();
        for (int i = start; i < limit; i++) {
            outputs[i] = this.voltage / 12;
        }
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)) return new Tuple(getPortByName(name),PortType.OUTPUT);
        return null;
    }

    @Override
    public void update(SubjectConstantGenerator o) {
        this.voltage = o.getVoltage();
    }

    @Override
    public Module getReference() {
        return this;
    }
}

