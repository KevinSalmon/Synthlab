package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

public class VCATest {
    public static VCA vca;
    public static VCO vco;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        vca = new VCA();
        synth.add(vca);

        vco = new VCO();
        synth.add(vco);
    }

    @Test
    public void OutputTest(){
        UnitOutputPort output = vca.getOutput();

        Assert.assertTrue("The name of the output is not 'output'",output.getName().equals(PortType.OUTPUT.toString()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void AmNullOutputTest(){
        Boolean empty = true;
        Integer i = 0;

        double[] output = vca.getOutput().getValues();
        while(empty && i < output.length) {
            if (output[i] != 0.0) {
                empty = false;
            }
            i++;
        }
        Assert.assertTrue("Am is not connected. The output should be empty.", empty);
    }

   /* @Test
    public void UseCaseOutputTest() {
        // this.am.getAmplitude() == 5.0 && a0 == 0.0
        vco.getOutput().connect(vca.getInput());
    }*/
}
