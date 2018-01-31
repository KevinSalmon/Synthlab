import module.VCO;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.*;

import java.util.Scanner;

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
        myVco.getOutput().connect( 0, myOut.input, 0 ); /* Left side */
        myVco.getOutput().connect( 0, myOut.input, 1 ); /* Right side */

        // Configure if necessary
        //myVco.frequency.set( 1000.0 );  // 1 kHz

        // Start at least the LineOut
        Scanner c = new Scanner(System.in);
        new Thread(()-> {
            while(true) {
                String str = c.nextLine();
                if (str.charAt(0) == 'd') {
                    myVco.IncreaseFrequency(0.1f);
                }
                if(str.charAt(0) == 'e'){
                    myVco.IncreaseOctave(1);
                }

            }
        }).start();
        myOut.start();

    }
}