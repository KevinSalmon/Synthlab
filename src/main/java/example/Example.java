package example;

import com.jsyn.unitgen.EnvelopeDAHDSR;
import com.softsynth.math.AudioMath;
import utils.Amplification;
import module.*;
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

        VCO vco2 = new VCO();
        synth.add(vco2);

        VCA vca = new VCA();
        synth.add(vca);

        Replicateur rep = new Replicateur();
        synth.add(rep);

        EG eg = new EG();
        synth.add(eg);

        OutputModule outModule = new OutputModule(synth);


        VCFLP vcflp = new VCFLP();
        synth.add(vcflp);
        EnvelopeDAHDSR env = new EnvelopeDAHDSR();
        synth.add(env);

        myVco.getOutput().connect(outModule.getInput());

        // Start at least the LineOut
        Scanner c = new Scanner(System.in);

        new Thread(()-> {
            Boolean exit = false;
            Double ampl = 0.5;
            Double a0 = 0.0;
            while(!exit) {
                String str = c.nextLine();
                if(str.length()>0){
                    switch (str.charAt(0)) {
                        // VCO
                        case 'd': myVco.increaseReglageFin(0.1); break;
                        case 'e': myVco.increaseOctave(1); break;
                        case 'q': myVco.decreaseReglageFin(0.1); break;
                        case 'a': myVco.decreaseOctave(1); break;
                        case 't': myVco.changeCurrentOsc(OscillatorType.TRIANGLE); break;
                        case 's': myVco.changeCurrentOsc(OscillatorType.SQUARE); break;

                        // VCA
//                        case '*': ampl=ampl+0.01; am.setAmplitude(ampl); break;
//                        case '9': ampl=ampl-0.01; am.setAmplitude(ampl); break;
                        case '/': a0++; vca.setA0(a0); break;
                        case '8': a0--; vca.setA0(a0); break;

                        // Output module
                        case 'm': outModule.switchMute(); break;
                        case '+': outModule.changeDecibelsAttenuation(1.0); break;
                        case '-': outModule.changeDecibelsAttenuation(-1.0); break;
                        case '0':
                            exit = true;
                            System.exit(0);
                            break;
                        default: break;
                    }
                }
            }
        }).start();
    }
}

