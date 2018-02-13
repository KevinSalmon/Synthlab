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
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;

public class VCFLPController implements Initializable, SubjectVCF {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        f0.setOnKeyReleased(e ->notifyObseurveur());
        f0.setOnMouseClicked(e -> notifyObseurveur());
        f0.setOnMouseMoved(event -> notifyObseurveur());
        f0.setOnMouseDragged(e -> notifyObseurveur());

        resonance.setOnKeyReleased(e ->notifyObseurveur());
        resonance.setOnMouseClicked(e -> notifyObseurveur());
        resonance.setOnMouseMoved(event -> notifyObseurveur());
        resonance.setOnMouseDragged(e -> notifyObseurveur());

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
}
