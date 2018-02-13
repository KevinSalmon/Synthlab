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
import utils.CableManager;
import utils.PortType;
import java.net.URL;
import java.util.ResourceBundle;

public class MixController implements Initializable, SubjectMixer {

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
    private CableManager cableManager;

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
            cableManager = CableManager.getInstance();
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
    public double getIn1DbAttenuation() {
        return sldIn1.getValue();
    }

    @Override
    public double getIn2DbAttenuation() {
        return sldIn2.getValue();
    }

    @Override
    public double getIn3DbAttenuation() {
        return sldIn3.getValue();
    }

    @Override
    public double getIn4DbAttenuation() {
        return sldIn4.getValue();
    }
}
