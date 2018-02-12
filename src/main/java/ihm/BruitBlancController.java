package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectBruitBlanc;
import controller.SubjectReplicateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;

public class BruitBlancController implements Initializable, SubjectBruitBlanc{

    @FXML
    private Pane pane;

    @FXML
    private Circle out;

    @FXML
    private Button delete;

    private Obseurveur<SubjectBruitBlanc> bruitBlancObseurveur;
    private CableManager cableManager;

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            bruitBlancObseurveur = o;
            cableManager = CableManager.getInstance();
            cableManager.addListener(out, o.getReference(), PortType.OUTPUT, pane);
        }
    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null)
            bruitBlancObseurveur = null;
    }

    @Override
    public void notifyObseurveur() { bruitBlancObseurveur.update(this); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delete.setOnMouseClicked(eh -> {
            Controller.getInstance().removeWithConfirmPopup(bruitBlancObseurveur, pane);
        });
    }
}
