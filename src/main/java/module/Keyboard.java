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
            keyPressed(event.getCode());
        }
        else if(event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            keyRelease(event.getCode());
        }
    }

    private void keyRelease(KeyCode code) {
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

    private void keyPressed(KeyCode code) {
        boolean gateUpdate = true;
        switch (code) {
            case Q:
                if(!keyPressed[0]){
                    keyPressed[0] = true;
                    note = Note.DO;
                    lastKey = code;
                }
                break;
            case S: if(!keyPressed[1]){
                keyPressed[1] = true;
                note = Note.RE;
                lastKey = code;
            }
                break;
            case D: if(!keyPressed[2]){
                keyPressed[2] = true;
                note = Note.MI;
                lastKey = code;
            }
                break;
            case F: if(!keyPressed[3]){
                keyPressed[3] = true;
                note = Note.FA;
                lastKey = code;
            }
                break;
            case G: if(!keyPressed[4]){
                keyPressed[4] = true;
                note = Note.SOL;
                lastKey = code;
            }
                break;
            case H: if(!keyPressed[5]){
                keyPressed[5] = true;
                note = Note.LA;
                lastKey = code;
            }
                break;
            case J: if(!keyPressed[6]){
                keyPressed[6] = true;
                note = Note.SI;
                lastKey = code;
            }
                break;
            case K: if(!keyPressed[7]){
                keyPressed[7] = true;
                note = Note.DOU;
                lastKey = code;
            }
                break;
            case Z: if(!keyPressed[8]){
                keyPressed[8] = true;
                note = Note.DOD;
                lastKey = code;
            }
                break;
            case E: if(!keyPressed[9]){
                keyPressed[9] = true;
                note = Note.RED;
                lastKey = code;
            }
                break;
            case T: if(!keyPressed[10]){
                keyPressed[10] = true;
                note = Note.FAD;
                lastKey = code;
            }
                break;
            case Y: if(!keyPressed[11]){
                keyPressed[11] = true;
                note = Note.SOLD;
                lastKey = code;
            }
                break;
            case U: if(!keyPressed[12]){
                keyPressed[12] = true;
                note = Note.LAD;
                lastKey = code;
            }
                break;
            case X: setOctave(octave+1);gateUpdate = false;break;
            case W: setOctave(octave-1);gateUpdate = false;break;
            default: gateUpdate = false;
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

        public double getFreq(){
            return freq;
        }

        public double freqToOctave(){
            return Math.log(freq/440.0)/Math.log(2.0);
        }
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.OUTPUT);
        return list;
    }
}
