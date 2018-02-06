import signal.ModulationSignal;
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


        ModulationSignal am = new ModulationSignal(1.0, 1);
        am.setVoltRange(-5.0, 5.0);
        vca.setAm(am);


        OutputModule outModule = new OutputModule(synth);
        vca.getOutput().connect( 0, outModule.getInput(), 0 );


        // Start at least the LineOut
        Scanner c = new Scanner(System.in);

        new Thread(()-> {
            Boolean exit = false;
            Double ampl = 1.0;
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
                        case '*': ampl=ampl+0.1; am.setAmplitude(ampl); break;
                        case '9': ampl=ampl-0.1; am.setAmplitude(ampl); break;
                        case '/': vca.setA0(5.0); break;
                        case '8': vca.setA0(0.0); break;
                        case '5': vca.setA0(-5.0); break;
                        case '2': vca.setA0(-10.0); break;

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

