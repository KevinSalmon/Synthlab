package module;

import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VCOTest {
    public static VCO vco;

    @Before
    public void init(){vco = new VCO();}

    @Test
    public void OutputTest(){
        UnitOutputPort output = vco.getOutput();

        Assert.assertTrue("The name of the output is not 'output'",output.getName().equals("output"));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", output.isConnected());
    }

    @Test
    public void FrequencyTest(){
//        Assert.assertTrue("The base frequency should be 440Hz", vco.getAudioSignal().get);
    }

}
