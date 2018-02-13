package module;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import controller.Obseurveur;
import controller.SubjectKeyboard;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.PortType;
import utils.Tuple;

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

        keyPressed = new boolean[Note.size];
        Arrays.fill(keyPressed,false);

        note = Note.None;

        addPort(cv,PortType.OUTPUT.getType());
        addPort(gate,PortType.GATE.getType());
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)){
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        }
        if(PortType.GATE.getType().equals(name)){
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);
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
        if(lastKey.equals(code))
            gate.setValueInternal(-5.0/12.0);

        switch (code){
            case Q: keyPressed[0] = false;
            case S: keyPressed[1] = false;
            case D: keyPressed[2] = false;
            case F: keyPressed[3] = false;
            case G: keyPressed[4] = false;
            case H: keyPressed[5] = false;
            case J: keyPressed[6] = false;
            case K: keyPressed[7] = false;
            case Z: keyPressed[8] = false;
            case E: keyPressed[9] = false;
            case T: keyPressed[10] = false;
            case Y: keyPressed[11] = false;
            case U: keyPressed[12] = false;
            default:
        }
    }

    private void keyPressedHandle(KeyCode code) {
        boolean gateUpdate = true;
        switch (code) {
            case Q:
                if(!keyPressed[0]){
                    keyPressed[0] = true;
                    note = Note.Do;
                    lastKey = code;
                }
                break;
            case S: if(!keyPressed[1]){
                keyPressed[1] = true;
                note = Note.Re;
                lastKey = code;
            }
                break;
            case D: if(!keyPressed[2]){
                keyPressed[2] = true;
                note = Note.Mi;
                lastKey = code;
            }
                break;
            case F: if(!keyPressed[3]){
                keyPressed[3] = true;
                note = Note.Fa;
                lastKey = code;
            }
                break;
            case G: if(!keyPressed[4]){
                keyPressed[4] = true;
                note = Note.Sol;
                lastKey = code;
            }
                break;
            case H: if(!keyPressed[5]){
                keyPressed[5] = true;
                note = Note.La;
                lastKey = code;
            }
                break;
            case J: if(!keyPressed[6]){
                keyPressed[6] = true;
                note = Note.Si;
                lastKey = code;
            }
                break;
            case K: if(!keyPressed[7]){
                keyPressed[7] = true;
                note = Note.Dou;
                lastKey = code;
            }
                break;
            case Z: if(!keyPressed[8]){
                keyPressed[8] = true;
                note = Note.Dod;
                lastKey = code;
            }
                break;
            case E: if(!keyPressed[9]){
                keyPressed[9] = true;
                note = Note.Red;
                lastKey = code;
            }
                break;
            case T: if(!keyPressed[10]){
                keyPressed[10] = true;
                note = Note.Fad;
                lastKey = code;
            }
                break;
            case Y: if(!keyPressed[11]){
                keyPressed[11] = true;
                note = Note.Sold;
                lastKey = code;
            }
                break;
            case U: if(!keyPressed[12]){
                keyPressed[12] = true;
                note = Note.Lad;
                lastKey = code;
            }
                break;
            case X: setOctave(octave+1);gateUpdate = false;break;
            case W: setOctave(octave-1);gateUpdate = false;break;
            default:
        }

        if(gateUpdate)gate.setValueInternal(5.0/12.0);
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
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
        Dod(277),
        Re(293.7),
        Red(311),
        Mi(329.6),
        Fa(349.2),
        Fad(370),
        Sol(392.0),
        Sold(415),
        La(440.0),
        Lad(466),
        Si(493.9),
        Dou(523.3),
        None(0.0);

        private static final int size = Note.values().length;

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
