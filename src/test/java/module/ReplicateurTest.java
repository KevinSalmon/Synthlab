package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.Tuple;

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
        vco.getOutput().connect(rep.getIn());
        synth.add(vco);

    }

    @Test
    public void InputTest(){
        UnitInputPort input = rep.getIn();

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
                    rep.getIn().getValue() == rep.getOut1().getValue());
            Assert.assertTrue("The input values are not the same as the output2 values",
                    rep.getIn().getValue() == rep.getOut2().getValue());
            Assert.assertTrue("The input values are not the same as the output2 values",
                    rep.getIn().getValue() == rep.getOut3().getValue());

        }
    }

    @Test
    public void getReferenceTest(){
        Assert.assertTrue("getReference does not return the object", rep.getReference().equals(rep));
    }

    @Test
    public void getInputTest(){
        UnitInputPort newIn = new UnitInputPort("inputTest");
        rep.setIn(newIn);
        Assert.assertTrue("getIn does not return the input", rep.getIn().equals(newIn));
    }

    @Test
    public void getOut1Test(){
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        rep.setOut1(newOut);
        Assert.assertTrue("getIn does not return the input", rep.getOut1().equals(newOut));
    }

    @Test
    public void getOut2Test(){
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        rep.setOut2(newOut);
        Assert.assertTrue("getIn does not return the input", rep.getOut2().equals(newOut));
    }

    @Test
    public void getOut3Test(){
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        rep.setOut3(newOut);
        Assert.assertTrue("getIn does not return the input", rep.getOut3().equals(newOut));
    }

    @Test
    public void setInputTest(){
        UnitInputPort previousIn = rep.getIn();
        UnitInputPort newIn = new UnitInputPort("inputTest");
        rep.setIn(newIn);
        Assert.assertTrue("setIn does not set the input", !rep.getIn().equals(previousIn));
    }

    @Test
    public void setOut1Test(){
        UnitOutputPort previousOut1 = rep.getOut1();
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        rep.setOut1(newOut);
        Assert.assertTrue("setOut1 does not set the output1", !rep.getOut1().equals(previousOut1));
    }

    @Test
    public void setOut2Test(){
        UnitOutputPort previousOut2 = rep.getOut2();
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        rep.setOut2(newOut);
        Assert.assertTrue("setOut2 does not set the output1", !rep.getOut2().equals(previousOut2));
    }

    @Test
    public void setOut3Test(){
        UnitOutputPort previousOut3 = rep.getOut3();
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        rep.setOut3(newOut);
        Assert.assertTrue("setOut3 does not set the output1", !rep.getOut3().equals(previousOut3));
    }

    @Test
    public void getPortTest(){
        Assert.assertTrue("getPort(input) does not return the input",
                rep.getPort(PortType.INPUT.getType()).getLeft().getName().equals(
                        new Tuple<>(rep.getIn(), PortType.INPUT).getLeft().getName()));
        Assert.assertTrue("getPort(output1) does not return the output1",
                rep.getPort(PortType.OUTPUT1.getType()).getLeft().getName().equals(
                        new Tuple<>(rep.getOut1(), PortType.OUTPUT1).getLeft().getName()));
        Assert.assertTrue("getPort(output2) does not return the output2",
                rep.getPort(PortType.OUTPUT2.getType()).getLeft().getName().equals(
                        new Tuple<>(rep.getOut2(), PortType.OUTPUT2).getLeft().getName()));
        Assert.assertTrue("getPort(output3) does not return the output3",
                rep.getPort(PortType.OUTPUT3.getType()).getLeft().getName().equals(
                        new Tuple<>(rep.getOut3(), PortType.OUTPUT3).getLeft().getName()));

        Assert.assertNull("getPort(\"\") is not null", rep.getPort(""));
    }
}
