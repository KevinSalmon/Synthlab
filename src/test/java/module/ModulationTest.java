package module;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ModulationTest implements AbstractSignalTest {

    Signal signal;

    @Override
    @Test
    public void increaseUntilLimit() {
        signal = new ModulationSignal();

        for (int i= 0; i <=10;i++){
            signal.addVoltage(1);
            Assert.assertTrue("Voltage must be less than : "+i, signal.getVolt() <= 10);
        }

        signal = new ModulationSignal();
        for (int i= -10; i <=0;i++){
            signal.addVoltage(-1);
            Assert.assertTrue("Voltage must be less than : "+i, signal.getVolt() >=-10);
        }


    }

    @Override
    @Test
    public void moreThanLimit() {
        signal = new ModulationSignal();
        signal.addVoltage(10);
        assertEquals("Voltage max : 10",  10.0,signal.getVolt());
        signal.addVoltage(1);
        assertEquals("Voltage max : 10",  10.0,signal.getVolt());

    }

    @Override
    @Test
    public void lessThanLimit() {
        signal = new ModulationSignal();
        signal.addVoltage(-10);
        assertEquals("Voltage max : -10", -10.0, signal.getVolt());
        signal.addVoltage(-1);
        assertEquals("Voltage max : -10", -10.0, signal.getVolt());

    }

    @Override
    @Test
    public void frequencyTest() {
        signal = new ModulationSignal();

        assertEquals("BandWith must be 22KHz", signal.getBandWidth(), (signal.getFrequencyMax() - signal.getFrequencyMin()));

    }
}
