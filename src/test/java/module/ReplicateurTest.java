package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReplicateurTest {

    public static Replicateur rep;
    public static VCO vco;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        rep = new Replicateur();
        synth.add(rep);

        vco = new VCO();
        vco.getOutput().connect(rep.getInput());
        synth.add(vco);

    }

    @Test
    public void InputTest(){
        UnitInputPort input = rep.getInput();

        Assert.assertTrue("The name of the input is not 'input' but " + input.getName(),input.getName().equals(PortType.INPUT.getType()));
        Assert.assertTrue("The input must have only one part",input.getNumParts() == 1);
        Assert.assertTrue("The input should have one connection from now", input.isConnected());
    }

    @Test
    public void Output1Test(){
        UnitOutputPort output = rep.getOut1();

        Assert.assertTrue("The name of the output is not 'output1'",output.getName().equals(PortType.OUTPUT1.getType()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void Output2Test(){
        UnitOutputPort output = rep.getOut2();

        Assert.assertTrue("The name of the output is not 'output2'",output.getName().equals(PortType.OUTPUT2.getType()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void Output3Test(){
        UnitOutputPort output = rep.getOut3();

        Assert.assertTrue("The name of the output is not 'output3'",output.getName().equals(PortType.OUTPUT3.getType()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void OtherModuleOutputToOutputTest() {

        for (int i=0; i < 10000 ; i++) {
            vco.generate();
            rep.generate();

            Assert.assertTrue("The other module output values are not the same as the output1 values",
                    vco.getOutput().getValue() == rep.getOut1().getValue());
            Assert.assertTrue("The other module output values are not the same as the output2 values",
                    vco.getOutput().getValue() == rep.getOut2().getValue());
            Assert.assertTrue("The other module output values are not the same as the output2 values",
                    vco.getOutput().getValue() == rep.getOut3().getValue());

        }
    }

    @Test
    public void InputToOutputTest() {

        for (int i=0; i < 10000 ; i++) {
            vco.generate();
            rep.generate();

            Assert.assertTrue("The input values are not the same as the output1 values",
                    rep.getInput().getValue() == rep.getOut1().getValue());
            Assert.assertTrue("The input values are not the same as the output2 values",
                    rep.getInput().getValue() == rep.getOut2().getValue());
            Assert.assertTrue("The input values are not the same as the output2 values",
                    rep.getInput().getValue() == rep.getOut3().getValue());

        }
    }
}
