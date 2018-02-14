package sauvegarde;

import module.Keyboard;

public class SavedKeyboard extends SavedModule {

    private Keyboard.Note note;
    private int octave;

    public SavedKeyboard() {

    }

    public SavedKeyboard(Keyboard.Note note, int octave) {
        this.note = note;
        this.octave = octave;
    }

    public SavedKeyboard(double xPos, double yPos, Keyboard.Note note, int octave) {
        super(xPos, yPos);
        this.note = note;
        this.octave = octave;
    }

    public Keyboard.Note getNote() {

        return note;
    }

    public void setNote(Keyboard.Note note) {
        this.note = note;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }
}
