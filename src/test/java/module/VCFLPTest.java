package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import controller.Obseurveur;
import controller.SubjectVCFLP;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import signal.Signal;
import utils.PortType;

import static junit.framework.TestCase.assertEquals;

/**
 * Classe de test for VCFLP
 */
public class VCFLPTest {

    /**
     * Test class for the update function
     */
    private class SubjectVCFLPTest implements SubjectVCFLP{
        private double frequency;
        @Override
        public double getFrequency() {
            return frequency;
        }


        public void setFrequency(double frequency) {
            this.frequency = frequency;
        }

        @Override
        public void register(Obseurveur o) {

        }

        @Override
        public void remove(Obseurveur o) {

        }

        @Override
        public void notifyObseurveur() {

        }
    }

    private VCFLP vcflp;
    private Synthesizer synth;

    @Before
    public void init(){
        synth = JSyn.createSynthesizer();

        vcflp = new VCFLP();
        synth.add(vcflp);
    }

    /**
     * Test when the VCFLP frequency is infinite
     */
    @Test
    public void VCFLPGenerationInfinityTest(){
        vcflp.getFilterLowPass().frequency.set(Double.MAX_VALUE);
        vcflp.generate();

        assertEquals(vcflp.getInput().getValues().length, vcflp.getOutput().getValues().length);
        for(int i = 0; i < vcflp.getInput().getValues().length; i++){
            assertEquals(vcflp.getInput().getValues()[i], vcflp.getOutput().getValues()[i]);

        }

    }

    /**
     * Test when the VCFLP frequency is null
     */
    @Test
    public void VCFLPGenerationFrequencyNullTest(){
        vcflp.getFilterLowPass().frequency.set(0.0);
        vcflp.generate();

        for(int i = 0; i < vcflp.getOutput().getValues().length; i++){
            assertEquals(vcflp.getOutput().getValues()[i], 0.0);
        }

    }

    /**
     * Test when the VCFLP frequency is neither null nor zero
     */
    @Test
    public void VCFLPGenerationFrequencyOtherTest(){
        vcflp.getFilterLowPass().frequency.set(440.0);
        vcflp.generate();

        assertEquals(vcflp.getFilterLowPass().frequency.get(), Math.pow(2,vcflp.getSignal().getVolt())* 440);

    }

    /**
     * Test the retrieve of a port
     */
    @Test
    public void getPortTest(){
        assertEquals("output port and returned port are not equals", vcflp.getOutput(), vcflp.getPort(PortType.OUTPUT.getType()).getLeft());
        assertEquals( "input port and returned port are not equals", vcflp.getInput(), vcflp.getPort(PortType.INPUT.getType()).getLeft());
        assertEquals("fm port and returned port are not equals", vcflp.getFm(), vcflp.getPort(PortType.FM.getType()).getLeft());
        Assert.assertNull(vcflp.getPort("NULL"));
    }

    /**
     * Test the update with a 0 frequency from the subject
     */
    @Test
    public void updateZeroFrequencyTest(){
        SubjectVCFLPTest o = new SubjectVCFLPTest();
        o.setFrequency(0.0);
        vcflp.update(o);
        assertEquals("Frequency must be at 0", 0.0, vcflp.getFilterLowPass().frequency.get());
    }

    /**
     * Test the update with a infinite frequency from the subject
     */
    @Test
    public void updateMaxValueFrequencyTest(){
        SubjectVCFLPTest o = new SubjectVCFLPTest();
        o.setFrequency(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, o.getFrequency());
        vcflp.update(o);
        assertEquals("Frequency must be at Double.Max_Value", Double.MAX_VALUE, vcflp.getFilterLowPass().frequency.get());
    }

    /**
     * Test when the Subject frequency is neither null nor zero
     */
    @Test
    public void updateWhateverFrequencyTest(){
        SubjectVCFLPTest o = new SubjectVCFLPTest();
        o.setFrequency(220.0);
        assertEquals( 220.0, o.getFrequency());
        vcflp.update(o);
        assertEquals("Frequency must be at 220.0", 220.0, vcflp.getFilterLowPass().frequency.get());
    }

    @Test
    public void referenceTest(){
        assertEquals(vcflp, vcflp.getReference());
    }

    @Test
    public void increaseFreqTest(){
        Double freq =vcflp.getFilterLowPass().frequency.get();
        vcflp.increaseFrequency();
        assertEquals(2 * freq, vcflp.getFilterLowPass().frequency.get());
    }
    @Test
    public void decreaseFreqTest(){
        Double freq =vcflp.getFilterLowPass().frequency.get();
        vcflp.decreaseFrequency();
        assertEquals( freq / 2, vcflp.getFilterLowPass().frequency.get());
    }

}
