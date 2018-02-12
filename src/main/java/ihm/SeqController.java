package ihm;

import controller.Obseurveur;
import controller.SubjectSeq;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.Sequenceur;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;

public class SeqController implements Initializable, SubjectSeq {

    @FXML
    Pane pane;
    @FXML
    Slider sld1;
    @FXML
    Slider sld2;
    @FXML
    Slider sld3;
    @FXML
    Slider sld4;
    @FXML
    Slider sld5;
    @FXML
    Slider sld6;
    @FXML
    Slider sld7;
    @FXML
    Slider sld8;
    @FXML
    Circle in;
    @FXML
    Circle out;

    private Sequenceur sequenceur;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CableManager cableManager = CableManager.getInstance();
        cableManager.addListener(in, sequenceur, PortType.INPUT, pane);
        cableManager.addListener(out, sequenceur, PortType.OUTPUT, pane);

    }

    @Override
    public void register(Obseurveur o) {

    }

    @Override
    public void remove(Obseurveur o) {

    }

    @Override
    public void notifyObseurveur() {

    }
}
