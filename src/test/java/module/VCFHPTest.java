package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import controller.Obseurveur;
import controller.SubjectVCF;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import static junit.framework.TestCase.assertEquals;

/**
 * Classe de test for VCFHP
 */
public class VCFHPTest {

    /**
     * Test class for the update function
     */
    private class SubjectVCFHPTest implements SubjectVCF{
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

    private VCF vcfhp;
    private Synthesizer synth;

    @Before
    public void init(){
        synth = JSyn.createSynthesizer();

        vcfhp = new VCF(false);
        synth.add(vcfhp);
    }

    /**
     * Test when the VCFHP frequency is infinite
     */
    @Test
    public void VCFHPGenerationInfinityTest(){
        vcfhp.getFilterPass().frequency.set(Double.MAX_VALUE);
        vcfhp.generate();

        assertEquals(vcfhp.getInput().getValues().length, vcfhp.getOutput().getValues().length);
        for(int i = 0; i < vcfhp.getInput().getValues().length; i++){
            assertEquals(vcfhp.getInput().getValues()[i], vcfhp.getOutput().getValues()[i]);

        }

    }

    /**
     * Test when the VCFHP frequency is null
     */
    @Test
    public void VCFHPGenerationFrequencyNullTest(){
        vcfhp.getFilterPass().frequency.set(0.0);
        vcfhp.generate();

        for(int i = 0; i < vcfhp.getOutput().getValues().length; i++){
            assertEquals(vcfhp.getOutput().getValues()[i], 0.0);
        }

    }
    

    /**
     * Test the retrieve of a port
     */
    @Test
    public void getPortTest(){
        assertEquals("output port and returned port are not equals", vcfhp.getOutput(), vcfhp.getPort(PortType.OUTPUT.getType()).getLeft());
        assertEquals( "input port and returned port are not equals", vcfhp.getInput(), vcfhp.getPort(PortType.INPUT.getType()).getLeft());
        assertEquals("fm port and returned port are not equals", vcfhp.getFm(), vcfhp.getPort(PortType.INPUTFM.getType()).getLeft());
        Assert.assertNull(vcfhp.getPort("NULL"));
    }



    /**
     * Test the update with a frequency from the subject
     */
    @Test
    public void updateFrequencyTest(){
        VCO vco = new VCO();
        vco.getOutput().connect(vcfhp.getInput());
        double [] outputVCF = vcfhp.getOutput().getValues();
        double [] outputVCO = vco.getOutput().getValues();
        SubjectVCFHPTest o = new SubjectVCFHPTest();
        o.setFrequency(5.0);
        assertEquals(5.0, o.getFrequency());
        vcfhp.update(o);
        vcfhp.generate();
        for(int i = 0; i < outputVCF.length;i++) assertEquals(outputVCF[i], outputVCO[i]);

    }



    /**
     * Test if the reference works
     */
    @Test
    public void referenceTest(){
        assertEquals(vcfhp, vcfhp.getReference());
    }

    /**
     * Test the update of the resonance
     */
    @Test
    public void updateResonanceTest(){
        SubjectVCFHPTest subjectVCFHPTest = new SubjectVCFHPTest();
        subjectVCFHPTest.setResonance(4.0);
        vcfhp.update(subjectVCFHPTest);

        assertEquals(subjectVCFHPTest.getResonance(), vcfhp.getResonance());
    }

    /**
     * Test when the resonance passed is the same as the resonance set
     */
    @Test
    public void updateResonanceWithoutChangesTest(){
        SubjectVCFHPTest subjectVCFHPTest = new SubjectVCFHPTest();
        double res = 4.0;
        vcfhp.setResonance(res);
        subjectVCFHPTest.setResonance(res);
        vcfhp.update(subjectVCFHPTest);

        assertEquals(subjectVCFHPTest.getResonance(), vcfhp.getResonance());
    }

    @Test
    public void getFrequencyTest(){
        assertEquals(vcfhp.getFilterPass().frequency.get(), vcfhp.getFrequency());
    }

    @Test
    public void setFrequencyTest(){
        vcfhp.getFilterPass().frequency.set(440.0);
        assertEquals(vcfhp.getFilterPass().frequency.get(), vcfhp.getFrequency());
    }


}
