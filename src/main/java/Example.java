import signal.ModulationSignal;
import com.jsyn.unitgen.LineOut;
import module.OutputModule;
import module.Replicateur;
import module.VCA;
import module.VCO;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import utils.OscillatorType;

import java.util.Scanner;
import java.util.logging.Logger;

public class Example {

    public static void main(String[] args) {
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        VCO myVco = new VCO();
        synth.add(myVco);

        VCO vco2 = new VCO();
        synth.add(vco2);

        vco2.setOctave(-100);
        Logger.getGlobal().info("vco2.frequency : " +vco2.getFrequency());
        vco2.getOutput().connect(myVco.getInput());

        VCA vca = new VCA();
        synth.add(vca);

        myVco.getOutput().connect(0, vca.getInput(), 0);

        ModulationSignal am = new ModulationSignal(0.5, 1);
        vca.setAm(am);

        Replicateur rep = new Replicateur();
        synth.add(rep);
        vca.getOutput().connect(0, rep.getIn(), 0);

        OutputModule outModule = new OutputModule(synth);
        vca.getOutput().connect(0, outModule.getInput() , 0);
        //rep.getOut1().connect( 0, outModule.getInput(), 0 );
        //rep.output2.connect( 0, outModule.getInput(), 0 );
        //rep.output3.connect( 0, outModule.getInput(), 0 );

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
                        case '*': ampl=ampl+0.01; am.setAmplitude(ampl); break;
                        case '9': ampl=ampl-0.01; am.setAmplitude(ampl); break;
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

