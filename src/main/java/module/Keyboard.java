package module;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import controller.Obseurveur;
import controller.SubjectKeyboard;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import utils.PortType;
import utils.Tuple;

public class Keyboard extends Module implements EventHandler<KeyEvent>, Obseurveur<SubjectKeyboard> {

    UnitOutputPort cv;
    private Note note;
    private int octave;

    UnitOutputPort gate;

    public Keyboard() {
        cv = new UnitOutputPort();
        gate = new UnitOutputPort();

        note = Note.Fa;

        addPort(cv,PortType.OUTPUT.getType());
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)){
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        }
        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] out = this.cv.getValues();

        for (int i = start; i < limit; i++) {
            out[i] = 0;
        }

    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            System.out.println("YEEEEESSSSS");
        }

    }


    @Override
    public void update(SubjectKeyboard o) {
    }

    @Override
    public Module getReference() {
        return this;
    }

    private enum Note{
        Do(261.6),
        Dod(261.6),
        Re(293.7),
        Red(261.6),
        Mi(329.6),
        Fa(349.2),
        Fad(440.0),
        Sol(392.0),
        Sold(440.0),
        La(440.0),
        Lad(440.0),
        Si(493.9),
        Dou(523.3);

        private double freq;
        Note(double v) {
            freq = v;
        }

        public double getFreq(){
            return freq;
        }

        public double freqToOctave(){
            return Math.log(freq/440.0)/Math.log(2.0);
        }
    }
}
