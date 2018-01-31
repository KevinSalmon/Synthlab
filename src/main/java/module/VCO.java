package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;

public class VCO extends Circuit implements UnitSource{

    private final SquareOscillator sqrOsc;
    private final UnitOscillator currentOsc;
    public final UnitOutputPort output;
    private UnitInputPort frequency;
    private Signal audioSignal;


    public VCO() {
        add(sqrOsc = new SquareOscillator());
        currentOsc = sqrOsc;

        addPort(output = sqrOsc.output, "output");
        addPort(frequency = sqrOsc.frequency);
        audioSignal = new AudioSignal();
        sqrOsc.frequency.set((440) * Math.pow(2, (audioSignal.getFrequencyMin()))); //1 kHz
    }


    public UnitOutputPort getOutput() {
        return output;
    }

   public void IncreaseFrequency(double v){
        audioSignal.addFrequency(v);
        sqrOsc.frequency.set((440) * Math.pow(2, (audioSignal.getFrequencyMin())));

   }

    public void IncreaseOctave(int amp) {
       audioSignal.changeOctave(amp);
        sqrOsc.frequency.set(440 * Math.pow(2, (audioSignal.getFrequencyMin())));
    }
}
