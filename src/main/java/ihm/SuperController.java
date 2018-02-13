package ihm;

import sauvegarde.SavedModule;

public interface SuperController {

    SavedModule createMemento();
    void loadProperties(SavedModule module);
}
