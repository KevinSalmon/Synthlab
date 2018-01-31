package module;

import org.junit.Before;
import org.junit.Test;

public interface AbstractSignalTest {
    /**
     * Vérifie l'intervalle de voltage
     */
    @Test
    void increaseUntilLimit();

    /**
     * Vérifie que le voltage ne sort pas de la borne supérieure
     */
    @Test
    void moreThanLimit();

    /**
     * Vérifie que le voltage ne sort pas de la borne inférieure
     */
    @Test
    void lessThanLimit();

    /**
     * Vérifie que la bande passante est de 22KHz
     */
    @Test
    void frequencyTest();
}
