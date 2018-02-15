package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectVCO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.OscillatorType;
import utils.PortType;

import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class VCOTest {
    public static VCO vco;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        vco = new VCO();
        synth.add(vco);
    }

    @Test
    public void OutputTest(){
        UnitOutputPort output = vco.getOutput();

        Assert.assertTrue("The name of the output is not 'output'",output.getName().equals("output"));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void FrequencyTest(){
        Assert.assertTrue("The base frequency should be 1000Hz", vco.getFrequency() == 440.0);
    }

    @Test
    public void OctaveTest(){
        Assert.assertTrue("The base octave should be 0", vco.getOctave() == 0);

        vco.increaseOctave(1);


        Assert.assertTrue("The octave should have increase by 1", vco.getOctave() == 1);
        Assert.assertTrue("The frequency should have doubled", vco.getFrequency() == 880);

        vco.increaseOctave(2*VCO.getOctaveMax()-VCO.getOctaveMin());

        Assert.assertTrue("The octave should be at max", vco.getOctave() == VCO.getOctaveMax());
        Assert.assertTrue("The frequency should have doubled 2 times", vco.getFrequency() == 3520);

        vco.decreaseOctave(1);

        Assert.assertTrue("The octave should have decrease by 1", vco.getOctave() == 2);
        Assert.assertTrue("The frequency should have been halved", vco.getFrequency() == 1760);

        vco.decreaseOctave(150);

        Assert.assertTrue("The octave should have decrease only by 4", vco.getOctave() == -2);
        Assert.assertTrue("The frequency should have been halved 4 times", vco.getFrequency() == 110);
    }

    @Test
    public void ReglageFinTest(){
        Assert.assertTrue("The base tuning should be 0", vco.getReglageFin() == 0);

        vco.increaseReglageFin(0.1);

        Assert.assertTrue("The tuning should have increase by 0.1", vco.getReglageFin() == 0.1);
        Assert.assertTrue("The frequency should have slighty increased", vco.getFrequency() == (440 * Math.pow(2,0.1)));

        vco.increaseReglageFin(150);

        Assert.assertTrue("The tuning should have increase only by 0.9", vco.getReglageFin() == 1);
        Assert.assertTrue("The frequency should have nearly doubled", vco.getFrequency() == 880);

        vco.decreaseReglageFin(0.1);

        Assert.assertTrue("The tuning should have decrease by 0.1", vco.getReglageFin() == 0.9);
        Assert.assertTrue("The frequency should have slighty decreased", vco.getFrequency() == (440 * Math.pow(2,0.9)));

        vco.decreaseReglageFin(150);

        Assert.assertTrue("The tuning should have decrease only by 1.9", vco.getReglageFin() == -1);
        Assert.assertTrue("The frequency should have been halved", vco.getFrequency() == 220);
    }

    @Test
    public void ResultatTest(){
        UnitOutputPort osc = vco.getCurrentOsc().getOutput();
        UnitOutputPort output = vco.getOutput();

        for (int i = 0; i < 10000; i++) {
            vco.generate();
            for (int j = 0; j < output.getValues().length; j++) {
                Assert.assertEquals("The output is not equals to the current oscillator output",osc.getValues()[j]*5.0/12.0,output.getValues()[j],0.0);
            }
        }

        vco.switchLFO();

        for (int i = 0; i < 10000; i++) {
            vco.generate();
            for (int j = 0; j < output.getValues().length; j++) {
                Assert.assertEquals("The output is not equals to the current oscillator output",osc.getValues()[j]*5.0/12.0,output.getValues()[j],0.0);
            }
        }
    }

    @Test
    public void OscillatorTest(){
        vco.changeCurrentOsc(OscillatorType.TRIANGLE);
        UnitOscillator osc = vco.getCurrentOsc();
        vco.changeCurrentOsc(OscillatorType.TRIANGLE);
        Assert.assertEquals("The oscillator shouldn't have change",osc, vco.getCurrentOsc());
        vco.changeCurrentOsc(OscillatorType.SAWTOOTH);
        Assert.assertNotEquals("The oscillator should have change",osc, vco.getCurrentOsc());

        vco.changeCurrentOsc(OscillatorType.SAWTOOTH);
        Assert.assertTrue("The oscillator should be a sawtooth one",vco.getCurrentOsc() instanceof SawtoothOscillator);
        vco.changeCurrentOsc(OscillatorType.TRIANGLE);
        Assert.assertTrue("The oscillator should be a triangle one",vco.getCurrentOsc() instanceof TriangleOscillator);
        vco.changeCurrentOsc(OscillatorType.SQUARE);
        Assert.assertTrue("The oscillator should be a square one",vco.getCurrentOsc() instanceof SquareOscillator);
    }

    @Test
    public void GetPortTest(){
        Assert.assertEquals("Should get the output port", vco.getOutput(), vco.getPort(PortType.OUTPUT.getType()).getLeft());
        Assert.assertEquals("Should be declare as output", PortType.OUTPUT, vco.getPort(PortType.OUTPUT.getType()).getRight());
        Assert.assertEquals("Should get the input port", vco.getInput(), vco.getPort(PortType.INPUTFM.getType()).getLeft());
        Assert.assertEquals("Should be declare as fm", PortType.INPUTFM, vco.getPort(PortType.INPUTFM.getType()).getRight());

        Assert.assertNull("Should get null", vco.getPort("port_qui_n'existe_pas"));
    }

    @Test
    public void GetterSetterTest(){

        //Octave
        vco.setOctave(3);
        Assert.assertEquals("Octave should be 3",3, vco.getOctave(),0);
        vco.setOctave(6);
        Assert.assertEquals("Octave should be 3",3, vco.getOctave(),0);
        vco.setOctave(-1);
        Assert.assertEquals("Octave should be -1",-1, vco.getOctave(),0);
        vco.setOctave(-10);
        Assert.assertEquals("Octave should be -2",-2, vco.getOctave(),0);

        //ReglageFin
        vco.setReglageFin(0.2);
        Assert.assertEquals("Tuning should be 0.2",0.2, vco.getReglageFin(),0);
        vco.setReglageFin(6);
        Assert.assertEquals("Tuning should be 1",1, vco.getReglageFin(),0);
        vco.setReglageFin(-0.5);
        Assert.assertEquals("Tuning should be -0.5",-0.5, vco.getReglageFin(),0);

        //Output
        UnitOutputPort to = new UnitOutputPort();
        vco.setOutput(to);
        Assert.assertEquals("The output we get should be the output we set", to, vco.getOutput());

        //AudioSignal
        double as = 440.0;
        vco.setAudioSignal(as);
        assertEquals("The signal we get should be the signal we set", as, vco.getAudioSignal());

        //LFO
        Assert.assertFalse("LFO should not be activate", vco.isLFO());
        vco.setLFO(true);
        Assert.assertTrue("LFO should be activate", vco.isLFO());

        //Reference
        Assert.assertEquals("Should get the VCO itself", vco, vco.getReference());
    }



    @Test
    public void UpdateTest(){
        SubjectVCO subject = new SubjectVCO() {
            Obseurveur o;

            @Override
            public int getOctaveValue() {
                return 1;
            }

            @Override
            public double getReglageFinValue() {
                return 0.346;
            }

            @Override
            public OscillatorType getOscillatorType() {
                return OscillatorType.SQUARE;
            }

            @Override
            public boolean isLFOActive() { return false; }

            @Override
            public void register(Obseurveur o) {
                this.o = o;
            }

            @Override
            public void remove(Obseurveur o) {

            }

            @Override
            public void notifyObseurveur() {
                o.update(this);
            }
        };

        subject.register(vco);
        subject.notifyObseurveur();

        Assert.assertEquals("Tuning should be 0.346",0.346, vco.getReglageFin(),0);
        Assert.assertEquals("Octave should be 1",1, vco.getOctave(),0);
        Assert.assertTrue("The oscillator should be a square one",vco.getCurrentOsc() instanceof SquareOscillator);
    }

    @Test
    public void LFOTest() {
        Assert.assertEquals("The frequency should be at 440", 440, vco.getFrequency(),0);
        vco.switchLFO();
        Assert.assertEquals("The frequency should be at 0.1", 0.1, vco.getFrequency(),0);
        vco.setReglageFin(0.5);
        Assert.assertEquals("The frequency should be at 10", 10, vco.getFrequency(),0.1);
    }

    @Test(expected = NullPointerException.class)
    public void OscillatorException(){
        vco.changeCurrentOsc(null);
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = vco.getAllPorts();
        assertTrue(list.contains(PortType.INPUTFM));
        assertTrue(list.contains(PortType.OUTPUT));
    }
}
