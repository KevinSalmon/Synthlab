package module;

import ihm.observer.Obseurveur;
import ihm.observer.SubjectKeyboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class KeyboardTest {
    private Keyboard keyboard;

    //Key Events
    //Key Pressed
    private KeyEvent APressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.A.toString(), KeyCode.A.toString(), KeyCode.A, false, false, false, false);
    private KeyEvent QPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Q.toString(), KeyCode.Q.toString(), KeyCode.Q, false, false, false, false);
    private KeyEvent SPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.S.toString(), KeyCode.S.toString(), KeyCode.S, false, false, false, false);
    private KeyEvent DPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.D.toString(), KeyCode.D.toString(), KeyCode.D, false, false, false, false);
    private KeyEvent FPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.F.toString(), KeyCode.F.toString(), KeyCode.F, false, false, false, false);
    private KeyEvent GPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.G.toString(), KeyCode.G.toString(), KeyCode.G, false, false, false, false);
    private KeyEvent HPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.H.toString(), KeyCode.H.toString(), KeyCode.H, false, false, false, false);
    private KeyEvent JPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.J.toString(), KeyCode.J.toString(), KeyCode.J, false, false, false, false);
    private KeyEvent KPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.K.toString(), KeyCode.K.toString(), KeyCode.K, false, false, false, false);
    private KeyEvent ZPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Z.toString(), KeyCode.Z.toString(), KeyCode.Z, false, false, false, false);
    private KeyEvent EPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.E.toString(), KeyCode.E.toString(), KeyCode.E, false, false, false, false);
    private KeyEvent TPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.T.toString(), KeyCode.T.toString(), KeyCode.T, false, false, false, false);
    private KeyEvent YPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Y.toString(), KeyCode.Y.toString(), KeyCode.Y, false, false, false, false);
    private KeyEvent UPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.U.toString(), KeyCode.U.toString(), KeyCode.U, false, false, false, false);
    private KeyEvent WPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.W.toString(), KeyCode.W.toString(), KeyCode.W, false, false, false, false);
    private KeyEvent XPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.X.toString(), KeyCode.X.toString(), KeyCode.X, false, false, false, false);

    //Key Released
    private KeyEvent QReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.Q.toString(), KeyCode.Q.toString(), KeyCode.Q, false, false, false, false);
    private KeyEvent SReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.S.toString(), KeyCode.S.toString(), KeyCode.S, false, false, false, false);
    private KeyEvent DReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.D.toString(), KeyCode.D.toString(), KeyCode.D, false, false, false, false);
    private KeyEvent FReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.F.toString(), KeyCode.F.toString(), KeyCode.F, false, false, false, false);
    private KeyEvent GReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.G.toString(), KeyCode.G.toString(), KeyCode.G, false, false, false, false);
    private KeyEvent HReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.H.toString(), KeyCode.H.toString(), KeyCode.H, false, false, false, false);
    private KeyEvent JReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.J.toString(), KeyCode.J.toString(), KeyCode.J, false, false, false, false);
    private KeyEvent KReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.K.toString(), KeyCode.K.toString(), KeyCode.K, false, false, false, false);
    private KeyEvent ZReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.Z.toString(), KeyCode.Z.toString(), KeyCode.Z, false, false, false, false);
    private KeyEvent EReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.E.toString(), KeyCode.E.toString(), KeyCode.E, false, false, false, false);
    private KeyEvent TReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.T.toString(), KeyCode.T.toString(), KeyCode.T, false, false, false, false);
    private KeyEvent YReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.Y.toString(), KeyCode.Y.toString(), KeyCode.Y, false, false, false, false);
    private KeyEvent UReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.U.toString(), KeyCode.U.toString(), KeyCode.U, false, false, false, false);

    @Before
    public void init(){
        keyboard = new Keyboard();
    }

    @Test
    public void initTest(){
        Assert.assertEquals("There should not have note", Keyboard.Note.NONE, keyboard.getNote());
    }

    @Test
    public void pressedTest(){
        keyboard.handle(QPressed);
        Assert.assertEquals("The note should be Do", Keyboard.Note.DO, keyboard.getNote());
        keyboard.handle(SPressed);
        Assert.assertEquals("The note should be Re", Keyboard.Note.RE, keyboard.getNote());
        keyboard.handle(QPressed);
        Assert.assertEquals("The note should still be Re", Keyboard.Note.RE, keyboard.getNote());
        keyboard.handle(DPressed);
        Assert.assertEquals("The note should be Mi", Keyboard.Note.MI, keyboard.getNote());
        keyboard.handle(FPressed);
        Assert.assertEquals("The note should be Fa", Keyboard.Note.FA, keyboard.getNote());
        keyboard.handle(GPressed);
        Assert.assertEquals("The note should be Sol", Keyboard.Note.SOL, keyboard.getNote());
        keyboard.handle(HPressed);
        Assert.assertEquals("The note should be La", Keyboard.Note.LA, keyboard.getNote());
        keyboard.handle(JPressed);
        Assert.assertEquals("The note should be Si", Keyboard.Note.SI, keyboard.getNote());
        keyboard.handle(KPressed);
        Assert.assertEquals("The note should be Dou", Keyboard.Note.DOU, keyboard.getNote());
        keyboard.handle(ZPressed);
        Assert.assertEquals("The note should be Dod", Keyboard.Note.DOD, keyboard.getNote());
        keyboard.handle(EPressed);
        Assert.assertEquals("The note should be Red", Keyboard.Note.RED, keyboard.getNote());
        keyboard.handle(TPressed);
        Assert.assertEquals("The note should be Fad", Keyboard.Note.FAD, keyboard.getNote());
        keyboard.handle(YPressed);
        Assert.assertEquals("The note should be Sold", Keyboard.Note.SOLD, keyboard.getNote());
        keyboard.handle(UPressed);
        Assert.assertEquals("The note should be Lad", Keyboard.Note.LAD, keyboard.getNote());
        Assert.assertEquals("The octave should be 0", 0, keyboard.getOctave(),0);
        keyboard.handle(XPressed);
        Assert.assertEquals("The note should be Lad", Keyboard.Note.LAD, keyboard.getNote());
        Assert.assertEquals("The octave should be 1", 1, keyboard.getOctave(),0);
        keyboard.handle(WPressed);
        Assert.assertEquals("The note should be Lad", Keyboard.Note.LAD, keyboard.getNote());
        Assert.assertEquals("The octave should be 0", 0, keyboard.getOctave(),0);
    }

    @Test
    public void releaseTest(){
        keyboard.handle(QPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(QReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(SPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(SReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(DPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(DReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(FPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(FReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(GPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(GReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(HPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(HReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(JPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(JReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(KPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(KReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(ZPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(ZReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(EPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(EReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(TPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(TReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(YPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(YReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(UPressed);
        Assert.assertEquals("The gate should be at 5V",5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(UReleased);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(XPressed);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(WPressed);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
        keyboard.handle(APressed);
        Assert.assertEquals("The gate should be at -5V",-5/12.0,keyboard.gate.get(),0.0);
    }

    @Test
    public void referenceTest(){
        Assert.assertEquals("The reference should be the keyboard itself", keyboard, keyboard.getReference());
    }

    @Test
    public void generateTest(){
        keyboard.handle(QPressed);
        keyboard.generate();
        double[] values = keyboard.getOutput().getValues();

        for (double value : values) {
            Assert.assertEquals("Value should correspond to DO", Keyboard.Note.DO.freqToOctave() / 12.0, value, 0);
        }
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = keyboard.getAllPorts();
        assertTrue(list.contains(PortType.OUTPUT));
    }

    @Test
    public void GetPortTest(){
        Assert.assertEquals("Should get the output port", keyboard.getOutput(), keyboard.getPort(PortType.OUTPUT.getType()).getLeft());
        Assert.assertEquals("Should be declare as output", PortType.OUTPUT, keyboard.getPort(PortType.OUTPUT.getType()).getRight());
        Assert.assertEquals("Should get the output port", keyboard.getGate(), keyboard.getPort(PortType.GATE.getType()).getLeft());
        Assert.assertEquals("Should be declare as gate", PortType.GATE, keyboard.getPort(PortType.GATE.getType()).getRight());

        Assert.assertNull("Should get null", keyboard.getPort("port_qui_n'existe_pas"));
    }

    @Test
    public void updateTest(){
        SubjectKeyboard subjectKeyboard = new SubjectKeyboard() {
            @Override
            public void register(Obseurveur o) {

            }

            @Override
            public void remove(Obseurveur o) {

            }

            @Override
            public void notifyObseurveur() {

            }

            @Override
            public void receiveNote(Keyboard.Note note) {
                Assert.assertEquals("There should be no note", Keyboard.Note.NONE, note);
            }

            @Override
            public void receiveOctave(int octave) {
                Assert.assertEquals("The octave should be 0", 0, octave,0);
            }
        };

        keyboard.update(subjectKeyboard);
    }
}
