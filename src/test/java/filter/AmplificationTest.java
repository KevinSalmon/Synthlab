package filter;

import org.junit.Before;
import org.junit.Test;
import utils.Amplification;

import static org.junit.jupiter.api.Assertions.*;

public class AmplificationTest {
    private Amplification filter;

    @Before
    public void setUp(){
        filter = new Amplification();
    }

    // Attenuation
    @Test
    public void defaultAttenuationTest() {
        assertEquals(0.0, filter.getDecibelsAmplification());
    }

    @Test
    public void setAttenuationTest() {
        filter.setDecibelsAmplification(-27.0);
        assertEquals(-27.0, filter.getDecibelsAmplification());
    }
}
