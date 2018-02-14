package sauvegarde;

import java.util.ArrayList;
import java.util.List;

public class SavedFile {

    private List<SavedModule> savedModules;
    private List<SavedCable> savedCables;

    public SavedFile() {
        this.savedModules = new ArrayList<>();
        this.savedCables = new ArrayList<>();
    }

    public SavedFile(List<SavedModule> savedModules, List<SavedCable> savedCables) {
        this.savedModules = savedModules;
        this.savedCables = savedCables;
    }

    public List<SavedModule> getSavedModules() {
        return savedModules;
    }

    public void setSavedModules(List<SavedModule> savedModules) {
        this.savedModules = savedModules;
    }

    public List<SavedCable> getSavedCables() {
        return savedCables;
    }

    public void setSavedCables(List<SavedCable> savedCables) {
        this.savedCables = savedCables;
    }
}
