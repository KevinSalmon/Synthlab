package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectSeq;
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
    @FXML
    Button delete;
    @FXML
    Button raz;

    private Obseurveur<SubjectSeq> obseurveurSequenceur;
    private int currentClicked =0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sld1.setOnMouseClicked(e -> {currentClicked = 1;notifyObseurveur();});
        sld1.setOnMouseReleased(e -> {currentClicked = 1;notifyObseurveur();});

        sld2.setOnMouseClicked(e -> {currentClicked = 2;notifyObseurveur();});
        sld2.setOnMouseReleased(e -> {currentClicked = 2;notifyObseurveur();});

        sld3.setOnMouseClicked(e -> {currentClicked = 3;notifyObseurveur();});
        sld3.setOnMouseReleased(e -> {currentClicked = 3;notifyObseurveur();});

        sld4.setOnMouseClicked(e -> {currentClicked = 4;notifyObseurveur();});
        sld4.setOnMouseReleased(e -> {currentClicked = 4;notifyObseurveur();});

        sld5.setOnMouseClicked(e -> {currentClicked = 5;notifyObseurveur();});

        sld5.setOnMouseReleased(e -> {currentClicked = 5;notifyObseurveur();});

        sld6.setOnMouseClicked(e -> { currentClicked = 6; notifyObseurveur();});
        sld6.setOnMouseReleased(e -> { currentClicked = 6;notifyObseurveur();});

        sld7.setOnMouseClicked(e -> {currentClicked = 7;notifyObseurveur();});
        sld7.setOnMouseReleased(e -> {currentClicked = 7;notifyObseurveur();});

        sld8.setOnMouseClicked(e -> {currentClicked = 8;notifyObseurveur();});
        sld8.setOnMouseReleased(e -> {currentClicked = 8;notifyObseurveur();});

        raz.setOnMouseClicked(e ->{
            sld1.setValue(0.0);
            sld2.setValue(0.0);
            sld3.setValue(0.0);
            sld4.setValue(0.0);
            sld5.setValue(0.0);
            sld6.setValue(0.0);
            sld7.setValue(0.0);
            sld8.setValue(0.0);
        });


    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseurveurSequenceur = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in, obseurveurSequenceur.getReference(), PortType.INPUT, pane);
            cableManager.addListener(out, obseurveurSequenceur.getReference(), PortType.OUTPUT, pane);
        }
        delete.setOnMouseClicked(eh -> {
            Controller.getInstance().removeWithConfirmPopup(obseurveurSequenceur, pane);
        });

    }

    @Override
    public void remove(Obseurveur o) {
        if(o.equals(obseurveurSequenceur)){
            obseurveurSequenceur = null;
        }

    }

    @Override
    public void notifyObseurveur() {
        obseurveurSequenceur.update(this);
    }

    @Override
    public int getCurrentSlider() {
        return currentClicked;
    }

    @Override
    public double getSliderValue(int i) {
        switch (i){
            case 1: return sld1.getValue();
            case 2: return sld2.getValue();
            case 3: return sld3.getValue();
            case 4: return sld4.getValue();
            case 5: return sld5.getValue();
            case 6: return sld6.getValue();
            case 7: return sld7.getValue();
            case 8: return sld8.getValue();
            default:return 0.0;


        }
    }
}
