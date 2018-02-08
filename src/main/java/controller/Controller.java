package controller;

import ihm.IHMController;
import com.jsyn.Synthesizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import module.*;
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

    private static volatile Controller instance;

    private static final Logger Log = Logger.getLogger( Controller.class.getName() );

    /**
     *
     */
    protected Controller(){
        synth = createSynthesizer();
        modules = new ArrayList<>();
        synth.start();
    }

    /**
     *
     * @param ihmController principal de l'appli
     */
    protected Controller(IHMController ihmController){
        synth = createSynthesizer();
        modules = new ArrayList<>();
        this.ihmController = ihmController;

        ihmController.setController(this);
        ihmController.init();
        synth.start();
    }

    public static Controller getInstance(){
        synchronized (Controller.class){
            if(instance == null) instance = new Controller();
            return instance;
        }
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
                VCA vca = new VCA();
                ((Subject)fxmlLoader.getController()).register(vca);
                synth.add(vca);
                break;
            case FxmlFilesNames.VCO:
                VCO vco = new VCO();
                ((Subject)fxmlLoader.getController()).register(vco);
                synth.add(vco);
                break;
            case FxmlFilesNames.REP:
                Replicateur rep = new Replicateur();
                ((Subject)fxmlLoader.getController()).register(rep);
                synth.add(rep);
                break;

            case FxmlFilesNames.VCFLP:
                VCFLP vcflp = new VCFLP();
                ((Subject) fxmlLoader.getController()).register(vcflp);
                synth.add(vcflp);
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

    public Pane createMiniature(String fxmlMiniatureFileName){
        return loadFxml(fxmlMiniatureFileName);
    }

    private Pane loadFxml(String fileName){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fileName));
        Pane pane = null;
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            Log.severe(e.toString());
        }
        return pane;
    }

    public void close() {
        synth.stop();
    }

    public Synthesizer getSynth() {
        return synth;
    }

    public IHMController getIhmController() {
        return ihmController;
    }

    public void setIhmController(IHMController ihmController) {
        if(ihmController != null){
            this.ihmController = ihmController;
            this.ihmController.setController(this);
            this.ihmController.init();
        }
    }
}
