package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectVCF;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.VCF;
import sauvegarde.SavedModule;
import sauvegarde.SavedVCF;
import sauvegarde.SavedVCO;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class VCFLPController implements Initializable, SubjectVCF, SuperController {

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

    @FXML
    Label frequence;

    private int minValue = 0;
    private int maxValue = 20000;
    private static final int INITIAL_VALUE = 0;

    private Obseurveur<SubjectVCF> vcflpObseurveur;
    private boolean isLp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        f0.setOnKeyReleased(e ->notifyObseurveur());
        f0.setOnMouseClicked(e -> notifyObseurveur());
        f0.setOnMouseMoved(event -> notifyObseurveur());

        resonance.setOnKeyReleased(e ->notifyObseurveur());
        resonance.setOnMouseClicked(e -> notifyObseurveur());
        resonance.setOnMouseMoved(event -> notifyObseurveur());

        delete.setOnMouseClicked(eh -> {
            Controller.getInstance().removeWithConfirmPopup(vcflpObseurveur, pane);
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
            frequence.setText("fréquence : 0 Hz");

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
        frequence.setText("fréquence : "+ ((VCF)vcflpObseurveur.getReference()).getFrequency()+" Hz");


    }
    @Override
    public SavedModule createMemento() {
        return new SavedVCF(pane.getLayoutX(), pane.getLayoutY(),
                f0.getValue(), resonance.getValue(),
                isLp());
    }

    @Override
    public void loadProperties(SavedModule module) {
        SavedVCF savedVCF = (SavedVCF)module;
        f0.setValue(savedVCF.getF0());
        resonance.setValue(savedVCF.getResonance());
        isLp = savedVCF.isLP();
        notifyObseurveur();
    }

    @Override
    public Circle getPort(PortType portType) {
        if (portType.equals(PortType.FM)) {
            return this.fm;
        }
        if (portType.equals(PortType.OUTPUT)) {
            return this.out;
        }
        if(portType.equals(PortType.INPUT)){
            return  this.in;
        }
        return null;
    }


    public void setIsLp(boolean b){
        this.isLp = b;
    }
    public boolean isLp(){
        return isLp;
    }
}
