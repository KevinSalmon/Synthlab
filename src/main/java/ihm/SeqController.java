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

    private Obseurveur<SubjectSeq> obseurveurSequenceur;
    private int currentClicked;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sld1.setOnMouseMoved(e -> {currentClicked = 1;notifyObseurveur();});
        sld1.setOnMouseClicked(e -> {currentClicked = 1;notifyObseurveur();});
        sld1.setOnKeyReleased(e -> {currentClicked = 1;notifyObseurveur();});

        sld2.setOnMouseMoved(e -> {currentClicked = 2;notifyObseurveur();});
        sld2.setOnMouseClicked(e -> {currentClicked = 2;notifyObseurveur();});
        sld2.setOnKeyReleased(e -> {currentClicked = 2;notifyObseurveur();});

        sld3.setOnMouseMoved(e -> {currentClicked = 3;notifyObseurveur();});
        sld3.setOnMouseClicked(e -> {currentClicked = 3;notifyObseurveur();});
        sld3.setOnKeyReleased(e -> {currentClicked = 3;notifyObseurveur();});

        sld4.setOnMouseMoved(e -> {currentClicked = 4;notifyObseurveur();});
        sld4.setOnMouseClicked(e -> {currentClicked = 4;notifyObseurveur();});
        sld4.setOnKeyReleased(e -> {currentClicked = 4;notifyObseurveur();});

        sld5.setOnMouseMoved(e -> {currentClicked = 5;notifyObseurveur();});
        sld5.setOnMouseClicked(e -> {currentClicked = 5;notifyObseurveur();});

        sld5.setOnKeyReleased(e -> {currentClicked = 5;notifyObseurveur();});

        sld6.setOnMouseMoved(e -> {currentClicked = 6;notifyObseurveur();});
        sld6.setOnMouseClicked(e -> { currentClicked = 6; notifyObseurveur();});
        sld6.setOnKeyReleased(e -> { currentClicked = 6;notifyObseurveur();});

        sld7.setOnMouseMoved(e -> {currentClicked = 7;notifyObseurveur();});
        sld7.setOnMouseClicked(e -> {currentClicked = 7;notifyObseurveur();});
        sld7.setOnKeyReleased(e -> {currentClicked = 7;notifyObseurveur();});

        sld8.setOnMouseMoved(e -> {currentClicked = 8;notifyObseurveur();});
        sld8.setOnMouseClicked(e -> {currentClicked = 8;notifyObseurveur();});
        sld8.setOnKeyReleased(e -> {currentClicked = 8;notifyObseurveur();});


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
}
