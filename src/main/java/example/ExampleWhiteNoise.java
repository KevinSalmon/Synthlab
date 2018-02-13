package example;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import module.BruitBlancModule;
import module.OutputModule;

public class ExampleWhiteNoise {

    public static void main(String[] args) {
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        BruitBlancModule bruitBlancModule = new BruitBlancModule();
        synth.add(bruitBlancModule);

        OutputModule outModule = new OutputModule(synth);
        bruitBlancModule.getOut().connect(0, outModule.getInput(), 0);
    }
}
