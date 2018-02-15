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
import sauvegarde.SavedModule;
import sauvegarde.SavedVCO;
import utils.PortType;
import utils.CableManager;
import utils.OscillatorType;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class VCOControllerIHM implements Initializable, SubjectVCO, SuperController {

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
    private RadioButton lfo;

    @FXML
    private Label frequency;

    private Obseurveur<SubjectVCO> obseuveurVCO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        octaveSlider.setOnKeyReleased(e ->notifyObseurveur());
        octaveSlider.setOnMouseClicked(e -> notifyObseurveur());
        octaveSlider.setOnMouseDragged(e -> notifyObseurveur());

        reglageFinSlider.setOnKeyReleased(e ->notifyObseurveur());
        reglageFinSlider.setOnMouseClicked(e -> notifyObseurveur());
        reglageFinSlider.setOnMouseDragged(event -> notifyObseurveur());

        typeOndeSlider.setOnKeyReleased(e ->notifyObseurveur());
        typeOndeSlider.setOnMouseClicked(e -> notifyObseurveur());
        typeOndeSlider.setOnMouseDragged(event -> notifyObseurveur());

        lfo.setOnMouseClicked(eh -> {
            obseuveurVCO.update(this);
            frequency.setText("Fréquence : " + getFrequencyToDisplay() + " Hz");
        });

        delete.setOnMouseClicked(eh -> Controller.getInstance().removeWithConfirmPopup(obseuveurVCO, border));
    }

    private OscillatorType getOscillatorType(double index){
        index = Math.round(index);
        if (index == 0.0){
            return OscillatorType.SQUARE;
        }
        if (index == 1.0){
            return OscillatorType.TRIANGLE;
        }
        if (index == 2.0){
            return OscillatorType.SAWTOOTH;
        }
        if (index == 3.0){
            return OscillatorType.SINE;
        }
        return null;
    }

    private Double setOscillatorType(OscillatorType type){
        if (type == OscillatorType.SQUARE){
            return 0.0;
        }
        if (type == OscillatorType.TRIANGLE){
            return 1.0;
        }
        if (type == OscillatorType.SAWTOOTH){
            return 2.0;
        }
        if (type == OscillatorType.SINE){
            return 3.0;
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
    public boolean isLFOActive() { return lfo.isSelected(); }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseuveurVCO = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(fm, o.getReference(), PortType.INPUTFM, border);
            cableManager.addListener(out, o.getReference(), PortType.OUTPUT, border);
            frequency.setText("Fréquence : " + getFrequencyToDisplay() + " Hz");
        }
    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null) obseuveurVCO=null;
    }

    @Override
    public void notifyObseurveur() {
        obseuveurVCO.update(this);
        frequency.setText("Fréquence :"+ getFrequencyToDisplay() +" Hz");
    }

    @Override
    public SavedModule createMemento() {
        return new SavedVCO(border.getLayoutX(), border.getLayoutY(),
                octaveSlider.getValue(), reglageFinSlider.getValue(),
                getOscillatorType(typeOndeSlider.getValue()), isLFOActive());
    }

    @Override
    public void loadProperties(SavedModule module) {
        SavedVCO savedVCO = (SavedVCO)module;
        octaveSlider.setValue(savedVCO.getOctave());
        reglageFinSlider.setValue(savedVCO.getReglageFin());
        Double sliderValue = setOscillatorType(savedVCO.getTypeOnde());
        typeOndeSlider.setValue(sliderValue);
        lfo.setSelected(savedVCO.isLFO());
        notifyObseurveur();
    }

    @Override
    public Circle getPort(PortType portType) {
        if (portType.equals(PortType.INPUTFM)) {
            return this.fm;
        }
        if (portType.equals(PortType.OUTPUT)) {
            return this.out;
        }
        return null;
    }

    private String getFrequencyToDisplay() {
        DecimalFormat f = new DecimalFormat("##.00");
        return f.format(((VCO)obseuveurVCO.getReference()).getFrequency());
    }
}
