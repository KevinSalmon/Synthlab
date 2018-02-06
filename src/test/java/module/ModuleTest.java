package module;

import Exceptions.PortTypeException;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Test;

public class ModuleTest {



    @Test
    public void connectModulesTest() throws PortTypeException {
        VCO vco1 = new VCO();
        Synthesizer synth = JSyn.createSynthesizer();
        OutputModule outputModule = new OutputModule(synth);

        vco1.setOutput(new UnitOutputPort("output"));

        vco1.connect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());
    }


    @Test(expected = PortTypeException.class)
    public void connectModuleExceptionTest() throws PortTypeException {
        VCO vco1 = new VCO();
        OutputModule outputModule = new OutputModule(JSyn.createSynthesizer());

        vco1.setOutput(new UnitOutputPort(("output")));

        outputModule.connect(vco1, PortType.INPUT.getType(), PortType.OUTPUT.getType());
    }
}
