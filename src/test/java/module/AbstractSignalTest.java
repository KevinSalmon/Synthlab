package module;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public abstract class AbstractSignalTest {
    protected static Signal signal;

    /**
     * Initialise le signal
     */
    public abstract void init();

    /**
     * Vérifie l'intervalle de voltage
     */
    @Test
    abstract void increaseUntilLimit();

    /**
     * Vérifie que le voltage ne sort pas de la borne supérieure
     */
    @Test
    abstract void moreThanLimit();

    /**
     * Vérifie que le voltage ne sort pas de la borne inférieure
     */
    @Test
    abstract void lessThanLimit();

    /**
     * Vérifie que la bande passante est de 22KHz
     */
    @Test
    public void frequencyTest() {

        assertEquals("BandWith must be 22KHz", signal.getBandWidth(), (signal.getFrequencyMax() - signal.getFrequencyMin()));

    }

    /**
     * Vérifie l'augmentation de l'octave
     */
    @Test
     public void changeOctaveUp(){
        signal.changeOctave(1);
        Assert.assertEquals(" Frequency must double", 440*2, signal.getFrequencyMin());
    }
    /**
     * Vérifie la diminution de l'octave
     */
    @Test
    public void changeOctaveDown(){
        signal.changeOctave(-1);
        Assert.assertEquals(" Frequency must divided by 2", 440/2, signal.getFrequencyMin());
    }

}
