package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectVCA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;

public class VCATest {
    public static VCA vca;
    public static VCO vco;
    public static ConstantGenerator constGen;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        vca = new VCA();
        synth.add(vca);

        constGen = new ConstantGenerator();
        constGen.getOutput().connect(vca.getAm());

        vco = new VCO();
        vco.getOutput().connect(vca.getInput());
        synth.add(vco);
    }

    @Test
    public void getReferenceTest() {
        assertEquals(vca, vca.getReference());
    }

    @Test
    public void getInputPortTest() {
        assertNotNull(vca.getPort(PortType.INPUT.getType()));
        assertNotNull(vca.getPort(PortType.OUTPUT.getType()));
        assertNotNull(vca.getPort(PortType.INPUTAM.getType()));
        assertNull(vca.getPort("FAKE"));
    }

    @Test
    public void getA0Test() {
        vca.setA0(-4.3);
        assertEquals(-4.3, vca.getA0());
    }

    @Test
    public void OutputTest(){
        UnitOutputPort output = vca.getOutput();

        Assert.assertTrue("The name of the output is not 'OUTPUT'",output.getName().equals(PortType.OUTPUT.getType()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }


    /****************
     * US
     ***************/

    // a0 peut être modifié uniquement lorsque am = 5V
    @Test
    public void a05VOutputTest() {
        constGen.setVoltage(5.0);
        vca.setA0(0.0);

        assertEquals(5.0, constGen.getVoltage());
        assertEquals(0.0, vca.getA0());

        constGen.generate();
        vco.generate();
        vca.generate();

        assertEquals(vco.getOutput().getValue() * (constGen.getOutput().getValue()*12/5), vca.getOutput().getValue(), 0.0);
    }

    @Test
    public void a04VOutputTest() {
        constGen.setVoltage(4.0);
        vca.setA0(0.0);

        assertEquals(4.0, constGen.getVoltage());
        assertEquals(0.0, vca.getA0());

        constGen.generate();
        vco.generate();
        vca.generate();

        assertEquals(vco.getOutput().getValue() * (constGen.getOutput().getValue()*12/5), vca.getOutput().getValue(), 0.0);
    }

    // lorsque que l’entrée am est déconnectée ou nulle, le gain du VCA est nul (pas de signal en sortie)
    @Test
    public void amNullOutputTest() {
        for (int i = 0; i < 1000; i++) {
            vca.generate();
            vco.generate();

            double[] values = vca.getOutput().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(values[j], 0.0,0.0);
            }
        }
    }

    @Test
    public void am0OutputTest() {
        for (int i = 0; i < 1000; i++) {
            constGen.generate();
            vca.generate();
            vco.generate();

            double[] values = vca.getOutput().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(values[j], 0.0,0.0);
            }
        }
    }

    // lorsque am vaut 5 V et a0 vaut 0 dB le signal de sortie est identique au signal d’entrée
    @Test
    public void am5Va00dbOutputTest() {
        constGen.setVoltage(5.0);
        vca.setA0(0.0);

        assertEquals(5.0, constGen.getVoltage());
        assertEquals(0.0, vca.getA0());

        for (int i = 0; i < 1000; i++) {
            constGen.generate();
            vco.generate();
            vca.generate();

            double[] vcaOut = vca.getOutput().getValues();
            double[] vcoOut = vco.getOutput().getValues();

            for (int j = 0; j < vcaOut.length; j++) {
                assertEquals(vcoOut[j], vcaOut[j]);
            }
        }
    }

    @Test
    public void am5Va00dbWrongOutputTest() {
        constGen.setVoltage(4.9);
        vca.setA0(0.0);

        assertEquals(4.9, constGen.getVoltage());
        assertEquals(0.0, vca.getA0());

        for (int i = 0; i < 1000; i++) {
            constGen.generate();
            vco.generate();
            vca.generate();

            double[] vcaOut = vca.getOutput().getValues();
            double[] vcoOut = vco.getOutput().getValues();

            for (int j = 0; j < vcaOut.length; j++) {
                assertNotSame(vcoOut[j], vcaOut[j]);
            }
        }
    }

    // lorsque la tension d’entrée sur am augmente d’1 V, le gain augmente de 12 dB
    @Test
    public void defaultAttenuationOutputTest() {
        constGen.setVoltage(5.0);
        vca.setA0(0.0);

        assertEquals(5.0, constGen.getVoltage());
        assertEquals(0.0, vca.getA0());
        assertEquals(0.0, vca.getA0());
    }

    @Test
    public void increase1Vp12dbOutputTest() {
        vca.setA0(0.0);

        double amplitude = 5.0;
        for (int i = 0; i < 5; i++) {
            constGen.setVoltage(amplitude);

            constGen.generate();
            vco.generate();
            vca.generate();

            assertEquals(vco.getOutput().getValue() * (constGen.getOutput().getValue()*12/5), vca.getOutput().getValue(), 0.0);

            amplitude++;
        }
    }

    // lorsque la tension d’entrée sur am diminue d’1 V, le gain diminue de 12 dB
    @Test
    public void decrease1Vm12dbOutputTest() {
        vca.setA0(0.0);

        double amplitude = 5.0;
        for (int i = 0; i < 5; i++) {
            constGen.setVoltage(amplitude);

            constGen.generate();
            vco.generate();
            vca.generate();

            assertEquals(vco.getOutput().getValue() * (constGen.getOutput().getValue()*12/5), vca.getOutput().getValue(), 0.0);

            amplitude--;
        }
    }


    /**
     * Update
     */
    class SubjectVCATest implements SubjectVCA {
        private double decibels;

        public SubjectVCATest(double decibels) {
            this.decibels = decibels;
        }

        @Override public Double getDecibel() { return this.decibels;  }
        @Override public void register(Obseurveur o) { }
        @Override public void remove(Obseurveur o) { }
        @Override public void notifyObseurveur() {  }
    }

    @Test
    public void updateTest() {
        SubjectVCA sub = new SubjectVCATest(-3.6);
        vca.update(sub);
        assertEquals(-3.6, vca.getA0());
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = vca.getAllPorts();
        assertTrue(list.contains(PortType.INPUTAM));
        assertTrue(list.contains(PortType.INPUT));
        assertTrue(list.contains(PortType.OUTPUT));
    }
}
