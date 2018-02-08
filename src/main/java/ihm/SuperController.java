package ihm;

import controller.Controller;
import controller.Obseurveur;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import utils.Cable;
import utils.CableManager;

import java.util.List;

/**
 * Classe qui implemente les methodes partagees par les differents controllers
 */
public class SuperController {

    /**
     * Suppression du module avec popup de confirmation
     * @param observeur a supprimer
     * @param pane lie a l'observeur
     */
    public void removeWithConfirmPopup(Obseurveur observeur, Pane pane){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Voulez-vous vraiment supprimer ce module ?");
        confirm.setTitle("Suppression d'un module");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                remove(observeur, pane);
            }
        });
    }

    /**
     * Suppression du module sans popup de confirmation
     * @param observeur a supprimer
     * @param pane lie a l'observeur
     */
    private void remove(Obseurveur observeur, Pane pane) {
        List<Cable> cables = CableManager.getInstance().getCables();

        List<Node> workspaceChildren = ((Pane)pane.getParent()).getChildren();

        for (int i=0; i < cables.size(); i++) {
            Cable cable = cables.get(i);
            if(cable.getModuleIn().equals(observeur) ||
                    cable.getModuleOut().equals(observeur)){

                cable.disconnect();

                workspaceChildren.remove(cable.getLine());
                cables.remove(i);
                i--;
            }
        }

        Controller controller = Controller.getInstance();
        controller.getSynth().remove(observeur.getReference());

        workspaceChildren.remove(pane);
    }
}
