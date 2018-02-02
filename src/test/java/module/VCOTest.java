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
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void FrequencyTest(){
        Assert.assertTrue("The base frequency should be 440Hz", vco.getFrequency() == 440.0);
    }

    @Test
    public void OctaveTest(){
        Assert.assertTrue("The base octave should be 0", vco.getOctave() == 0);

        vco.increaseOctave(1);

        Assert.assertTrue("The octave should have increase by 1", vco.getOctave() == 1);

        vco.increaseOctave(150);

        Assert.assertTrue("The octave should have increase only by 2", vco.getOctave() == 3);

        vco.decreaseOctave(1);

        Assert.assertTrue("The octave should have decrease by 1", vco.getOctave() == 2);

        vco.decreaseOctave(150);

        Assert.assertTrue("The octave should have decrease only by 4", vco.getOctave() == -2);
    }

    @Test
    public void ReglageFinTest(){
        Assert.assertTrue("The base octave should be 0", vco.getReglageFin() == 0);

        vco.increaseReglageFin(1);

        Assert.assertTrue("The octave should have increase by 1", vco.getReglageFin() == 1);

        vco.increaseReglageFin(150);

        Assert.assertTrue("The octave should have increase only by 2", vco.getReglageFin() == 3);

        vco.decreaseReglageFin(1);

        Assert.assertTrue("The octave should have decrease by 1", vco.getReglageFin() == 2);

        vco.decreaseReglageFin(150);

        Assert.assertTrue("The octave should have decrease only by 4", vco.getReglageFin() == -2);
    }

}
