package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import controller.Obseurveur;
import controller.SubjectOutput;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.*;

public class OutputModuleTest {
    private Synthesizer synth;
    private OutputModule outputModule;
    private VCO vco;

    @Before
    public void setUp(){
        vco = new VCO();
        synth = JSyn.createSynthesizer();
        synth.start();
        synth.add(vco);
        outputModule = new OutputModule(synth);
    }

    @Test
    public void getReferenceTest() {
        assertEquals(outputModule, outputModule.getReference());
    }

    @Test
    public void getOnlyOneInputPortTest() {
        assertNotNull(outputModule.getPort(PortType.INPUT.getType()));
        assertNull(outputModule.getPort(PortType.OUTPUT.getType()));
        assertNull(outputModule.getPort(PortType.AM.getType()));
    }

    // Input // Output
    @Test
    public void defaultInputTest() {
        assertNotNull(outputModule.getInput());
    }

    @Test
    public void defaultOutputTest() {
        assertNull(outputModule.getOutput());
    }

    // Mute
    @Test
    public void defaultMuteTest() {
        assertFalse(outputModule.getMute());
    }

    @Test
    public void switchMuteTest() {
        outputModule.switchMute();
        assertTrue(outputModule.getMute());
    }

    @Test
    public void resetSwitchMuteTest() {
        outputModule.switchMute();
        outputModule.switchMute();
        assertFalse(outputModule.getMute());
    }

    // Attenuation
    @Test
    public void defaultAttenuationTest() {
        assertEquals(0.0, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void setAttenuationTest() {
        outputModule.setDecibelsAttenuation(-41.0);
        assertEquals(-41.0, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void setWrongAttenuationTest() {
        outputModule.setDecibelsAttenuation(12.1);
        assertEquals(0.0, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void changeAttenuationTest() {
        outputModule.changeDecibelsAttenuation(-10);
        assertEquals(-10.0, outputModule.getDecibelsAttenuation());
        outputModule.changeDecibelsAttenuation(-4);
        assertEquals(-14.0, outputModule.getDecibelsAttenuation());
        outputModule.changeDecibelsAttenuation(8);
        assertEquals(-6.0, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void changeWrongAttenuationTest() {
        outputModule.changeDecibelsAttenuation(12.1);
        assertEquals(0.0, outputModule.getDecibelsAttenuation());
    }


    /**
     * Update
     */
    class SubjectOutputTest implements SubjectOutput {
        private boolean mute;
        private double decibels;

        public SubjectOutputTest(boolean mute, double decibels) {
            this.mute = mute;
            this.decibels = decibels;
        }

        @Override public boolean getMuteValue() { return this.mute; }

        @Override public double getDecibelValue() { return this.decibels; }

        @Override public void register(Obseurveur o) { }

        @Override public void remove(Obseurveur o) { }

        @Override public void notifyObseurveur() { }
    }

    @Test
    public void updateMuteTrueTest() {
        SubjectOutput sub = new SubjectOutputTest(true, 0.0);
        outputModule.update(sub);
        assertTrue(outputModule.getMute());
    }

    @Test
    public void updateMuteFalseTest() {
        SubjectOutput sub = new SubjectOutputTest(false, 0.0);
        outputModule.update(sub);
        assertFalse(outputModule.getMute());
    }

    @Test
    public void updateGoodAttenuationTest() {
        SubjectOutput sub = new SubjectOutputTest(false, -2.8);
        outputModule.update(sub);
        assertEquals(-2.8, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void updateWrongAttenuationTest() {
        SubjectOutput sub = new SubjectOutputTest(false, 32.0);
        outputModule.update(sub);
        assertEquals(0.0, outputModule.getDecibelsAttenuation());
    }
}
