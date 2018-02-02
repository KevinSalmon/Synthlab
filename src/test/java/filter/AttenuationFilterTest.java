package filter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttenuationFilterTest {
    private AttenuationFilter filter;

    @Before
    public void setUp(){
        filter = new AttenuationFilter();
    }

    // Attenuation
    @Test
    public void defaultAttenuationTest() {
        assertEquals(0.0, filter.getDecibelsAttenuation());
    }

    @Test
    public void setAttenuationTest() {
        filter.setDecibelsAttenuation(-27.0);
        assertEquals(-27.0, filter.getDecibelsAttenuation());
    }
}
