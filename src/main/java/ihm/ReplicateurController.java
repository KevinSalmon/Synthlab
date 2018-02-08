package ihm;

import controller.Controller;
import controller.Obseurveur;
import controller.SubjectReplicateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import utils.Cable;
import utils.FxmlFilesNames;
import utils.PortType;
import utils.CableManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
            List<Cable> cables = cableManager.getCables();

            List<Node> workspaceChildren = ((Pane)pane.getParent()).getChildren();

            for (int i=0; i < cables.size(); i++) {
                Cable cable = cables.get(i);
                if(cable.getModuleIn().equals(replicateurObseurveur) ||
                        cable.getModuleOut().equals(replicateurObseurveur)){

                    workspaceChildren.remove(cable.getLine());
                    cables.remove(i);
                    i--;
                }
            }

            Controller controller = Controller.getInstance();
            controller.getSynth().remove(replicateurObseurveur.getReference());

            workspaceChildren.remove(pane);
        });
    }
}
