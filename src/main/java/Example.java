import com.jsyn.unitgen.LineOut;
import module.OutputModule;
import module.VCA;
import module.VCO;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import utils.OscillatorType;

import java.util.Scanner;

public class Example {


    public static void main(String[] args) {
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        VCO myVco = new VCO();
        synth.add(myVco);

        VCA vca = new VCA();
        synth.add(vca);
        myVco.getOutput().connect(0, vca.getInput(), 0);

        OutputModule outModule = new OutputModule(synth);
        vca.getOutput().connect( 0, outModule.getInput(), 0 );

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
                    else if(str.charAt(0) == 't'){
                        myVco.changeCurrentOsc(OscillatorType.TRIANGLE);
                    }
                    else if(str.charAt(0) == 's'){
                        myVco.changeCurrentOsc(OscillatorType.SQUARE);
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
    }
}

