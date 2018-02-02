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
        Assert.assertTrue("The frequency should have doubled", vco.getFrequency() == 880);

        vco.increaseOctave(150);

        Assert.assertTrue("The octave should have increase only by 2", vco.getOctave() == 3);
        Assert.assertTrue("The frequency should have doubled 2 times", vco.getFrequency() == 3520);

        vco.decreaseOctave(1);

        Assert.assertTrue("The octave should have decrease by 1", vco.getOctave() == 2);
        Assert.assertTrue("The frequency should have been halved", vco.getFrequency() == 1760);

        vco.decreaseOctave(150);

        Assert.assertTrue("The octave should have decrease only by 4", vco.getOctave() == -2);
        Assert.assertTrue("The frequency should have been halved 4 times", vco.getFrequency() == 110);
    }

    @Test
    public void ReglageFinTest(){
        Assert.assertTrue("The base tuning should be 0", vco.getReglageFin() == 0);

        vco.increaseReglageFin(0.1);

        Assert.assertTrue("The tuning should have increase by 0.1", vco.getReglageFin() == 0.1);
        Assert.assertTrue("The frequency should have slighty increased", vco.getFrequency() == (440 * Math.pow(2,0.1)));

        vco.increaseReglageFin(150);

        Assert.assertTrue("The tuning should have increase only by 0.9", vco.getReglageFin() == 1);
        Assert.assertTrue("The frequency should have nearly doubled", vco.getFrequency() == 880);

        vco.decreaseReglageFin(0.1);

        Assert.assertTrue("The tuning should have decrease by 0.1", vco.getReglageFin() == 0.9);
        Assert.assertTrue("The frequency should have slighty decreased", vco.getFrequency() == (440 * Math.pow(2,0.9)));

        vco.decreaseReglageFin(150);

        Assert.assertTrue("The tuning should have decrease only by 0.9", vco.getReglageFin() == 0);
        Assert.assertTrue("The frequency should have returned to the base value", vco.getFrequency() == 440);
    }

}
