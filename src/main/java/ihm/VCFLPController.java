package ihm;

import controller.Obseurveur;
import controller.SubjectVCFLP;
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

public class VCFLPController extends SuperController implements Initializable, SubjectVCFLP{

    @FXML
    Circle fm;

    @FXML
    Circle in;

    @FXML
    Circle out;

    @FXML
    Slider resonance;

    @FXML
    Slider f0;

    @FXML
    Pane pane;

    @FXML
    Button delete;

    private Obseurveur<SubjectVCFLP> vcflpObseurveur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        f0.setOnKeyReleased(e ->notifyObseurveur());
        f0.setOnMouseClicked(e -> notifyObseurveur());
        f0.setOnMouseMoved(event -> notifyObseurveur());

        resonance.setOnKeyReleased(e ->notifyObseurveur());
        resonance.setOnMouseClicked(e -> notifyObseurveur());
        resonance.setOnMouseMoved(event -> notifyObseurveur());

        delete.setOnMouseClicked(eh -> {
            removeWithConfirmPopup(vcflpObseurveur, pane);
        });
    }


    @Override
    public double getFrequency() {
        return f0.getValue();
    }

    @Override
    public double getResonance() {
        return resonance.getValue();
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            vcflpObseurveur = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in, vcflpObseurveur.getReference(), PortType.INPUT, pane);
            cableManager.addListener(out, vcflpObseurveur.getReference(), PortType.OUTPUT, pane);
            cableManager.addListener(fm, vcflpObseurveur.getReference(), PortType.FM, pane);

        }

    }

    @Override
    public void remove(Obseurveur o) {
        if(o.equals(vcflpObseurveur)){
            vcflpObseurveur = null;
        }

    }

    @Override
    public void notifyObseurveur() {
        vcflpObseurveur.update(this);

    }
}
