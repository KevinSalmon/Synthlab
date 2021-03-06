package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectVCF;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Classe de test for VCFLP
 */
public class VCFLPTest {

    /**
     * Test class for the update function
     */
    private class SubjectVCFLPTestLP implements SubjectVCF{
        private double frequency;
        private double resonance;
        @Override
        public double getFrequency() {
            return frequency;
        }

        @Override
        public double getResonance() {
            return resonance;
        }



        public void setResonance(Double res){
            this.resonance = res;
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

    private VCF vcflp;
    private Synthesizer synth;

    @Before
    public void init(){
        synth = JSyn.createSynthesizer();

        vcflp = new VCF(true);
        synth.add(vcflp);
    }

    /**
     * Test when the VCFLP frequency is infinite
     */
    @Test
    public void VCFLPGenerationInfinityTest(){
        vcflp.getFilterPass().frequency.set(Double.MAX_VALUE);
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
        vcflp.getFilterPass().frequency.set(0.0);
        vcflp.generate();

        for(int i = 0; i < vcflp.getOutput().getValues().length; i++){
            assertEquals(vcflp.getOutput().getValues()[i], 0.0);
        }

    }
    

    /**
     * Test the retrieve of a port
     */
    @Test
    public void getPortTest(){
        assertEquals("output port and returned port are not equals", vcflp.getOutput(), vcflp.getPort(PortType.OUTPUT.getType()).getLeft());
        assertEquals( "input port and returned port are not equals", vcflp.getInput(), vcflp.getPort(PortType.INPUT.getType()).getLeft());
        assertEquals("fm port and returned port are not equals", vcflp.getFm(), vcflp.getPort(PortType.INPUTFM.getType()).getLeft());
        Assert.assertNull(vcflp.getPort("NULL"));
    }



    /**
     * Test the update with a frequency from the subject
     */
    @Test
    public void updateFrequencyTest(){
        VCO vco = new VCO();
        vco.getOutput().connect(vcflp.getInput());
        double [] outputVCF = vcflp.getOutput().getValues();
        double [] outputVCO = vco.getOutput().getValues();
        SubjectVCFLPTestLP o = new SubjectVCFLPTestLP();
        o.setFrequency(5.0);
        assertEquals(5.0, o.getFrequency());
        vcflp.update(o);
        vcflp.generate();
        for(int i = 0; i < outputVCF.length;i++) assertEquals(outputVCF[i], outputVCO[i]);

    }



    /**
     * Test if the reference works
     */
    @Test
    public void referenceTest(){
        assertEquals(vcflp, vcflp.getReference());
    }

    /**
     * Test the update of the resonance
     */
    @Test
    public void updateResonanceTest(){
        SubjectVCFLPTestLP subjectVCFLPTest = new SubjectVCFLPTestLP();
        subjectVCFLPTest.setResonance(4.0);
        vcflp.update(subjectVCFLPTest);

        assertEquals(subjectVCFLPTest.getResonance(), vcflp.getResonance());
    }

    /**
     * Test when the resonance passed is the same as the resonance set
     */
    @Test
    public void updateResonanceWithoutChangesTest(){
        SubjectVCFLPTestLP subjectVCFLPTest = new SubjectVCFLPTestLP();
        double res = 4.0;
        vcflp.setResonance(res);
        subjectVCFLPTest.setResonance(res);
        vcflp.update(subjectVCFLPTest);

        assertEquals(subjectVCFLPTest.getResonance(), vcflp.getResonance());
    }

    @Test
    public void frequencyTest(){
        assertEquals(vcflp.getFrequency(), vcflp.getFilterPass().frequency.get());
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = vcflp.getAllPorts();
        assertTrue(list.contains(PortType.INPUT));
        assertTrue(list.contains(PortType.INPUTFM));
        assertTrue(list.contains(PortType.OUTPUT));
    }
}
