package module;

import ihm.observer.Obseurveur;
import ihm.observer.SubjectSeq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the sequencer
 */
public class SequenceurTest {
    private Sequenceur sequenceur;

    private class SubjectSeqTest implements SubjectSeq{

        @Override
        public int getCurrentSlider() {
            return 1;
        }

        @Override
        public double getSliderValue(int i) {
            return 5.0;
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


    @Before
    public void init(){
        sequenceur = new Sequenceur();
    }

    /**
     * Test the retrieve of the ports
     */
    @Test
    public void getPortByNameTest(){
        Assert.assertEquals("Ports must be equals", sequenceur.getInputPort(), sequenceur.getPort(PortType.INPUT.getType()).getLeft());
        Assert.assertEquals("Ports must be equals", sequenceur.getOutputPort(), sequenceur.getPort(PortType.OUTPUT.getType()).getLeft());
        Assert.assertNull("Does not exist", sequenceur.getPort("fake"));
    }

    /**
     * Test the retrieve of the reference
     */
    @Test
    public void getReference(){
        Assert.assertEquals("Must be equals", sequenceur, sequenceur.getReference());
    }

    /**
     * Test the retrieve of the input and output port
     */
    @Test
    public void getPortTest(){
        Assert.assertEquals("Output ports must be equals", sequenceur.getOutputPort(), sequenceur.getPort(PortType.OUTPUT.getType()).getLeft());
        Assert.assertEquals("Input ports must be equals ", sequenceur.getInputPort(), sequenceur.getPort(PortType.INPUT.getType()).getLeft());

    }

    /**
     * Test the update of the sliders values in the seq
     */
    @Test
    public void updateTest(){
        SubjectSeqTest subjectSeqTest = new SubjectSeqTest();
        sequenceur.update(subjectSeqTest);
        Assert.assertEquals("Values "+(subjectSeqTest.getCurrentSlider()-1)+" must be equals", subjectSeqTest.getSliderValue(0), sequenceur.getValues()[0], 0.1);

    }

    @Test
    public void resetToOneTest(){
        SubjectSeqTest subjectSeqTest = new SubjectSeqTest();
        sequenceur.resetToOne(subjectSeqTest);
        Assert.assertEquals("Actual value must be equals to zero",0, sequenceur.getActualValue());


    }

    /**
     * Test the generate with a 0.0 voltage
     */
    @Test
    public void generateTest(){
        double[] inputValues = sequenceur.getInputPort().getValues();
        for (int i = 0; i < inputValues.length; i ++) inputValues[i] = 0.0;
        SubjectSeqTest subjectSeqTest = new SubjectSeqTest();
        sequenceur.update(subjectSeqTest);
        sequenceur.generate(0, inputValues.length);
        double[] outValues = sequenceur.getOutputPort().getValues();
        for(int i =0; i < inputValues.length; i ++){
            Assert.assertEquals(outValues[i], sequenceur.getValues()[0]/12.0, 0.1);
        }


    }

    /**
     * Test the generate with a 1.0 voltage
     */
    @Test
    public void generateNextSliderTest() {
        double[] inputValues = sequenceur.getInputPort().getValues();
        for (int i = 0; i < inputValues.length; i++) inputValues[i] = 1.0;
        Assert.assertFalse("Must be false", sequenceur.isCheck());
        SubjectSeqTest subjectSeqTest = new SubjectSeqTest();
        sequenceur.update(subjectSeqTest);
        sequenceur.generate(0, inputValues.length);
        double[] outValues = sequenceur.getOutputPort().getValues();
        for (int i = 0; i < inputValues.length; i++) {
            System.out.println(outValues[i]);
            Assert.assertEquals(outValues[i], sequenceur.getValues()[1], 0.1);
        }
        Assert.assertTrue("Must be true", sequenceur.isCheck());
    }

    /**
     * Test the generate with a -1.0 voltage
     */
    @Test
    public void generateNegativeValuesTest() {
        double[] inputValues = sequenceur.getInputPort().getValues();
        for (int i = 0; i < inputValues.length; i++) inputValues[i] = -1.0;
        double[] outValues = sequenceur.getOutputPort().getValues();
        for (int i = 0; i < inputValues.length; i++) {
            Assert.assertEquals(outValues[i], sequenceur.getValues()[0], 0.1);
        }
    }

    /**
     * Test the retrieve of the actualValue
     */
    @Test
    public void getActualValueTest(){
        Assert.assertEquals(sequenceur.getActualValue(), 0);
    }


    @Test
    public void getAllPortsTest() {
        List<PortType> list = sequenceur.getAllPorts();
        assertTrue(list.contains(PortType.INPUT));
        assertTrue(list.contains(PortType.OUTPUT));
    }

}
