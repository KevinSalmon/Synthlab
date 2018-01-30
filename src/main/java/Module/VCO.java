package Module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;

public class VCO extends Circuit implements UnitSource{

    private final SquareOscillator sqrOsc;
    private final UnitOscillator currentOsc;
    public final UnitOutputPort output;


    public VCO() {
        add(sqrOsc = new SquareOscillator());
        currentOsc = sqrOsc;

        addPort(output = sqrOsc.output, "output");

        sqrOsc.frequency.set(1000.0); //1 kHz
    }


    public UnitOutputPort getOutput() {
        return output;
    }
}
