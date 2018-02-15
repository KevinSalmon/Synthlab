package controller;

import com.jsyn.Synthesizer;
import com.sun.org.apache.bcel.internal.classfile.Unknown;
import ihm.IHMController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import module.Module;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.omg.CORBA.portable.UnknownException;
import rule.JavaFXThreadingRule;
import utils.FxmlFilesNames;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.UnexpectedException;

public class ControllerTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private Controller controller;

    @Before
    public void init(){

        Assert.assertNotNull("getInstance should never return null", Controller.getInstance());
        controller = Controller.getInstance();
        Assert.assertEquals("getInstance should not create a new controller",controller, Controller.getInstance());
    }

    @Test
    public void initTest(){
        Assert.assertNotNull("The controller should have a synthetizer", controller.synth);
        Assert.assertTrue("The synthetizer should be running", controller.synth.isRunning());
        Assert.assertNull("The controller should not have a ihmController", controller.ihmController);
        Assert.assertEquals("The getter don't return the good synth", controller.synth, controller.getSynth());
    }

    @Test
    public void CreateTest() {
        Synthesizer synth = controller.synth;
        Pane moduleout = controller.createModule(FxmlFilesNames.MODULE_OUT);
        Assert.assertEquals("Id should be 'module'", "module", moduleout.getId());
        Assert.assertEquals("StyleClass should be 'out'", "out", moduleout.getStyleClass().get(0));


        Pane vco = controller.createModule(FxmlFilesNames.VCO);
        Assert.assertEquals("Id should be 'module'", "module", vco.getId());
        Assert.assertEquals("StyleClass should be 'vco'", "vco", vco.getStyleClass().get(0));

        Pane vca = controller.createModule(FxmlFilesNames.VCA);
        Assert.assertEquals("Id should be 'module'", "module", vca.getId());
        Assert.assertEquals("StyleClass should be 'vca'", "vca", vca.getStyleClass().get(0));

        Pane eg = controller.createModule(FxmlFilesNames.EG);
        Assert.assertEquals("Id should be 'module'", "module", eg.getId());
        Assert.assertEquals("StyleClass should be 'eg'", "eg", eg.getStyleClass().get(0));

        Pane bb = controller.createModule(FxmlFilesNames.BRUITBLANC);
        Assert.assertEquals("Id should be 'module'", "module", bb.getId());
        Assert.assertEquals("StyleClass should be 'blanc'", "blanc", bb.getStyleClass().get(0));

        Pane mix = controller.createModule(FxmlFilesNames.MIX);
        Assert.assertEquals("Id should be 'module'", "module", mix.getId());
        Assert.assertEquals("StyleClass should be 'mix'", "mix", mix.getStyleClass().get(0));

        Pane osc = controller.createModule(FxmlFilesNames.OSCILLOSCOPE);
        Assert.assertEquals("Id should be 'module'", "module", osc.getId());
        Assert.assertEquals("StyleClass should be 'oscillo'", "oscillo", osc.getStyleClass().get(0));

        Pane rep = controller.createModule(FxmlFilesNames.REP);
        Assert.assertEquals("Id should be 'module'", "module", rep.getId());
        Assert.assertEquals("StyleClass should be 'rep'", "rep", rep.getStyleClass().get(0));

        Pane seq = controller.createModule(FxmlFilesNames.SEQ);
        Assert.assertEquals("Id should be 'module'", "module", seq.getId());
        Assert.assertEquals("StyleClass should be 'seq'", "seq", seq.getStyleClass().get(0));

        Pane vcfhp = controller.createModule(FxmlFilesNames.VCFHP);
        Assert.assertEquals("Id should be 'module'", "module", vcfhp.getId());
        Assert.assertEquals("StyleClass should be 'vcfhp'", "vcfhp", vcfhp.getStyleClass().get(0));

        Pane vcflp = controller.createModule(FxmlFilesNames.VCFLP);
        Assert.assertEquals("Id should be 'module'", "module", vcflp.getId());
        Assert.assertEquals("StyleClass should be 'vcflp'", "vcflp", vcflp.getStyleClass().get(0));


        Parent root = new Parent() {
        };
        Scene scene = new Scene(root);
        controller.setScene(scene);
        Pane key = controller.createModule(FxmlFilesNames.KEYBOARD);
        Assert.assertEquals("Id should be 'module'", "module", key.getId());
        Assert.assertEquals("StyleClass should be 'keyboard'", "keyboard", key.getStyleClass().get(0));

    }

    @Test
    public void createMiniatureTest() {
        Pane mini = controller.createMiniature(FxmlFilesNames.MINIATURE_BRUITBLANC);
        Assert.assertEquals("Id should be 'miniature'", "miniature", mini.getId());
        Assert.assertEquals("StyleClass should be 'blanc'", "blanc", mini.getStyleClass().get(0));
    }

    @Test
    public void closeTest(){
        controller.close();

        //TODO trouver un moyen "sûr" de vérifier la fermeture du synthé

        //Assert.assertFalse("Synthesizer should be close", controller.synth.isRunning());
    }

    @Test
    public void setIHMTest(){
        IHMController realihm = controller.ihmController;
        IHMController ihmController = new IHMController(){
            @Override
            public void init(boolean initModuleMenu) {
                Assert.assertTrue("The init should be true", initModuleMenu);
            }
        };
        controller.setIhmController(ihmController);
        Assert.assertEquals("The IHMController should have changed",ihmController, controller.ihmController);
        Assert.assertEquals("The getter do not return the good ihm",ihmController, controller.getIhmController());
        Assert.assertEquals("The IHMController should have the controller as it's controller",controller, ihmController.getController());
        controller.ihmController = realihm;
    }

}
