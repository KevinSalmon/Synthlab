import module.OutputModule;
import module.VCO;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import java.util.Scanner;

public class Example {


    public static void main(String[] args){

        // Create and start Synthesizer
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        // Create at least a LineOut to get sound to speakers
        //LineOut myOut = new LineOut();
        VCO myVco = new VCO();

        // Add everything to the synthesizer
        //synth.add(myOut);
        synth.add(myVco);

        // Connect all module together
        //myVco.getOutput().connect( 0, myOut.input, 0 ); /* Left side */
        //myVco.getOutput().connect( 0, myOut.input, 1 ); /* Right side */

        OutputModule outModule = new OutputModule(synth);
        outModule.setInput(myVco.getOutput());

        // Start at least the LineOut
        Scanner c = new Scanner(System.in);
        new Thread(()-> {
            while(true) {
                String str = c.nextLine();
                if(str.length()>0){
                    if (str.charAt(0) == 'd') {
                        myVco.increaseReglageFin(0.1);
                    }
                    else if(str.charAt(0) == 'e'){
                        myVco.increaseOctave(1);
                    }
                    else if(str.charAt(0) == 'q'){
                        myVco.decreaseReglageFin(0.1);
                    }
                    else if(str.charAt(0) == 'a'){
                        myVco.decreaseOctave(1);
                    }
                    else if (str.charAt(0) == 'm') { // Mute
                        outModule.switchMute();
                    }
                    else if (str.charAt(0) == '+') { // dB +
                        outModule.changeDecibelsAttenuation(1.0);
                    }
                    else if (str.charAt(0) == '-') { // dB -
                        outModule.changeDecibelsAttenuation(-1.0);
                    }
                }
            }
        }).start();
        //myOut.start();
    }
}
