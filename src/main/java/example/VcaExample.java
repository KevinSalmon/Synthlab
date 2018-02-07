package example;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import module.ConstantGenerator;
import module.OutputModule;
import module.VCA;
import module.VCO;
import utils.OscillatorType;
import java.util.Scanner;

public class VcaExample {
    public static void main(String[] args) {
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        VCO vcoIn = new VCO();
        synth.add(vcoIn);

        ConstantGenerator constAm = new ConstantGenerator();
        constAm.setVoltage(5.0);
        synth.add(constAm);

        VCA vca = new VCA();
        synth.add(vca);

        vcoIn.getOutput().connect(0, vca.getInput(), 0);
        constAm.getOutput().connect(0, vca.getAm(), 0);

        OutputModule outModule = new OutputModule(synth);
        vca.getOutput().connect(0, outModule.getInput(), 0);

        // Start at least the LineOut
        Scanner c = new Scanner(System.in);

        new Thread(()-> {
            Boolean exit = false;
            Double am = 5.0;
            Double a0 = 0.0;
            while(!exit) {
                String str = c.nextLine();
                if(str.length()>0){
                    switch (str.charAt(0)) {
                        // VCO
                        case 'd': vcoIn.increaseReglageFin(0.1); break;
                        case 'e': vcoIn.increaseOctave(1); break;
                        case 'q': vcoIn.decreaseReglageFin(0.1); break;
                        case 'a': vcoIn.decreaseOctave(1); break;
                        case 't': vcoIn.changeCurrentOsc(OscillatorType.TRIANGLE); break;
                        case 's': vcoIn.changeCurrentOsc(OscillatorType.SQUARE); break;

                        // VCA
                        case '*': am=am+0.1; constAm.setVoltage(am); break;
                        case '9': am=am-0.1; constAm.setVoltage(am); break;
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
