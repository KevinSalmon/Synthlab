package IHM;

import controller.Obseurveur;
import controller.SubjectReplicateur;
import controller.SubjectVCA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.PortType;
import utils.CableManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReplicateurController implements Initializable, SubjectReplicateur{

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

    private List<Obseurveur<SubjectReplicateur>> replicateurObseurveurList;

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            replicateurObseurveurList.add(o);
            CableManager cableManager = CableManager.getInstance();
            cableManager.addListener(in, o.getReference(), PortType.INPUT, pane);
            cableManager.addListener(out1, o.getReference(), PortType.OUTPUT1, pane);
            cableManager.addListener(out2, o.getReference(), PortType.OUTPUT2, pane);
            cableManager.addListener(out3, o.getReference(), PortType.OUTPUT3, pane);
        }
    }

    @Override
    public void remove(Obseurveur o) {
        replicateurObseurveurList.remove(o);
    }

    @Override
    public void notifyObseurveur() {
        for (Obseurveur<SubjectReplicateur> obseurveur: replicateurObseurveurList) {
            obseurveur.update(this);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        replicateurObseurveurList = new ArrayList<>();
    }
}
