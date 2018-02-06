package utils;

import exceptions.OutputException;
import exceptions.PortTypeException;
import com.jsyn.JSyn;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import module.OutputModule;
import module.VCA;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test about the CableManager
 */

public class CableManagerTest extends  CableManager{


    private Circle in = new Circle();

    private Circle out = new Circle();

    public CableManagerTest() {
        super();
    }


    /**
     * Initialization test
     */
    @Test
    public void initTest(){
        Assert.assertTrue(this.instance == null);
        this.instance = this.getInstance();
        Assert.assertTrue(this.instance == CableManager.getInstance());
    }

    /**
     * Test the creation of a line
     * @throws OutputException must not happen
     */
    @Test
    public void setOutputInputTest() throws OutputException, PortTypeException {
        VCA vca = new VCA();
        OutputModule outputModule = new OutputModule(JSyn.createSynthesizer());
        Pane pane = new Pane();
        out.setLayoutX(0.0);
        out.setLayoutY(0.0);
        pane.getChildren().add(out);
        this.setOutput(out, vca);
        assertEquals("Outputs are equals", out, this.currentCable.getOutput());
        pane.getChildren().add(in);
        in.setLayoutX(1.0);
        in.setLayoutY(1.0);
        Cable cableActual = this.currentCable;
        this.setInput(in, outputModule);
        assertEquals("Inputs are equals", in, cableActual.getInput());
        Assert.assertNotNull(cableActual.getLine());
        assertEquals("Start line must equals in", cableActual.getInput().getLayoutX(), cableActual.getLine().getStartX(), 0.1);
        assertEquals("Start line must equals in", cableActual.getInput().getLayoutY(), cableActual.getLine().getStartY(), 0.1);
        assertEquals("Start line must equals in", cableActual.getOutput().getLayoutX(), cableActual.getLine().getEndX(), 0.1);
        assertEquals("Start line must equals in", cableActual.getOutput().getLayoutY(), cableActual.getLine().getEndY(), 0.1);
        assertEquals("size list must be 1", 1, this.cables.size());
    }


    /**
     * Test the update of a point
     */
    @Test
     public void updateOutputInputTest(){
        Pane pane = new Pane();
         out.setLayoutY(0.0);
         out.setLayoutX(0.0);
         in.setLayoutY(1.0);
         in.setLayoutX(1.0);

         pane.getChildren().add(out);
         pane.getChildren().add(in);
         Cable c = new Cable();
         c.setInput(in);
         c.setOutput(out);
         c.setLine(new Line(in.getLayoutX(), in.getLayoutY(), out.getLayoutX(), out.getLayoutY()));
         this.cables.add(c);

         out.setLayoutY(1.0);
         out.setLayoutX(1.0);
         this.updateOutputX(out);
         this.updateOutputY(out);
         assertEquals("out and cable must be equals", out.getLayoutX(), this.cables.get(0).getOutput().getLayoutX(), 0.1);
         assertEquals("out and cable must be equals", out.getLayoutY(), this.cables.get(0).getOutput().getLayoutY(), 0.1);

         this.updateInputX(in);
         this.updateInputY(in);
         assertEquals("in and cable must be equals", in.getLayoutX(), this.cables.get(0).getInput().getLayoutX(), 0.1);
         assertEquals("in and cable must be equals", in.getLayoutY(), this.cables.get(0).getInput().getLayoutY(), 0.1);

     }
}
