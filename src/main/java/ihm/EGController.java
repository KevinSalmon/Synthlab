package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectEG;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sauvegarde.SavedEG;
import sauvegarde.SavedModule;
import utils.CableManager;
import utils.PortType;
import java.net.URL;
import java.util.ResourceBundle;

public class EGController implements Initializable, SubjectEG, SuperController {

    @FXML
    private Pane border;

    @FXML
    private Slider attack;

    @FXML
    private Slider decay;

    @FXML
    private Slider sustain;

    @FXML
    private Slider release;

    @FXML
    private Circle in;

    @FXML
    private Circle out;

    @FXML
    private Button delete;

    private Obseurveur<SubjectEG> obseuveurEG;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        attack.setOnKeyReleased(e ->notifyObseurveur());
        attack.setOnMouseClicked(e -> notifyObseurveur());
        attack.setOnMouseMoved(e -> notifyObseurveur());

        decay.setOnKeyReleased(e ->notifyObseurveur());
        decay.setOnMouseClicked(e -> notifyObseurveur());
        decay.setOnMouseMoved(event -> notifyObseurveur());

        sustain.setOnKeyReleased(e ->notifyObseurveur());
        sustain.setOnMouseClicked(e -> notifyObseurveur());
        sustain.setOnMouseMoved(event -> notifyObseurveur());

        release.setOnKeyReleased(e ->notifyObseurveur());
        release.setOnMouseClicked(e -> notifyObseurveur());
        release.setOnMouseMoved(event -> notifyObseurveur());

        delete.setOnMouseClicked(eh -> Controller.getInstance().removeWithConfirmPopup(obseuveurEG, border));
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseuveurEG = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in, o.getReference(), PortType.INPUT, border);
            cableManager.addListener(out, o.getReference(), PortType.OUTPUT, border);
        }
    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null) obseuveurEG =null;
    }

    @Override
    public void notifyObseurveur() {
        obseuveurEG.update(this);
    }

    @Override
    public double getAttackValue() {
        return attack.getValue();
    }

    @Override
    public double getDecayValue() {
        return decay.getValue();
    }

    @Override
    public double getSustainValue() {
        return sustain.getValue();
    }

    @Override
    public double getReleaseValue() {
        return release.getValue();
    }

    @Override
    public SavedModule createMemento() {
        return new SavedEG(border.getLayoutX(), border.getLayoutY(), getAttackValue(), getDecayValue(), getSustainValue(), getReleaseValue());
    }

    @Override
    public void loadProperties(SavedModule module) {
        SavedEG saved = (SavedEG)module;
        attack.setValue(saved.getAttack());
        decay.setValue(saved.getDecay());
        sustain.setValue(saved.getSustain());
        release.setValue(saved.getRelease());
        notifyObseurveur();
    }

    @Override
    public Circle getPort(PortType portType) {
        if (portType.equals(PortType.INPUT)) {
            return this.in;
        }
        if (portType.equals(PortType.OUTPUT)) {
            return this.out;
        }
        return null;
    }
}
