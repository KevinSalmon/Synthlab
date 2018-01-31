package module;

import org.junit.Assert;
import org.junit.Test;

public class AudioSignalTest implements AbstractSignalTest {

    private Signal signal;


    @Override
    @Test
    public void increaseUntilLimit() {
        signal = new AudioSignal(0, 0.5, 440);
        int i = 0;
        for (; i <= 5; i++) {
            signal.addVoltage(1);
            Assert.assertTrue("Under or equals 5 volts", signal.getVolt() <= 5);
        }

        signal = new AudioSignal();
        for (i= -10; i <=0;i++){
            signal.addVoltage(-1);
            Assert.assertTrue("Voltage must be less than : "+i, signal.getVolt() >=-5);
        }



    }

    @Override
    @Test
    public void moreThanLimit(){
        signal = new AudioSignal(4, 0.5, 440);

        signal.addVoltage(1);
        Assert.assertTrue("Equals 5 volt", signal.getVolt() == 5);

        signal.addVoltage(1);
        Assert.assertTrue("Equals 5 volt", signal.getVolt() == 5);

    }

    @Override
    @Test
    public void lessThanLimit(){
        signal = new AudioSignal(-4, 0.5, 440);

        signal.addVoltage(-1);
        Assert.assertTrue("Equals 5 volt", signal.getVolt() == -5);

        signal.addVoltage(-1);
        Assert.assertTrue("Equals 5 volt", signal.getVolt() == -5);

    }

    @Override
    @Test
    public void frequencyTest(){
        signal = new AudioSignal(4, 0.5, 440);
        Assert.assertTrue(signal.getFrequencyMin() == 440);
        Assert.assertTrue((signal.getFrequencyMax() - signal.getFrequencyMin() == signal.getBandWidth()));
    }
}
