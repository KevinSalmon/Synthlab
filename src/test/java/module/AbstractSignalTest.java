package module;

import Signal.Signal;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public abstract class AbstractSignalTest {
    protected static Signal signal;

    /**
     * Initialise le signal
     */
    public abstract void init();

//    /**
//     * Vérifie l'intervalle de voltage
//     */
//    @Test
//    abstract void increaseUntilLimit();
//
//    /**
//     * Vérifie que le voltage ne sort pas de la borne supérieure
//     */
//    @Test
//    abstract void moreThanLimit();
//
//    /**
//     * Vérifie que le voltage ne sort pas de la borne inférieure
//     */
//    @Test
//    abstract void lessThanLimit();

    /**
     * Vérifie que la bande passante est de 22KHz
     */
    @Test
    public void frequencyInitializeTest() {

        Assert.assertTrue("The bandwith must be strictly positive", signal.getBandWidth() > 0);

        Assert.assertTrue("The frequency must be initialize over 0", signal.getFrequency() >= 0);
        Assert.assertTrue("The frequency must be initialize under the bandwidth", signal.getFrequency() <= signal.getBandWidth());
    }

}
