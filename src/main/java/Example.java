import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.WhiteNoise;

public class Example {


    public static void main(String[] args){

        // Create and start Synthesizer
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        // Create at least a LineOut to get sound to speakers
        LineOut myOut = new LineOut();
        SineOscillator sineOsc = new SineOscillator();

        // Add everything to the synthesizer
        synth.add(myOut);
        synth.add(sineOsc);

        // Connect all module together
        sineOsc.output.connect( 0, myOut.input, 0 ); /* Left side */
        sineOsc.output.connect( 0, myOut.input, 1 ); /* Right side */

        // Configure if necessary
        sineOsc.frequency.set( 440.0 );  // 440 Hz
        sineOsc.amplitude.set( 0.5 );  // Half amplitude.

        // Start at least the LineOut
        myOut.start();
    }
}
