package controller;

import IHM.IHMController;
import IHM.VCOControllerIHM;
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

import static com.jsyn.JSyn.createSynthesizer;

public class Controller {
    List<Module> modules;
    IHMController ihmController;
    Synthesizer synth;

    public Controller(IHMController ihmController){
        synth = createSynthesizer();
        modules = new ArrayList<>();
        //synth.start();
        this.ihmController = ihmController;

        ihmController.setController(this);
        ihmController.init();
    }

    public void addModule(Module module){
        modules.add(module);
    }


    private Pane createModuleWithoutEvent(String fxmlModuleFileName){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlModuleFileName));
        Pane modulePane = null;
        try {
            modulePane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
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
