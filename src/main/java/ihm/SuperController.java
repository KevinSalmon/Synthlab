package ihm;

import com.jsyn.JSyn;
import controller.Controller;
import controller.Obseurveur;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import utils.Cable;
import utils.CableManager;

import java.util.List;

public class SuperController {

    public void removeComponent(Obseurveur observeur, Pane pane){
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
