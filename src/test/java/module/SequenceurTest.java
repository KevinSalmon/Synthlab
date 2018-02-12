package module;

import org.junit.Assert;
import org.junit.Test;
import utils.PortType;

public class SequenceurTest {
    private Sequenceur sequenceur;


    @Test
    public void getPortTest(){
        sequenceur = new Sequenceur();
        Assert.assertEquals("Ports must be equals", sequenceur.getInputPort(), sequenceur.getPort(PortType.INPUT.getType()).getLeft());
        Assert.assertEquals("Ports must be equals", sequenceur.getOutputPort(), sequenceur.getPort(PortType.OUTPUT.getType()).getLeft());
    }
}
