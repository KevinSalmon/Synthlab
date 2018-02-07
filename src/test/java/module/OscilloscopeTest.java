package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OscilloscopeTest {

    private static Oscilloscope oscillo;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        oscillo = new Oscilloscope();
        synth.add(oscillo);
    }

    @Test
    public void initTest(){

    }

    @Test
    public void ResultatTest(){
        UnitInputPort input = oscillo.getInput();
        UnitOutputPort output = oscillo.getOutput();

        for (int i = 0; i < 10000; i++) {
            oscillo.generate();
            for (int j = 0; j < output.getValues().length; j++) {
                Assert.assertEquals("The output is not equals to the input",input.getValues()[j],output.getValues()[j],0.0);
            }
        }
    }
}
