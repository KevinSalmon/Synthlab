package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import controller.Obseurveur;
import controller.SubjectOutput;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;
import java.io.*;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;

public class OutputModuleTest {
    private Synthesizer synth;
    private OutputModule outputModule;
    private VCO vco;

    @Before
    public void setUp(){
        synth = JSyn.createSynthesizer();
        synth.start();

        outputModule = new OutputModule(synth);

        vco = new VCO();
        vco.getOutput().connect(outputModule.getInput());
        synth.add(vco);
    }

    @Test
    public void getReferenceTest() {
        assertEquals(outputModule, outputModule.getReference());
    }

    @Test
    public void getOnlyOneInputPortTest() {
        assertNotNull(outputModule.getPort(PortType.INPUT.getType()));
        assertNull(outputModule.getPort(PortType.OUTPUT.getType()));
        assertNull(outputModule.getPort(PortType.INPUTAM.getType()));
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

    @Test
    public void signalMuteTest() {
        outputModule.setMute(true);
        for (int i = 0; i < 1000; i++) {
            outputModule.generate();
            vco.generate();

            double[] values = outputModule.getOutputTest().getValues();
            for (int j = 0; j < values.length; j++) {
                assertEquals(values[j], 0.0);
            }
        }
    }

    @Test
    public void signalDefaultTest() {
        for (int i = 0; i < 1000; i++) {
            vco.generate();
            outputModule.generate();

            double[] outModule = outputModule.getOutputTest().getValues();
            double[] vcoModule = vco.getOutput().getValues();
            for (int j = 0; j < outModule.length; j++) {
                assertEquals(vcoModule[j], outModule[j]);
            }
        }
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
        private boolean recordEnabled;
        private String recordFilename;

        public SubjectOutputTest(boolean mute, double decibels, boolean recordEnabled, String recordFilename) {
            this.mute = mute;
            this.decibels = decibels;
            this.recordEnabled = recordEnabled;
            this.recordFilename = recordFilename;
        }

        @Override public boolean getMuteValue() { return this.mute; }

        @Override public double getDecibelValue() { return this.decibels; }

        @Override
        public boolean getRecordEnabled() {
            return this.recordEnabled;
        }

        @Override
        public String getRecordFilename() {
            return this.recordFilename;
        }

        @Override public void register(Obseurveur o) { }

        @Override public void remove(Obseurveur o) { }

        @Override public void notifyObseurveur() { }
    }

    @Test
    public void updateMuteTrueTest() {
        SubjectOutput sub = new SubjectOutputTest(true, 0.0, false, "");
        outputModule.update(sub);
        assertTrue(outputModule.getMute());
    }

    @Test
    public void updateMuteFalseTest() {
        SubjectOutput sub = new SubjectOutputTest(false, 0.0, false, "");;
        outputModule.update(sub);
        assertFalse(outputModule.getMute());
    }

    @Test
    public void updateGoodAttenuationTest() {
        SubjectOutput sub = new SubjectOutputTest(false, -2.8, false, "");
        outputModule.update(sub);
        assertEquals(-2.8, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void updateWrongAttenuationTest() {
        SubjectOutput sub = new SubjectOutputTest(false, 32.0, false, "");
        outputModule.update(sub);
        assertEquals(0.0, outputModule.getDecibelsAttenuation());
    }

    @Test
    public void updateRecordEnabledTest() {
        String filename = "OutputTest-" + System.currentTimeMillis() + ".wav";
        File wavFile = new File(filename);

        SubjectOutput sub = new SubjectOutputTest(false, 0.0, true, filename);
        outputModule.update(sub);

        assertTrue(wavFile.exists());

        sub = new SubjectOutputTest(false, 0.0, false, filename);
        outputModule.update(sub);

        wavFile.delete();
        assertFalse(wavFile.exists());
    }

    @Test
    public void updateOnlyOneRecordEnabledTest() {
        String filename1 = "OutputTest-" + System.currentTimeMillis() + ".wav";
        String filename2 = "OutputTest-" + System.currentTimeMillis() + "-2.wav";
        File wavFile1 = new File(filename1);
        File wavFile2 = new File(filename2);

        SubjectOutput sub = new SubjectOutputTest(false, 0.0, true, filename1);
        outputModule.update(sub);

        sub = new SubjectOutputTest(false, 0.0, true, filename2);
        outputModule.update(sub);

        assertTrue(wavFile1.exists());
        assertFalse(wavFile2.exists());

        sub = new SubjectOutputTest(false, 0.0, false, filename1);
        outputModule.update(sub);

        wavFile1.delete();
        assertFalse(wavFile1.exists());
        assertFalse(wavFile2.exists());
    }

    @Test
    public void updateRecordFileNotFoundExceptionTest() { // Se produit lorsque file est un dossier
        SubjectOutput sub = new SubjectOutputTest(false, 0.0, true, "OutputTest-" + System.currentTimeMillis() + "/test.wav");
        outputModule.update(sub);
        assertTrue(outputModule.getLastUpdateFail());
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = outputModule.getAllPorts();
        assertTrue(list.contains(PortType.INPUT));
    }
}