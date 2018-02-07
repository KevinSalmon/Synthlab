package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import controller.Obseurveur;
import controller.SubjectEG;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import signal.AudioSignal;
import utils.PortType;

public class EGTest {
    public static EG eg;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        eg = new EG();
        synth.add(eg);
    }
    
    @Test
    public void TestInit(){
        Assert.assertEquals("Attack should be 0.1", 0.1, eg.getAttack(),0);
        Assert.assertEquals("Decay should be 0.2", 0.2, eg.getDecay(),0);
        Assert.assertEquals("Sustain should be 0.5", 0.5, eg.getSustain(),0);
        Assert.assertEquals("Release should be 0.3", 0.3, eg.getRelease(),0);
    }

    @Test
    public void GetterSetterTest(){

        //Attack
        eg.setAttack(3);
        Assert.assertEquals("Attack should be 3",3, eg.getAttack(),0);
        eg.setAttack(60);
        Assert.assertEquals("Attack should be 8",8, eg.getAttack(),0);
        eg.setAttack(-10);
        Assert.assertEquals("Attack should be 0.01",0.01, eg.getAttack(),0);

        //Decay
        eg.setDecay(1);
        Assert.assertEquals("Decay should be 1",1, eg.getDecay(),0);
        eg.setDecay(60);
        Assert.assertEquals("Decay should be 2",2, eg.getDecay(),0);
        eg.setDecay(-10);
        Assert.assertEquals("Decay should be 0",0, eg.getDecay(),0);

        //Sustain
        eg.setSustain(0.123456);
        Assert.assertEquals("Sustain should be 0.123456",0.123456, eg.getSustain(),0);
        eg.setSustain(60);
        Assert.assertEquals("Sustain should be 1",1, eg.getSustain(),0);
        eg.setSustain(-10);
        Assert.assertEquals("Sustain should be 0",0, eg.getSustain(),0);

        //Release
        eg.setRelease(0.123456);
        Assert.assertEquals("Release should be 0.123456",0.123456, eg.getRelease(),0);
        eg.setRelease(60);
        Assert.assertEquals("Release should be 8",8, eg.getRelease(),0);
        eg.setRelease(-10);
        Assert.assertEquals("Release should be 0.01",0.01, eg.getRelease(),0);

        //Reference
        Assert.assertEquals("Should get the EG itself", eg, eg.getReference());
    }

    @Test
    public void GetPortTest(){
        Assert.assertEquals("Should get the output port", eg.getOutput(), eg.getPort(PortType.OUTPUT.getType()).getLeft());
        Assert.assertEquals("Should be declare as output", PortType.OUTPUT, eg.getPort(PortType.OUTPUT.getType()).getRight());
        Assert.assertEquals("Should get the input port", eg.getInput(), eg.getPort(PortType.INPUT.getType()).getLeft());
        Assert.assertEquals("Should be declare as input", PortType.INPUT, eg.getPort(PortType.INPUT.getType()).getRight());

        Assert.assertNull("Should get null", eg.getPort("port_qui_n'existe_pas"));
    }

    @Test
    public void UpdateTest() {
        SubjectEG subject = new SubjectEG() {
            Obseurveur o;

            @Override
            public double getAttackValue() {
                return 3;
            }

            @Override
            public double getDecayValue() {
                return 1.23456;
            }

            @Override
            public double getSustainValue() {
                return 0.123456;
            }

            @Override
            public double getReleaseValue() {
                return 4;
            }

            @Override
            public void register(Obseurveur o) {
                this.o = o;
            }

            @Override
            public void remove(Obseurveur o) {

            }

            @Override
            public void notifyObseurveur() {
                o.update(this  );
            }
        };

        subject.register(eg);
        subject.notifyObseurveur();

        Assert.assertEquals("Attack must be 3", 3, eg.getAttack(),0);
        Assert.assertEquals("Decay must be 1.23456", 1.23456, eg.getDecay(),0);
        Assert.assertEquals("Sustain must be 0.123456", 0.123456, eg.getSustain(),0);
        Assert.assertEquals("Realease must be 4", 4, eg.getRelease(),0);

    }
}
