package module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;

import ihm.observer.Obseurveur;
import ihm.observer.SubjectOscillo;
import utils.PortType;
import utils.Tuple;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class OscilloscopeTest {

    private static Oscilloscope oscillo;

    @Before
    public void init(){
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        oscillo = new Oscilloscope();
        synth.add(oscillo);
    }

    @Test
    public void initTest(){

    }

    @Test
    public void ResultatTest(){
        UnitInputPort input = oscillo.getInput();
        UnitOutputPort output = oscillo.getOutput();

        for (int i = 0; i < 10000; i++) {
            oscillo.generate();
            for (int j = 0; j < output.getValues().length; j++) {
                Assert.assertEquals("The output is not equals to the input",input.getValues()[j],output.getValues()[j],0.0);
            }
        }
    }

    @Test
    public void getPortTest(){
        Tuple<UnitPort, PortType> proto = oscillo.getPort(PortType.OUTPUT.getType());
        Tuple<UnitPort, PortType> protoin = oscillo.getPort(PortType.INPUT.getType());

        Assert.assertEquals("Output is not an outputPort",PortType.OUTPUT, proto.getRight());
        Assert.assertEquals("Input is not an input port ",PortType.INPUT, protoin.getRight());


        Assert.assertNull("Should get null", oscillo.getPort("port_qui_n'existe_pas"));

        //Reference
        //    Assert.assertEquals("Should get the oscillo itself", oscillo, oscillo.getReference());
    }



    @Test
    public void getReferenceTest(){
        Assert.assertTrue("getReference does not return the object", oscillo.getReference().equals(oscillo));
    }


    @Test
    public void OutputTest(){

        UnitOutputPort output = oscillo.getOutput();

        Assert.assertTrue("The name of the output is not 'output'", output.getName().equals(PortType.OUTPUT.getType()));
        Assert.assertTrue("The output must have only one part", output.getNumParts() == 1);
        Assert.assertTrue("The output should not have any connection from now", !output.isConnected());

    }



    @Test
    public void UpdateTest() {

        oscillo.getScreen();

        SubjectOscillo subject = new SubjectOscillo() {
            Obseurveur obs;

            @Override
            public void register(Obseurveur o) {
                this.obs = o;

            }

            @Override
            public void remove(Obseurveur o) {

            }

            @Override
            public void notifyObseurveur() {
                obs.update(this);
            }

            @Override
            public void receiveSeries(double[] series) {


            }


        };

    }

    @Test
    public void getAllPortsTest() {
        List<PortType> list = oscillo.getAllPorts();
        assertTrue(list.contains(PortType.INPUT));
        assertTrue(list.contains(PortType.OUTPUT));
    }
}

