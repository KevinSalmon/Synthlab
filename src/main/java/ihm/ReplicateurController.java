package ihm;

import controller.Obseurveur;
import controller.SubjectReplicateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import utils.PortType;
import utils.CableManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ReplicateurController extends SuperController implements Initializable, SubjectReplicateur{

    @FXML
    private Pane pane;

    @FXML
    private Circle in;

    @FXML
    private Circle out1;

    @FXML
    private Circle out2;

    @FXML
    private Circle out3;

    @FXML
    private Button delete;

    private Obseurveur<SubjectReplicateur> replicateurObseurveur;
    private CableManager cableManager;

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            replicateurObseurveur = o;
            cableManager = CableManager.getInstance();
            cableManager.addListener(in, o.getReference(), PortType.INPUT, pane);
            cableManager.addListener(out1, o.getReference(), PortType.OUTPUT1, pane);
            cableManager.addListener(out2, o.getReference(), PortType.OUTPUT2, pane);
            cableManager.addListener(out3, o.getReference(), PortType.OUTPUT3, pane);
        }
    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null)
            replicateurObseurveur = null;
    }

    @Override
    public void notifyObseurveur() {
        replicateurObseurveur.update(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delete.setOnMouseClicked(eh -> {
            removeWithConfirmPopup(replicateurObseurveur, pane);
        });
    }
}
