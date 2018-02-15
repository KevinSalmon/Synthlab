package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectVCF;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.VCF;
import sauvegarde.SavedModule;
import sauvegarde.SavedVCF;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;

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

    private Obseurveur<SubjectVCF> vcflpObseurveur;
    private boolean isLp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        f0.setOnKeyReleased(e ->notifyObseurveur());
        f0.setOnMouseClicked(e -> notifyObseurveur());
//        f0.setOnMouseMoved(event -> notifyObseurveur());
        f0.setOnMouseDragged(e -> notifyObseurveur());

        resonance.setOnKeyReleased(e ->notifyObseurveur());
        resonance.setOnMouseClicked(e -> notifyObseurveur());
//        resonance.setOnMouseMoved(event -> notifyObseurveur());
        resonance.setOnMouseDragged(e -> notifyObseurveur());

        delete.setOnMouseClicked(e -> Controller.getInstance().removeWithConfirmPopup(vcflpObseurveur, pane));
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
    public void receiveFrequency(double frequency) {
        frequence.setText("fréquence : "+ frequency+" Hz");
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            vcflpObseurveur = o;
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in, vcflpObseurveur.getReference(), PortType.INPUT, pane);
            cableManager.addListener(out, vcflpObseurveur.getReference(), PortType.OUTPUT, pane);
            cableManager.addListener(fm, vcflpObseurveur.getReference(), PortType.INPUTFM, pane);
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
    }

    @Override
    public SavedModule createMemento() {
        return new SavedVCF(pane.getLayoutX(), pane.getLayoutY(), resonance.getValue(), f0.getValue(),  isLp());
    }

    @Override
    public void loadProperties(SavedModule module) {
        SavedVCF savedVCF = (SavedVCF)module;
        f0.setValue(savedVCF.getF0());
        resonance.setValue(savedVCF.getResonance());
        isLp = savedVCF.getLP();
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
