package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.UnitSource;
import controller.Obseurveur;
import controller.SubjectVCFLP;
import utils.PortType;
import utils.Tuple;

public class VCFLP extends Module implements UnitSource, Obseurveur<SubjectVCFLP>{

    private FilterLowPass filterLowPass;
    private UnitInputPort fm;
    private final int f0 = 372;


    public VCFLP(){
        this.filterLowPass = new FilterLowPass();
        this.fm = new UnitInputPort(PortType.FM.getType());
        addPort(fm);
    }




    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.equals(PortType.OUTPUT.getType())) return new Tuple<>(getPortByName(name), PortType.OUTPUT);
        if(name.equals(PortType.INPUT.getType())) return new Tuple<>(getPortByName(name), PortType.INPUT);
        if(name.equals(PortType.FM.getType())) return new Tuple<>(getPortByName(name), PortType.FM);
        return null;
    }

    @Override
    public UnitOutputPort getOutput() {
        return filterLowPass.output;
    }

    @Override
    public void update(SubjectVCFLP o) {
        if(o.getFrequency() == 0){
            filterLowPass.frequency.set(f0);
        }
        if(o.getFrequency() == Double.MAX_VALUE){
//            filterLowPass.frequency.set();
        }
        filterLowPass.frequency.set(o.getFrequency());
        filterLowPass.updateCoefficients();
    }

    @Override
    public Module getReference() {
        return this;
    }
}
