package utils;

import com.jsyn.unitgen.*;

/**
 * Factory qui créer un oscillateur
 */
public class OscillatorFactory {

    /**
     * Change le type de l'oscillateur
     * @param oscillatorType
     * @return le nouvel oscillateur
     */
    public static UnitOscillator createOscillator(OscillatorType oscillatorType){

        switch (oscillatorType){
            case SQUARE: return new SquareOscillator();
            case SAWTOOTH: return new SawtoothOscillator();
            case SINE: return new SineOscillator();
            case TRIANGLE:return new TriangleOscillator();
            default: throw new IllegalArgumentException("Oscillator not found");
        }

    }

    private OscillatorFactory(){}
}
