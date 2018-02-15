package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectMixer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sauvegarde.SavedMix;
import sauvegarde.SavedModule;
import utils.CableManager;
import utils.PortType;
import java.net.URL;
import java.util.ResourceBundle;

public class MixController implements Initializable, SubjectMixer, SuperController {

    @FXML Pane pane;
    @FXML Circle in1;
    @FXML Circle in2;
    @FXML Circle in3;
    @FXML Circle in4;
    @FXML Circle out;
    @FXML Slider sldIn1;
    @FXML Slider sldIn2;
    @FXML Slider sldIn3;
    @FXML Slider sldIn4;
    @FXML Button delete;

    private Obseurveur<SubjectMixer> obseurveurs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sldIn1.setOnMouseMoved(e -> notifyObseurveur());
        sldIn1.setOnMouseClicked(e -> notifyObseurveur());
        sldIn1.setOnKeyReleased(e -> notifyObseurveur());
        sldIn1.setOnMouseDragged(e -> notifyObseurveur());

        sldIn2.setOnMouseMoved(e -> notifyObseurveur());
        sldIn2.setOnMouseClicked(e -> notifyObseurveur());
        sldIn2.setOnKeyReleased(e -> notifyObseurveur());
        sldIn2.setOnMouseDragged(e -> notifyObseurveur());

        sldIn3.setOnMouseMoved(e -> notifyObseurveur());
        sldIn3.setOnMouseClicked(e -> notifyObseurveur());
        sldIn3.setOnKeyReleased(e -> notifyObseurveur());
        sldIn3.setOnMouseDragged(e -> notifyObseurveur());

        sldIn4.setOnMouseMoved(e -> notifyObseurveur());
        sldIn4.setOnMouseClicked(e -> notifyObseurveur());
        sldIn4.setOnKeyReleased(e -> notifyObseurveur());
        sldIn4.setOnMouseDragged(e -> notifyObseurveur());

        delete.setOnMouseClicked(eh -> Controller.getInstance().removeWithConfirmPopup(obseurveurs, pane));
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseurveurs = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in1, obseurveurs.getReference(), PortType.INPUT1, pane);
            cableManager.addListener(in2, obseurveurs.getReference(), PortType.INPUT2, pane);
            cableManager.addListener(in3, obseurveurs.getReference(), PortType.INPUT3, pane);
            cableManager.addListener(in4, obseurveurs.getReference(), PortType.INPUT4, pane);
            cableManager.addListener(out, obseurveurs.getReference(), PortType.OUTPUT, pane);
        }
    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null) {
            obseurveurs = null;
        }
    }

    @Override
    public void notifyObseurveur() {
        obseurveurs.update(this);
    }

    @Override
    public double getIn1attenuation() {
        return sldIn1.getValue();
    }

    @Override
    public double getIn2attenuation() {
        return sldIn2.getValue();
    }

    @Override
    public double getIn3attenuation() {
        return sldIn3.getValue();
    }

    @Override
    public double getIn4attenuation() {
        return sldIn4.getValue();
    }

    @Override
    public SavedModule createMemento() {
        return new SavedMix(pane.getLayoutX(), pane.getLayoutY(), getIn1attenuation(), getIn2attenuation(), getIn3attenuation(), getIn4attenuation());
    }

    @Override
    public void loadProperties(SavedModule module) {
        SavedMix saved = (SavedMix)module;
        sldIn1.setValue(saved.getIn1attenuation());
        sldIn2.setValue(saved.getIn2attenuation());
        sldIn3.setValue(saved.getIn3attenuation());
        sldIn4.setValue(saved.getIn4attenuation());
        notifyObseurveur();
    }

    @Override
    public Circle getPort(PortType portType) {
        if (portType.equals(PortType.INPUT1)) { return this.in1; }
        if (portType.equals(PortType.INPUT2)) { return this.in2; }
        if (portType.equals(PortType.INPUT3)) { return this.in3; }
        if (portType.equals(PortType.INPUT4)) { return this.in4; }
        if (portType.equals(PortType.OUTPUT)) { return this.out; }
        return null;
    }
}
