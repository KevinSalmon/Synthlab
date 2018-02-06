package ihm;

import controller.Obseurveur;
import controller.SubjectVCO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.PortType;
import utils.CableManager;
import utils.OscillatorType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    private Circle in;

    @FXML
    private Circle out;

    private List<Obseurveur<SubjectVCO>> obseuveurVCOputList;

    public VCOControllerIHM(){
        obseuveurVCOputList = new ArrayList<>();
    }

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
    public void register(Obseurveur o) {
        if(o != null){
            obseuveurVCOputList.add(o);
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in, o.getReference(), PortType.INPUT, border);
            cableManager.addListener(out, o.getReference(), PortType.OUTPUT, border);
        }
    }

    @Override
    public void remove(Obseurveur o) {
        obseuveurVCOputList.remove(o);
    }

    @Override
    public void notifyObseurveur() {
        for(Obseurveur<SubjectVCO> o : obseuveurVCOputList){
            o.update(this);
        }
    }
}
