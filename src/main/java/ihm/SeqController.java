package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectSeq;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.Sequenceur;
import sauvegarde.SavedModule;
import sauvegarde.SavedSequenceur;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;

public class SeqController implements Initializable, SubjectSeq, SuperController {

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
    @FXML
    Button remiseAUn;

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
            for (int i =1; i <= 8; i++){
                currentClicked=i;
                notifyObseurveur();
            }
        });
        remiseAUn.setOnMouseClicked((MouseEvent e) ->{currentClicked = 1;
            ((Sequenceur) obseurveurSequenceur.getReference()).resetToOne(this);
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

    @Override
    public SavedModule createMemento() {
        return new SavedSequenceur(pane.getLayoutX(), pane.getLayoutY(),
                sld1.getValue(),
                sld2.getValue(),
                sld3.getValue(),
                sld4.getValue(),
                sld5.getValue(),
                sld6.getValue(),
                sld7.getValue(),
                sld8.getValue());
    }

    @Override
    public void loadProperties(SavedModule module) {
        SavedSequenceur savedSequenceur = (SavedSequenceur) module;
        sld1.setValue(savedSequenceur.getSld1());
        sld2.setValue(savedSequenceur.getSld2());
        sld3.setValue(savedSequenceur.getSld3());
        sld4.setValue(savedSequenceur.getSld4());
        sld5.setValue(savedSequenceur.getSld5());
        sld6.setValue(savedSequenceur.getSld6());
        sld7.setValue(savedSequenceur.getSld7());
        sld8.setValue(savedSequenceur.getSld8());
        notifyObseurveur();
    }

    @Override
    public Circle getPort(PortType portType) {

        if (portType.equals(PortType.OUTPUT)) {
            return this.out;
        }
        if(portType.equals(PortType.INPUT)){
            return  this.in;
        }
        return null;
    }
}
