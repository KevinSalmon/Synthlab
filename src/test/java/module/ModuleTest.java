package module;

import Exceptions.PortTypeException;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Test;

public class ModuleTest {



    @Test
    public void connectModulesTest() throws PortTypeException {
        VCO vco1 = new VCO();
        VCO vco2 = new VCO();

        vco1.setOutput(new UnitOutputPort("output"));
        vco2.setInput(new UnitInputPort("input"));

        vco1.connect(vco2, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) vco2.getPortByName(PortType.INPUT.getType())).isConnected());
    }


    @Test(expected = PortTypeException.class)
    public void connectModuleExceptionTest() throws PortTypeException {
        VCO vco1 = new VCO();
        VCO vco2 = new VCO();

        vco1.setOutput(new UnitOutputPort(("output")));
        vco2.setInput(new UnitInputPort("input"));

        vco2.connect(vco1, PortType.INPUT.getType(), PortType.OUTPUT.getType());
    }
}
