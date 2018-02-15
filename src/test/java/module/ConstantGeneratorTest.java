package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectConstantGenerator;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;

public class ConstantGeneratorTest {
    private Synthesizer synth;
    private ConstantGenerator constGenerator;

    @Before
    public void setUp(){
        synth = JSyn.createSynthesizer();
        synth.start();

        constGenerator = new ConstantGenerator();
        synth.add(constGenerator);
    }

    @Test
    public void getReferenceTest() {
        assertEquals(constGenerator, constGenerator.getReference());
    }

    @Test
    public void getPortTest() {
        assertNull(constGenerator.getPort(PortType.INPUT.getType()));
        assertNotNull(constGenerator.getPort(PortType.OUTPUT.getType()));
        assertNull(constGenerator.getPort(PortType.INPUTAM.getType()));
    }

    @Test
    public void outputTest() {
        assertNotNull(constGenerator.getOutput());
    }

    @Test
    public void defaultVoltageTest() {
        for (int i = 0; i < 100; i++) {
            constGenerator.generate();

            double[] values = constGenerator.getOutput().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(0.0, values[j]);
            }
        }
    }

    @Test
    public void voltage5VTest() {
        constGenerator.setVoltage(5.0);

        for (int i = 0; i < 100; i++) {
            constGenerator.generate();

            double[] values = constGenerator.getOutput().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(5.0 / 12, values[j]);
            }
        }
    }

    class SubjectConstantGeneratorTest implements SubjectConstantGenerator {
        private double voltage;

        public SubjectConstantGeneratorTest(double voltage) { this.voltage = voltage; }
        @Override public void register(Obseurveur o) { }
        @Override public void remove(Obseurveur o) { }
        @Override public void notifyObseurveur() { }
        @Override public double getVoltage() { return this.voltage; }
    }

    @Test
    public void updateVoltageTest() {
        SubjectConstantGenerator sub = new SubjectConstantGeneratorTest(5.0);
        constGenerator.update(sub);
        assertEquals(5.0, constGenerator.getVoltage());
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = constGenerator.getAllPorts();
        assertTrue(list.contains(PortType.OUTPUT));
    }
}
