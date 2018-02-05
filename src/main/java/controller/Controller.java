package controller;

import IHM.IHMController;
import com.jsyn.Synthesizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import module.Module;
import module.OutputModule;
import module.VCO;
import utils.FxmlFilesNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.jsyn.JSyn.createSynthesizer;

/**
 * Controlleur principal de l'application
 */
public class Controller {

    List<Module> modules;
    /**
     * controlleur de l'ihm principale
     */
    IHMController ihmController;
    /**
     * Synthétiseur de l'appli
     */
    Synthesizer synth;

    private static final Logger Log = Logger.getLogger( Controller.class.getName() );

    /**
     *
     * @param ihmController principal de l'appli
     */
    public Controller(IHMController ihmController){
        synth = createSynthesizer();
        modules = new ArrayList<>();
        this.ihmController = ihmController;

        ihmController.setController(this);
        ihmController.init();
    }

    public void addModule(Module module){
        modules.add(module);
    }

    /**
     * Créer un module en le chargeant grâce au nom de son fxml
     * @param fxmlModuleFileName nom du fichier fxml à charger
     * @return Pane graphique du module
     */
    private Pane createModuleWithoutEvent(String fxmlModuleFileName){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlModuleFileName));
        Pane modulePane = null;
        try {
            modulePane = fxmlLoader.load();
        } catch (IOException e) {
            Log.severe(e.toString());
        }

        switch (fxmlModuleFileName){
            case FxmlFilesNames.MODULE_OUT:
                OutputModule outputModule = new OutputModule(synth);
                ((Subject)fxmlLoader.getController()).register(outputModule);
                break;
            case FxmlFilesNames.VCA:
//                VCA vca = new VCA();
                //((Subject)fxmlLoader.getController()).register(vca);
                break;
            case FxmlFilesNames.VCO:
                VCO vco = new VCO();
                ((Subject)fxmlLoader.getController()).register(vco);
                break;
            default:
                break;
        }

        return modulePane;
    }

    /**
     * Création d'un module en chargeant le fxml associé
     * @param fxmlModuleFileName nom du fxml du module à ajouter
     * @return module
     */
    public Pane createModule(String fxmlModuleFileName){
        Pane m = createModuleWithoutEvent(fxmlModuleFileName);
        m.setOnDragDetected(de -> ihmController.onDragDetected(de, m));
        m.setOnDragDone(de -> ihmController.onDragDone(de, m));
        return m;
    }
}
