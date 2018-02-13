package ihm;

import javafx.scene.shape.Circle;
import sauvegarde.SavedModule;
import utils.PortType;

public interface SuperController {

    SavedModule createMemento();
    void loadProperties(SavedModule module);
    public Circle getPort(PortType portType);
}
