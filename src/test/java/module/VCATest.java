package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import controller.Obseurveur;
import controller.SubjectOutput;
import controller.SubjectVCA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import signal.ModulationSignal;
import signal.Signal;
import utils.PortType;

import static junit.framework.TestCase.*;

public class VCATest {
    public static VCA vca;
    public static VCO vco;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        vca = new VCA();
        synth.add(vca);

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
        assertNotNull(vca.getPort(PortType.AM.getType()));
        assertNull(vca.getPort("FAKE"));
    }

    @Test
    public void getAmTest() {
        Signal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);
        assertEquals(am, vca.getAm());
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

    @Test
    public void a05VOutputTest() {
        Signal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);
        vca.setA0(6.0);

        assertEquals(5.0, vca.getAm().getVolt());
        assertEquals(6.0, vca.getA0());

        vco.generate();
        vca.generate();

        assertEquals(6.0, vca.getDecibelsAttenuation());
    }

    @Test
    public void a04VOutputTest() {
        Signal am = new ModulationSignal(0.4, 1);
        vca.setAm(am);
        vca.setA0(6.0);

        assertEquals(4.0, vca.getAm().getVolt());
        assertEquals(6.0, vca.getA0());

        vco.generate();
        vca.generate();

        assertEquals(0.0, vca.getDecibelsAttenuation());
    }


    /****************
     * US
     ***************/

    // lorsque que l’entrée am est déconnectée ou nulle, le gain du VCA est nul (pas de signal en sortie)
    @Test
    public void amNullOutputTest() {
        assertEquals(null, vca.getAm());

        for (int i = 0; i < 1000; i++) {
            vca.generate();
            vco.generate();

            double[] values = vca.getOutput().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(values[j], 0.0);
            }
        }
    }

    @Test
    public void am0OutputTest() {
        vca.setAm(new ModulationSignal(0.0, 1));
        assertEquals(0.0, vca.getAm().getAmplitude());

        for (int i = 0; i < 1000; i++) {
            vca.generate();
            vco.generate();

            double[] values = vca.getOutput().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(values[j], 0.0);
            }
        }
    }

    // lorsque am vaut 5 V et a0 vaut 0 dB le signal de sortie est identique au signal d’entrée
    @Test
    public void am5Va00dbOutputTest() {
        Signal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);
        vca.setA0(0.0);
        assertEquals(5.0, vca.getAm().getVolt());
        assertEquals(0.0, vca.getA0());

        for (int i = 0; i < 1000; i++) {
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
        Signal am = new ModulationSignal(0.9, 1);
        vca.setAm(am);
        vca.setA0(0.0);
        assertEquals(9.0, vca.getAm().getVolt());
        assertEquals(0.0, vca.getA0());

        for (int i = 0; i < 1000; i++) {
            vco.generate();
            vca.generate();

            double[] vcoOut = vco.getOutput().getValues();
            double[] vcaOut = vca.getOutput().getValues();
            for (int j = 0; j < vcaOut.length; j++) {
                assertNotSame(vcoOut[j], vcaOut[j]);
            }
        }
    }

    // lorsque la tension d’entrée sur am augmente d’1 V, le gain augmente de 12 dB
    @Test
    public void defaultAttenuationOutputTest() {
        Signal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);
        vca.setA0(0.0);

        assertEquals(0.0, vca.getDecibelsAttenuation());
    }

    @Test
    public void increase1Vp12dbOutputTest() {
        Signal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);
        vca.setA0(0.0);

        double amplitude = 0.5;

        for (int i = 0; i < 5; i++) {
            am.setAmplitude(amplitude);

            vco.generate();
            vca.generate();

            assertEquals((12.0 * i), vca.getDecibelsAttenuation(), 0.001);

            amplitude = amplitude + 0.1;
        }
    }

    // lorsque la tension d’entrée sur am diminue d’1 V, le gain diminue de 12 dB
    @Test
    public void decrease1Vm12dbOutputTest() {
        Signal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);
        vca.setA0(0.0);

        double amplitude = 0.5;

        for (int i = 0; i < 5; i++) {
            am.setAmplitude(amplitude);

            vco.generate();
            vca.generate();

            assertEquals((-12.0 * i), vca.getDecibelsAttenuation(), 0.001);

            amplitude = amplitude - 0.1;
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
        assertEquals(-3.6, vca.getDecibelsAttenuation());
    }
}
