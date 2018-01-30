import Module.VCO;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.*;

public class Example {


    public static void main(String[] args){

        // Create and start Synthesizer
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        // Create at least a LineOut to get sound to speakers
        LineOut myOut = new LineOut();
        VCO myVco = new VCO();

        // Add everything to the synthesizer
        synth.add(myOut);
        synth.add(myVco);

        // Connect all module together
        myVco.output.connect( 0, myOut.input, 0 ); /* Left side */
        myVco.output.connect( 0, myOut.input, 1 ); /* Right side */

        // Configure if necessary
        //myVco.frequency.set( 1000.0 );  // 1 kHz

        // Start at least the LineOut
        myOut.start();
    }
}
