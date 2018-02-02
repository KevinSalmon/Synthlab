package utils;

import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import org.junit.Assert;
import org.junit.Test;

public class OscillatorFactoryTest {

    private OscillatorFactory oscillatorFactory;


    @Test
    public void triangleOscillatorTest(){
        Assert.assertEquals("Must be a triangleOscillator", TriangleOscillator.class, oscillatorFactory.createOscillator(OscillatorType.TRIANGLE).getClass());
    }

    @Test
    public void sawToothOscillatorTest(){
        Assert.assertEquals("Must be a sawToothOscillator", SawtoothOscillator.class, oscillatorFactory.createOscillator(OscillatorType.SAWTOOTH).getClass());

    }

    @Test
    public void squareOscillatorTest(){
        Assert.assertEquals("Must be a squareOscillator", SquareOscillator.class, oscillatorFactory.createOscillator(OscillatorType.SQUARE).getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void OscillatorExcecption(){
        oscillatorFactory.createOscillator(OscillatorType.valueOf("erreur"));
    }
}
