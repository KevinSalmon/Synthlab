package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectVCO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.VCO;
import utils.PortType;
import utils.CableManager;
import utils.OscillatorType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class VCOControllerIHM implements Initializable, SubjectVCO {

    @FXML
    private Pane border;

    @FXML
    private Slider octaveSlider;

    @FXML
    private Slider reglageFinSlider;

    @FXML
    private Slider typeOndeSlider;

    @FXML
    private Circle fm;

    @FXML
    private Circle out;

    @FXML
    private Button delete;

    @FXML
    private RadioButton LFO;

    @FXML
    private Label frequency;

    private Obseurveur<SubjectVCO> obseuveurVCO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        octaveSlider.setOnKeyReleased(e ->notifyObseurveur());
        octaveSlider.setOnMouseClicked(e -> notifyObseurveur());
        octaveSlider.setOnMouseMoved(e -> notifyObseurveur());

        reglageFinSlider.setOnKeyReleased(e ->notifyObseurveur());
        reglageFinSlider.setOnMouseClicked(e -> notifyObseurveur());
        reglageFinSlider.setOnMouseMoved(event -> notifyObseurveur());

        typeOndeSlider.setOnKeyReleased(e ->notifyObseurveur());
        typeOndeSlider.setOnMouseClicked(e -> notifyObseurveur());
        typeOndeSlider.setOnMouseMoved(event -> notifyObseurveur());

        LFO.setOnMouseClicked(eh -> obseuveurVCO.update(this));

        delete.setOnMouseClicked(eh -> Controller.getInstance().removeWithConfirmPopup(obseuveurVCO, border));
    }

    private OscillatorType getOscillatorType(double index){
        if (index == 0.0){
            return OscillatorType.SQUARE;
        }
        if (index == 1.0){
            return OscillatorType.TRIANGLE;
        }
        if (index == 2.0){
            return OscillatorType.SAWTOOTH;
        }
        return null;
    }

    @Override
    public int getOctaveValue() {
        return new Double(octaveSlider.getValue()).intValue();
    }

    @Override
    public double getReglageFinValue() {
        return reglageFinSlider.getValue();
    }

    @Override
    public OscillatorType getOscillatorType() {
       return getOscillatorType(typeOndeSlider.getValue());
    }

    @Override
    public boolean isLFOActive() { return LFO.isSelected(); }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseuveurVCO = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(fm, o.getReference(), PortType.FM, border);
            cableManager.addListener(out, o.getReference(), PortType.OUTPUT, border);
            frequency.setText(frequency.getText() + String.valueOf(((VCO)obseuveurVCO.getReference()).getFrequency())+ " Hz");
        }
    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null) obseuveurVCO=null;
    }

    @Override
    public void notifyObseurveur() {
        obseuveurVCO.update(this);
        frequency.setText("fréquence "+ ((VCO)obseuveurVCO.getReference()).getFrequency()+" Hz");
    }
}
