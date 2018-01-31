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

        for(int i=1; i < 3;i++){
            signal.setOctave(1);
            Assert.assertEquals(" Octave must be "+i, i, signal.getOctave());

        }
    }
    /**
     * Vérifie la diminution de l'octave
     */
    @Test
    public void changeOctaveDown(){
        for(int i=1; i < 3;i++){
            signal.setOctave(-1);
            assertEquals(" Octave must be -"+(-i), -i, signal.getOctave());

        }
    }

    /**
     *  Vérifie le réglage fin
     */

    public void reglageFinTest(){
        signal.setReglageFin(1);
        assertEquals("Reglage fin must be 1", 1, signal.getReglageFin());
        signal.setReglageFin(1);
        assertEquals("Reglage fin must be 1", 1, signal.getReglageFin());
        signal.setReglageFin(0);
        assertEquals("Reglage fin must be 0", 0, signal.getReglageFin());
        signal.setReglageFin(-1);
        assertEquals("Reglage fin must be -1", -1, signal.getReglageFin());
        signal.setReglageFin(-1);
        assertEquals("Reglage fin must be -1", -1, signal.getReglageFin());

    }

}
