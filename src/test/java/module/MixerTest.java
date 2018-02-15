package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.softsynth.math.AudioMath;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectMixer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;
import utils.Tuple;

import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;

public class MixerTest {

    public static Mixer mixer;
    public static VCO vco1;
    public static VCO vco3;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        mixer = new Mixer();
        synth.add(mixer);

        vco1 = new VCO();
        vco1.setOctave(-1);
        vco1.setReglageFin(-0.65);
        vco1.getOutput().connect(mixer.getIn1());
        synth.add(vco1);


        vco3 = new VCO();
        vco3.setOctave(1);
        vco3.setReglageFin(2.4);
        vco3.getOutput().connect(mixer.getIn3());
        synth.add(vco3);
    }

    @Test
    public void getReferenceTest(){
        Assert.assertTrue("getReference does not return the object", mixer.getReference().equals(mixer));
    }


    @Test
    public void Input1Test(){
        UnitInputPort input = mixer.getIn1();

        Assert.assertTrue("The name of the input is not '" + PortType.INPUT1.getType() + "' but " + input.getName(), input.getName().equals(PortType.INPUT1.getType()));
        Assert.assertTrue("The input must have only one part",input.getNumParts() == 1);
        Assert.assertTrue("The input should have one connection from now", input.isConnected());
    }

    @Test
    public void Input2Test(){
        UnitInputPort input = mixer.getIn2();

        Assert.assertTrue("The name of the input is not '" + PortType.INPUT2.getType() + "' but " + input.getName(), input.getName().equals(PortType.INPUT2.getType()));
        Assert.assertTrue("The input must have only one part",input.getNumParts() == 1);
        Assert.assertFalse("The input should have no connection from now", input.isConnected());
    }

    @Test
    public void Input3Test(){
        UnitInputPort input = mixer.getIn3();

        Assert.assertTrue("The name of the input is not '" + PortType.INPUT3.getType() + "' but " + input.getName(), input.getName().equals(PortType.INPUT3.getType()));
        Assert.assertTrue("The input must have only one part",input.getNumParts() == 1);
        Assert.assertTrue("The input should have one connection from now", input.isConnected());
    }

    @Test
    public void Input4Test(){
        UnitInputPort input = mixer.getIn4();

        Assert.assertTrue("The name of the input is not '" + PortType.INPUT4.getType() + "' but " + input.getName(), input.getName().equals(PortType.INPUT4.getType()));
        Assert.assertTrue("The input must have only one part",input.getNumParts() == 1);
        Assert.assertFalse("The input should have no connection from now", input.isConnected());
    }

    @Test
    public void OutputTest(){
        UnitOutputPort output = mixer.getOutput();

        Assert.assertTrue("The name of the output is not 'output1'",output.getName().equals(PortType.OUTPUT.getType()));
        Assert.assertTrue("The output must have only one part",output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());
    }

    @Test
    public void getPortTest(){
        Assert.assertTrue("getPort(input1) does not return the input",
            mixer.getPort(PortType.INPUT1.getType()).getLeft().getName().equals(
                    new Tuple<>(mixer.getIn1(), PortType.INPUT1).getLeft().getName()));

        Assert.assertTrue("getPort(input2) does not return the input",
                mixer.getPort(PortType.INPUT2.getType()).getLeft().getName().equals(
                        new Tuple<>(mixer.getIn2(), PortType.INPUT2).getLeft().getName()));

        Assert.assertTrue("getPort(input3) does not return the input",
                mixer.getPort(PortType.INPUT3.getType()).getLeft().getName().equals(
                        new Tuple<>(mixer.getIn3(), PortType.INPUT3).getLeft().getName()));

        Assert.assertTrue("getPort(input4) does not return the input",
                mixer.getPort(PortType.INPUT4.getType()).getLeft().getName().equals(
                        new Tuple<>(mixer.getIn4(), PortType.INPUT4).getLeft().getName()));

        Assert.assertTrue("getPort(output) does not return the output",
            mixer.getPort(PortType.OUTPUT.getType()).getLeft().getName().equals(
                    new Tuple<>(mixer.getOutput(), PortType.OUTPUT).getLeft().getName()));

        Assert.assertNull("getPort(\"\") is not null", mixer.getPort(""));
    }

    class SubjectMixerTest implements SubjectMixer {
        private double in1db;
        private double in2db;
        private double in3db;
        private double in4db;

        public SubjectMixerTest(double in1db, double in2db, double in3db, double in4db) {
            this.in1db = in1db;
            this.in2db = in2db;
            this.in3db = in3db;
            this.in4db = in4db;
        }

        @Override public double getIn1DbAttenuation() { return in1db; }
        @Override public double getIn2DbAttenuation() { return in2db; }
        @Override public double getIn3DbAttenuation() { return in3db; }
        @Override public double getIn4DbAttenuation() { return in4db; }
        @Override public void register(Obseurveur o) { }
        @Override public void remove(Obseurveur o) { }
        @Override public void notifyObseurveur() { }
    }

    @Test
    public void updateTest() {
        mixer.update(new SubjectMixerTest(0.12, 5.3, -37.9, -0.0));
        assertEquals(0.12, mixer.getIn1dbAttenuation());
        assertEquals(5.3, mixer.getIn2dbAttenuation());
        assertEquals(-37.9, mixer.getIn3dbAttenuation());
        assertEquals(0.0, mixer.getIn4dbAttenuation(), 0.0);
    }

    @Test
    public void updateWrongTest() {
        mixer.update(new SubjectMixerTest(12.1, 20.3, 99.9, 999999.99));
        assertEquals(0.0, mixer.getIn1dbAttenuation());
        assertEquals(0.0, mixer.getIn2dbAttenuation());
        assertEquals(0.0, mixer.getIn3dbAttenuation());
        assertEquals(0.0, mixer.getIn4dbAttenuation());
    }

    @Test
    public void emptyInputToOuputTest() { // 0.0 => input => 0.0
        for (int i = 0; i < 1000; i++) {
            mixer.generate();

            double[] mixerMod = mixer.getOutput().getValues();
            for (int j = 0; j < mixerMod.length; j++) {
                assertEquals(0.0, mixerMod[j]);
            }
        }
    }

    @Test
    public void oneInputToOuputTest() { // vco1 => input => vco1
        for (int i = 0; i < 1000; i++) {
            vco1.generate();
            mixer.generate();

            double[] vco1Mod = vco1.getOutput().getValues();
            double[] mixerMod = mixer.getOutput().getValues();

            for (int j = 0; j < mixerMod.length; j++) {
                assertEquals(vco1Mod[j], mixerMod[j]);
            }
        }
    }

    @Test
    public void twoInputNoAttenuationToOuputTest() { // vco1 + 0.0 + vco3 + 0.0 => vco1 + vco3
        for (int i = 0; i < 1000; i++) {
            vco1.generate();
            vco3.generate();
            mixer.generate();

            double[] vco1Values = vco1.getOutput().getValues();
            double[] vco3Values = vco3.getOutput().getValues();
            double[] mixerValues = mixer.getOutput().getValues();

            for (int j = 0; j < mixerValues.length; j++) {
                assertEquals(vco1Values[j] + vco3Values[j], mixerValues[j]);
            }
        }
    }

    @Test
    public void twoInputWithAttenuationToOuputTest() {
        mixer.update(new SubjectMixerTest(-5.0, 6.0, 11.5, -0.0));

        for (int i = 0; i < 1000; i++) {
            vco1.generate();
            vco3.generate();
            mixer.generate();

            double[] vco1Values = vco1.getOutput().getValues();
            double[] vco3Values = vco3.getOutput().getValues();
            double[] mixerValues = mixer.getOutput().getValues();

            for (int j = 0; j < mixerValues.length; j++) {
                double excepted = (vco1Values[j] * AudioMath.decibelsToAmplitude(-5.0)) + (vco3Values[j] * AudioMath.decibelsToAmplitude(11.5));
                excepted = (excepted > 1.0) ? excepted = 1.0 : excepted;
                excepted = (excepted < -1.0) ? excepted = -1.0 : excepted;

                assertEquals(excepted, mixerValues[j]);
            }
        }
    }

    @Test
    public void infiniteTest() {
        mixer.update(new SubjectMixerTest(-100.0, -101.0, -999.8, -99999.9));
        for (int i = 0; i < 1000; i++) {
            vco1.generate();
            vco3.generate();
            mixer.generate();

            double[] mixerValues = mixer.getOutput().getValues();

            for (int j = 0; j < mixerValues.length; j++) {
                assertEquals(0.0, mixerValues[j]);
            }
        }
    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = mixer.getAllPorts();
        assertTrue(list.contains(PortType.INPUT1));
        assertTrue(list.contains(PortType.INPUT2));
        assertTrue(list.contains(PortType.INPUT3));
        assertTrue(list.contains(PortType.INPUT4));
        assertTrue(list.contains(PortType.OUTPUT));
    }
}
