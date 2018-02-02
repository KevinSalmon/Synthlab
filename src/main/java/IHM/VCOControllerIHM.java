package IHM;

import controller.Obseurveur;
import controller.SubjectOutput;
import controller.SubjectVCO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VCOControllerIHM implements Initializable, SubjectVCO {


    @FXML
    private Slider slider_octave;

    @FXML
    private Slider slider_reglageFin;

    @FXML
    private Slider slider_f0;

    @FXML
    private Slider slider_type;

    private List<Obseurveur<SubjectVCO>> obseuveurVCOputList;

    public VCOControllerIHM(){
        obseuveurVCOputList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider_octave.setOnInputMethodTextChanged(event -> notifyObseurveur());
        slider_reglageFin.setOnInputMethodTextChanged(event -> notifyObseurveur());
    }

    @Override
    public int getOctaveValue() {
        return new Double(slider_octave.getValue()).intValue();
    }

    @Override
    public double getReglageFinValue() {
        return slider_reglageFin.getValue();
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
