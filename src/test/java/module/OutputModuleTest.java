package module;

import com.jsyn.JSyn;
import com.jsyn.ports.UnitOutputPort;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.*;

public class OutputModuleTest {
    private OutputModule outputModule;

    @Before
    public void setUp(){
        outputModule = new OutputModule(JSyn.createSynthesizer());
    }

    // Input
    @Test
    public void defaultInputConnectionTest() {
        assertNull(outputModule.getInput());
    }

    @Test
    public void setInputTest() {
        UnitOutputPort myPort = new UnitOutputPort();
        outputModule.setInput(myPort);
        assertEquals(myPort, outputModule.getInput());
    }

    @Test
    public void setNullInputTest() {
        outputModule.setInput(null);
        assertNull(outputModule.getInput());
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
}
