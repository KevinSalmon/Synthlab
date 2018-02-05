package IHM;

import Exceptions.OutputException;
import controller.Obseurveur;
import controller.SubjectVCO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import utils.CableManager;
import utils.OscillatorType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VCOControllerIHM implements Initializable, SubjectVCO {


    @FXML
    private Slider octaveSlider;

    @FXML
    private Slider reglageFinSlider;

    @FXML
    private Slider f0Slider;

    @FXML
    private Slider typeOndeSlider;

    @FXML
    private Circle in;

    @FXML
    private Circle out;

    private List<Obseurveur<SubjectVCO>> obseuveurVCOputList;
    private Line line;

    public VCOControllerIHM(){
        obseuveurVCOputList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        octaveSlider.setOnInputMethodTextChanged(event -> notifyObseurveur());
        octaveSlider.setOnKeyReleased(e ->notifyObseurveur());
        octaveSlider.setOnMouseClicked(e -> notifyObseurveur());

        reglageFinSlider.setOnInputMethodTextChanged(event -> notifyObseurveur());
        CableManager cableManager = CableManager.getInstance();

        DoubleProperty xValue = new SimpleDoubleProperty();
        xValue.bind(in.getParent().layoutXProperty());
        xValue.addListener((observable, oldValue, newValue) -> cableManager.updateInputX(in));

        DoubleProperty yValue = new SimpleDoubleProperty();
        yValue.bind(in.getParent().layoutYProperty());
        yValue.addListener((observable, oldValue, newValue) -> cableManager.updateInputY(in));
        in.setOnMouseClicked(event -> {
            try {
                line = cableManager.setInput(in);
                line.toFront();
            } catch (OutputException e) {
                e.printStackTrace();
            }
        });

        DoubleProperty xValueOut = new SimpleDoubleProperty();
        xValueOut.bind(out.getParent().layoutXProperty());
        xValueOut.addListener((observable, oldValue, newValue) ->{
            cableManager.updateOutputX(out);
            //line.toFront();

                }

        );
        DoubleProperty yValueOut = new SimpleDoubleProperty();
        yValueOut.bind(out.getParent().layoutYProperty());
        yValueOut.addListener((observable, oldValue, newValue) ->{
            cableManager.updateOutputY(out);
            //line.toFront();

                }

        );
        out.setOnMouseClicked(event -> cableManager.setOutput(out));
        reglageFinSlider.setOnKeyReleased(e ->notifyObseurveur());
        reglageFinSlider.setOnMouseClicked(e -> notifyObseurveur());

        f0Slider.setOnInputMethodTextChanged(event -> notifyObseurveur());
        f0Slider.setOnKeyReleased(e ->notifyObseurveur());
        f0Slider.setOnMouseClicked(e -> notifyObseurveur());

        typeOndeSlider.setOnInputMethodTextChanged(event -> notifyObseurveur());
        typeOndeSlider.setOnKeyReleased(e ->notifyObseurveur());
        typeOndeSlider.setOnMouseClicked(e -> notifyObseurveur());
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
    public double getFrequency() {
        return f0Slider.getValue();
    }

    @Override
    public OscillatorType getOscillatorType() {
       return getOscillatorType(typeOndeSlider.getValue());
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseuveurVCOputList.add(o);
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
