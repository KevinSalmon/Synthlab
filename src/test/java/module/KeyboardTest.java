package module;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.FxmlFilesNames;
import utils.PortType;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class KeyboardTest {
    Keyboard keyboard;

    //Key Events
    //Key Pressed
    KeyEvent QPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Q.toString(), KeyCode.Q.toString(), KeyCode.Q, false, false, false, false);
    KeyEvent SPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.S.toString(), KeyCode.S.toString(), KeyCode.S, false, false, false, false);
    KeyEvent DPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.D.toString(), KeyCode.D.toString(), KeyCode.D, false, false, false, false);
    KeyEvent FPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.F.toString(), KeyCode.F.toString(), KeyCode.F, false, false, false, false);
    KeyEvent GPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.G.toString(), KeyCode.G.toString(), KeyCode.G, false, false, false, false);
    KeyEvent HPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.H.toString(), KeyCode.H.toString(), KeyCode.H, false, false, false, false);
    KeyEvent JPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.J.toString(), KeyCode.J.toString(), KeyCode.J, false, false, false, false);
    KeyEvent KPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.K.toString(), KeyCode.K.toString(), KeyCode.K, false, false, false, false);
    KeyEvent ZPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Z.toString(), KeyCode.Z.toString(), KeyCode.Z, false, false, false, false);
    KeyEvent EPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.E.toString(), KeyCode.E.toString(), KeyCode.E, false, false, false, false);
    KeyEvent TPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.T.toString(), KeyCode.T.toString(), KeyCode.T, false, false, false, false);
    KeyEvent YPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Y.toString(), KeyCode.Y.toString(), KeyCode.Y, false, false, false, false);
    KeyEvent UPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.U.toString(), KeyCode.U.toString(), KeyCode.U, false, false, false, false);
    KeyEvent WPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.W.toString(), KeyCode.W.toString(), KeyCode.W, false, false, false, false);
    KeyEvent XPressed = new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.X.toString(), KeyCode.X.toString(), KeyCode.X, false, false, false, false);

    //Key Released
    KeyEvent QReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.Q.toString(), KeyCode.Q.toString(), KeyCode.Q, false, false, false, false);
    KeyEvent SReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.S.toString(), KeyCode.S.toString(), KeyCode.S, false, false, false, false);
    KeyEvent DReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.D.toString(), KeyCode.D.toString(), KeyCode.D, false, false, false, false);
    KeyEvent FReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.F.toString(), KeyCode.F.toString(), KeyCode.F, false, false, false, false);
    KeyEvent GReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.G.toString(), KeyCode.G.toString(), KeyCode.G, false, false, false, false);
    KeyEvent HReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.H.toString(), KeyCode.H.toString(), KeyCode.H, false, false, false, false);
    KeyEvent JReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.J.toString(), KeyCode.J.toString(), KeyCode.J, false, false, false, false);
    KeyEvent KReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.K.toString(), KeyCode.K.toString(), KeyCode.K, false, false, false, false);
    KeyEvent ZReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.Z.toString(), KeyCode.Z.toString(), KeyCode.Z, false, false, false, false);
    KeyEvent EReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.E.toString(), KeyCode.E.toString(), KeyCode.E, false, false, false, false);
    KeyEvent TReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.T.toString(), KeyCode.T.toString(), KeyCode.T, false, false, false, false);
    KeyEvent YReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.Y.toString(), KeyCode.Y.toString(), KeyCode.Y, false, false, false, false);
    KeyEvent UReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.U.toString(), KeyCode.U.toString(), KeyCode.U, false, false, false, false);
    KeyEvent WReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.W.toString(), KeyCode.W.toString(), KeyCode.W, false, false, false, false);
    KeyEvent XReleased = new KeyEvent(KeyEvent.KEY_RELEASED, KeyCode.X.toString(), KeyCode.X.toString(), KeyCode.X, false, false, false, false);

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
    }

    @Test
    public void referenceTest(){
        Assert.assertEquals("The reference should be the keyboard itself", keyboard, keyboard.getReference());
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = keyboard.getAllPorts();
        assertTrue(list.contains(PortType.OUTPUT));
    }
}
