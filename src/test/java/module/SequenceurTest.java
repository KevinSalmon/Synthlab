package module;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PortType;

public class SequenceurTest {
    private Sequenceur sequenceur;


    @Before
    public void init(){
        sequenceur = new Sequenceur();
    }
    @Test
    public void getPortTest(){
        Assert.assertEquals("Ports must be equals", sequenceur.getInputPort(), sequenceur.getPort(PortType.INPUT.getType()).getLeft());
        Assert.assertEquals("Ports must be equals", sequenceur.getOutputPort(), sequenceur.getPort(PortType.OUTPUT.getType()).getLeft());
        Assert.assertNull("Does not exist", sequenceur.getPort("fake"));
    }

    @Test
    public void getReference(){
        Assert.assertEquals("Must be equals", sequenceur, sequenceur.getReference());
    }
}
