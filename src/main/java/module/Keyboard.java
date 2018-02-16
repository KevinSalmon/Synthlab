package module;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectKeyboard;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.PortType;
import utils.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Keyboard extends Module implements EventHandler<KeyEvent>, Obseurveur<SubjectKeyboard> {

    UnitOutputPort cv;
    UnitOutputPort gate;

    private Note note;
    private int octave;

    private boolean[] keyPressed;
    private KeyCode lastKey;

    public Keyboard() {
        cv = new UnitOutputPort();
        gate = new UnitOutputPort();

        keyPressed = new boolean[Note.SIZE];
        Arrays.fill(keyPressed,false);

        note = Note.NONE;

        addPort(cv,PortType.OUTPUT.getType());
        addPort(gate,PortType.GATE.getType());
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)){
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        }
        if(PortType.GATE.getType().equals(name)){
            return new Tuple<>(getPortByName(name),PortType.GATE);
        }
        return null;
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] out = this.cv.getValues();

        for (int i = start; i < limit; i++) {
            out[i] = (note.freqToOctave()+octave)/12;
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            keyPressedHandle(event.getCode());
        }
        else if(event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            keyReleaseHandle(event.getCode());
        }
    }

    private void keyReleaseHandle(KeyCode code) {
        if(lastKey != null && lastKey.equals(code))
            gate.setValueInternal(-5.0/12.0);

        switch (code){
            case Q: keyPressed[0] = false;break;
            case S: keyPressed[1] = false;break;
            case D: keyPressed[2] = false;break;
            case F: keyPressed[3] = false;break;
            case G: keyPressed[4] = false;break;
            case H: keyPressed[5] = false;break;
            case J: keyPressed[6] = false;break;
            case K: keyPressed[7] = false;break;
            case Z: keyPressed[8] = false;break;
            case E: keyPressed[9] = false;break;
            case T: keyPressed[10] = false;break;
            case Y: keyPressed[11] = false;break;
            case U: keyPressed[12] = false;break;
            default: break;
        }
    }

    private void keyPressedHandle(KeyCode code) {
        boolean gateUpdate = true;
        switch (code) {
            case Q:
                updateNote(code, 0, Note.DO);
                break;
            case S: updateNote(code, 1, Note.RE);
                break;
            case D: updateNote(code, 2, Note.MI);
                break;
            case F: updateNote(code, 3, Note.FA);
                break;
            case G: updateNote(code, 4, Note.SOL);
                break;
            case H: updateNote(code, 5, Note.LA);
                break;
            case J: updateNote(code, 6, Note.SI);
                break;
            case K: updateNote(code, 7, Note.DOU);
                break;
            case Z: updateNote(code, 8, Note.DOD);
                break;
            case E: updateNote(code, 9, Note.RED);
                break;
            case T: updateNote(code, 10, Note.FAD);
                break;
            case Y: updateNote(code, 11, Note.SOLD);
                break;
            case U: updateNote(code, 12, Note.LAD);
                break;
            case X: octave+=1;gateUpdate = false;break;
            case W: octave-=1;gateUpdate = false;break;
            default: gateUpdate = false;
        }

        if(gateUpdate)gate.setValueInternal(5.0/12.0);
    }

    private void updateNote(KeyCode code, int nb, Note n) {
        if(!keyPressed[nb]){
            keyPressed[nb] = true;
            note = n;
            lastKey = code;
        }
    }

    public Note getNote() {
        return note;
    }

    public int getOctave() {
        return octave;
    }

    @Override
    public void update(SubjectKeyboard o) {
       o.receiveNote(getNote());
       o.receiveOctave(getOctave());
    }

    @Override
    public Module getReference() {
        return this;
    }

    public enum Note{
        DO(261.6),
        DOD(277),
        RE(293.7),
        RED(311),
        MI(329.6),
        FA(349.2),
        FAD(370),
        SOL(392.0),
        SOLD(415),
        LA(440.0),
        LAD(466),
        SI(493.9),
        DOU(523.3),
        NONE(0.0);

        private static final int SIZE = Note.values().length;

        private double freq;
        Note(double v) {
            freq = v;
        }

        public double freqToOctave(){
            return Math.log(freq/440.0)/Math.log(2.0);
        }
    }

    public UnitOutputPort getOutput() {
        return cv;
    }

    public UnitOutputPort getGate() {
        return gate;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.OUTPUT);
        list.add(PortType.GATE);
        return list;
    }
}
