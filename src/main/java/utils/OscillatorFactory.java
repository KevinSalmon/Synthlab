package utils;

import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

/**
 * Factory qui créer un oscillateur
 */
public class OscillatorFactory {

    /**
     * Change le type de l'oscillateur
     * @param oscillatorType
     * @return le nouvel oscillateur
     */
    public static UnitOscillator changeOscillator(OscillatorType oscillatorType){

        switch (oscillatorType){
            case SQUARE: return new SquareOscillator();
            case SAWTOOTH: return new SawtoothOscillator();
            case TRIANGLE:return new TriangleOscillator();
            default: throw new IllegalArgumentException("Oscillator not found");
        }

    }
}
