package module;

import exceptions.PortTypeException;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import java.util.Arrays;

public class ModuleTest {
    
    Synthesizer synth;
    
    @Before
    public void inti(){
        synth = JSyn.createSynthesizer();
    }
    
    @Test
    public void connectModulesTest() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        Synthesizer synth = JSyn.createSynthesizer();
        OutputModule outputModule = new OutputModule(synth);
        waitABit();

        vco1.setOutput(new UnitOutputPort("output"));

        vco1.connect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());
    }


    @Test(expected = PortTypeException.class)
    public void connectModuleExceptionTest() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        OutputModule outputModule = new OutputModule(JSyn.createSynthesizer());

        vco1.setOutput(new UnitOutputPort(("input")));

        outputModule.connect(vco1, PortType.INPUT.getType(), PortType.INPUT.getType());
    }

    @Test(expected = PortTypeException.class)
    public void connectModuleExceptionOtherTest() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        VCA vca = new VCA();

        vco1.setOutput(new UnitOutputPort(("output")));
        vca.connect(vco1, PortType.OUTPUT.getType(), PortType.OUTPUT.getType());
    }

    @Test
    public void disconnectModulesTest() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        Synthesizer synth = JSyn.createSynthesizer();
        OutputModule outputModule = new OutputModule(synth);
        waitABit();

        vco1.setOutput(new UnitOutputPort("output"));

        vco1.connect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());

        vco1.disconnect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be disconnected", !((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be disconnected", !((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());
    }

    @Test
    public void disconnectModulesOtherTest() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        synth.start();
        OutputModule outputModule = new OutputModule(synth);
        waitABit();

        vco1.setOutput(new UnitOutputPort("output"));

        vco1.connect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());

        vco1.disconnect(null, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());
    }

    @Test
    public void disconnectModulesOther2Test() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        Synthesizer synth = JSyn.createSynthesizer();
        OutputModule outputModule = new OutputModule(synth);
        waitABit();

        vco1.setOutput(new UnitOutputPort("output"));

        vco1.connect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());

        vco1.disconnect(outputModule, null, PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());
    }

    private void waitABit() throws InterruptedException {

            synth.start();

        synth.sleepFor(0.01);
    }

    @Test
    public void disconnectModulesOther3Test() throws PortTypeException, InterruptedException {
        VCO vco1 = new VCO();
        Synthesizer synth = JSyn.createSynthesizer();
        OutputModule outputModule = new OutputModule(synth);
        waitABit();

        vco1.setOutput(new UnitOutputPort("output"));

        vco1.connect(outputModule, PortType.OUTPUT.getType(), PortType.INPUT.getType());

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());

        vco1.disconnect(outputModule, PortType.OUTPUT.getType(), null);

        Assert.assertTrue("UnitPort must be connected", ((UnitOutputPort) vco1.getPortByName(PortType.OUTPUT.getType())).isConnected());
        Assert.assertTrue("UnitPort must be connected", ((UnitInputPort) outputModule.getPortByName(PortType.INPUT.getType())).isConnected());
    }
}
