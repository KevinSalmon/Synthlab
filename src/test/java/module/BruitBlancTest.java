package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.WhiteNoise;
import ihm.BruitBlancController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;
import utils.Tuple;

public class BruitBlancTest {

    public static BruitBlancModule bruitBlanc;
    public static VCO vco;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        bruitBlanc = new BruitBlancModule();
        synth.add(bruitBlanc);

    }

    @Test
    public void OutputTest(){
        UnitOutputPort output = bruitBlanc.getOut();

        Assert.assertTrue("The name of the output is not 'output'",output.getName().equals(PortType.OUTPUT.getType()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void getReferenceTest(){
        Assert.assertTrue("getReference does not return the object", bruitBlanc.getReference().equals(bruitBlanc));
    }


    @Test
    public void getOutTest(){
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        bruitBlanc.setOut(newOut);
        Assert.assertTrue("getOut does not return the out", bruitBlanc.getOut().equals(newOut));
    }

    @Test
    public void setOutTest(){
        UnitOutputPort previousOut = bruitBlanc.getOut();
        UnitOutputPort newOut = new UnitOutputPort("outTest");
        bruitBlanc.setOut(newOut);
        Assert.assertTrue("setOut1 does not set the output1", !bruitBlanc.getOut().equals(previousOut));
    }

    @Test
    public void getWhiteNoiseTest(){
        WhiteNoise whiteNoise = new WhiteNoise();
        bruitBlanc.setWhiteNoise(whiteNoise);
        Assert.assertTrue("getWhiteNoise does not return the whitenoise", bruitBlanc.getWhiteNoise().equals(whiteNoise));
    }

    @Test
    public void setWhiteNoiseTest(){
        WhiteNoise previousWhiteNoise = bruitBlanc.getWhiteNoise();
        WhiteNoise newWhiteNoise = new WhiteNoise();
        bruitBlanc.setWhiteNoise(newWhiteNoise);
        Assert.assertTrue("setWhiteNoise does not set the WhiteNoise", !bruitBlanc.getWhiteNoise().equals(previousWhiteNoise));
    }

    @Test
    public void getPortTest(){
        Assert.assertTrue("getPort(output) does not return the output1",
                bruitBlanc.getPort(PortType.OUTPUT.getType()).getLeft().getName().equals(
                        new Tuple<>(bruitBlanc.getOut(), PortType.OUTPUT).getLeft().getName()));

        Assert.assertNull("getPort(\"\") is not null", bruitBlanc.getPort(""));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void updateTest(){
        BruitBlancController bruitBlancController = new BruitBlancController();
        bruitBlanc.update(bruitBlancController);
    }

    @Test
    public void generateTest(){
        bruitBlanc.generate();
        Assert.assertNotNull("generate does not generate values", bruitBlanc.getOut().getValues());
    }
}
