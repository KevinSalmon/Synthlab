package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ihm.*;
import com.jsyn.Synthesizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.*;
import sauvegarde.*;
import utils.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.jsyn.JSyn.createSynthesizer;

/**
 * Controlleur principal de l'application
 */
public class Controller {
    /**
     * controlleur de l'ihm principale
     */
    IHMController ihmController;
    /**
     * Synthétiseur de l'appli
     */
    Synthesizer synth;

    /**
     * Instance unique du Controller
     */
    private static volatile Controller instance;

    /**
     * Logger
     */
    private static final Logger Log = Logger.getLogger( Controller.class.getName() );

    /**
     * Scene de l'application
     */
    private Scene scene;

    private Map<Module, Tuple<String, SuperController>> toSave;

    private Module loadedModule;

    private SuperController tmpController;

    /**
     *
     */
    protected Controller(){
        toSave  = new HashMap<>();

        synth = createSynthesizer();
        synth.start();
    }

    /**
     *
     * @param ihmController principal de l'appli
     */
    public Controller(IHMController ihmController){
        toSave  = new HashMap<>();

        synth = createSynthesizer();
        this.ihmController = ihmController;

        ihmController.setController(this);
        ihmController.init(true);
        synth.start();
    }

    /**
     *
     * @return L'instance du singleton Controller
     */
    public static Controller getInstance(){
        synchronized (Controller.class){
            if(instance == null) instance = new Controller();
            return instance;
        }
    }

    /**
     * Créer un module en le chargeant grâce au nom de son fxml
     * @param fxmlModuleFileName nom du fichier fxml à charger
     * @return Pane graphique du module
     */
    private Pane createModuleWithoutEvent(String fxmlModuleFileName, SavedModule... savedModules){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlModuleFileName));
        Pane modulePane = null;
        try {
            modulePane = fxmlLoader.load();
        } catch (IOException e) {
            Log.severe(e.toString());
        }

        SuperController moduleController;
        Tuple<String, SuperController> save;

        switch (fxmlModuleFileName){
            case FxmlFilesNames.MODULE_OUT:
                OutputModule outputModule = new OutputModule(synth);
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(outputModule, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                break;
            case FxmlFilesNames.KEYBOARD:
                Keyboard keyboard = new Keyboard();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(keyboard, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                scene.setOnKeyReleased(keyboard);
                scene.setOnKeyPressed(keyboard);
                break;
            case FxmlFilesNames.VCA:
                VCA vca = new VCA();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(vca, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(vca);
                break;
            case FxmlFilesNames.VCO:
                VCO vco = new VCO();
                loadedModule = vco;

                moduleController = fxmlLoader.getController();
                saveModuleForLoading(vco, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(vco);
                break;
            case FxmlFilesNames.REP:
                Replicateur rep = new Replicateur();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(rep, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(rep);
                break;

            case FxmlFilesNames.VCFLP:
                VCF vcflp = new VCF(true);
                moduleController = fxmlLoader.getController();
                ((VCFLPController)moduleController).setIsLp(true);
                saveModuleForLoading(vcflp, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(vcflp);
                break;
            case FxmlFilesNames.VCFHP:
                VCF vcfhp = new VCF(false);
                moduleController = fxmlLoader.getController();
                ((VCFLPController)moduleController).setIsLp(false);
                saveModuleForLoading(vcfhp, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(vcfhp);
                break;
            case FxmlFilesNames.OSCILLOSCOPE:
                Oscilloscope osc = new Oscilloscope();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(osc, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(osc);
                break;
            case FxmlFilesNames.EG:
                EG eg = new EG();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(eg, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(eg);
                break;
            case FxmlFilesNames.BRUITBLANC:
                BruitBlancModule bruitBlancModule = new BruitBlancModule();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(bruitBlancModule, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(bruitBlancModule);
                break;
            case FxmlFilesNames.SEQ:
                Sequenceur sequenceur = new Sequenceur();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(sequenceur, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(sequenceur);
                break;
            case FxmlFilesNames.MIX:
                Mixer mixer = new Mixer();
                moduleController = fxmlLoader.getController();
                saveModuleForLoading(mixer, moduleController, fxmlLoader, fxmlModuleFileName, savedModules);
                synth.add(mixer);
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

    /**
     * Charge le Fxml d'une miniature de module
     * @param fxmlMiniatureFileName nom du fichier fxml
     * @return le Pane de la miniature
     */
    public Pane createMiniature(String fxmlMiniatureFileName){
        return loadFxml(fxmlMiniatureFileName);
    }

    /**
     * Charge un Fxml et retourne le Pane instancié
     * @param fileName nom du Fxml à instancier
     * @return Pane du Fxml chargé
     */
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

    /**
     * Appelé quand on quitte l'application
     */
    public void close() {
        synth.stop();
    }

    /**
     * Getter du Synthesizer de l'application
     * @return Synthesizer
     */
    public Synthesizer getSynth() {
        return synth;
    }

    /**
     * Getter de IHMController
     * @return IHMController
     */
    public IHMController getIhmController() {
        return ihmController;
    }

    /**
     * Setter de l'IHMController
     * @param ihmController nouvel IHMController
     */
    public void setIhmController(IHMController ihmController) {
        if(ihmController != null){
            this.ihmController = ihmController;
            this.ihmController.setController(this);
            this.ihmController.init(true);
        }
    }

    /**
     * Setter de la scene root de l'application
     * @param scene de l'application
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        scene.getRoot().setId("root_id");
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> ihmController.onRezize(newSceneWidth, null));
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> ihmController.onRezize(null, newSceneHeight));
        setSkin(SkinNames.SKIN_MOCHE_NAME);
    }

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

                workspaceChildren.remove(cable.getCurve());
                cables.remove(i);
                i--;
            }
        }

        //TODO modules.remove(observeur.getReference());

        Controller controller = Controller.getInstance();
        controller.getSynth().remove(observeur.getReference());

        workspaceChildren.remove(pane);
    }

    /**
     * Setter du skin de l'application
     * @param skinName nom du skin
     */
    public void setSkin(String skinName){
        String stylePath = "";
        switch (skinName){
            case SkinNames.SKIN_MOCHE_NAME:
                stylePath = SkinNames.SKIN_MOCHE_FILE;
                break;
            case SkinNames.SKIN_METAL:
                stylePath = SkinNames.SKIN_METAL_FILE;
                break;
            default:
                break;
        }
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(this.getClass().getResource(stylePath).toExternalForm());
    }

    /**
     * Sauvegarde du workspace
     */
    public void saveWorkspace(){
        CableManager cableManager = CableManager.getInstance();

        List<SavedCable> cablesToSave = new ArrayList<>();

        int idModule = 0;


        Map<Module, SavedModule> saved = new HashMap<>();
        SavedModule moduleIn;
        SavedModule moduleOut;

        for (Cable cable : cableManager.getCables()){
            if(saved.containsKey(cable.getModuleIn())){
                moduleIn = saved.get(cable.getModuleIn());
            }else{
                Tuple<String, SuperController> tuple = toSave.get(cable.getModuleIn());
                moduleIn = tuple.getRight().createMemento();
                moduleIn.setModuleFXMLFile(tuple.getLeft());
                moduleIn.setIdModule(idModule);
                idModule++;
                saved.put(cable.getModuleIn(), moduleIn);
            }

            if(saved.containsKey(cable.getModuleOut())){
                moduleOut = saved.get(cable.getModuleOut());
            }else{
                Tuple<String, SuperController> tuple = toSave.get(cable.getModuleOut());
                moduleOut = tuple.getRight().createMemento();
                moduleOut.setModuleFXMLFile(tuple.getLeft());
                moduleOut.setIdModule(idModule);
                idModule++;
                saved.put(cable.getModuleOut(), moduleOut);
            }

            SavedCable cableToSave = new SavedCable(moduleIn.getIdModule(),
                    moduleOut.getIdModule(), cable.getInputName(), cable.getOutputName());

            cablesToSave.add(cableToSave);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        File file = new File("save.json");

        SavedFile savedFile = new SavedFile();
        savedFile.setSavedCables(cablesToSave);
        savedFile.setSavedModules(saved.values().stream().collect(Collectors.toList()));
        try {
            mapper.writeValue(file, savedFile);
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    /**
     * Chargement du workspace
     */
    public void loadWorkspace(){


        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        File file = new File("save.json");
        SavedFile savedFile = new SavedFile();
        Map<Module, Map<PortType, Circle>>  modulesMap = new HashMap<>();
        Map<PortType, Circle> ports;
        Map<Module, Integer> moduleIntegerMap = new HashMap<>();

        try {
            savedFile = mapper.readValue(file, SavedFile.class);
        } catch (IOException e) {
            Logger.getGlobal().severe("Cannot load file : "+ e.getMessage());
        }

        for (SavedModule module : savedFile.getSavedModules()){

            Pane newPane = createModuleWithoutEvent(module.getModuleFXMLFile(), module);
            ihmController.addModuleToWorkspace(newPane, module.getxPos(), module.getyPos());
            ports = new HashMap<>();
            for (PortType port: loadedModule.getAllPorts()) {
                ports.put(port, tmpController.getPort(port));
                Logger.getGlobal().info("loaded module"+ String.valueOf(loadedModule)+" port "+port.getType()+ " tmpController "+tmpController.toString()+" port retrieved "+tmpController.getPort(port));

            }
            modulesMap.put(loadedModule, ports);
            moduleIntegerMap.put(loadedModule, module.getIdModule());
        }

        //(Circle point2D, Module moduleIn, String name) {
        for (SavedCable cable : savedFile.getSavedCables()){
            Tuple<Module, Map<PortType, Circle>> m2 = getModuleById(cable.getIdModuleIn(), modulesMap, moduleIntegerMap);
            Tuple<Module, Map<PortType, Circle>> m1 = getModuleById(cable.getIdModuleOut(), modulesMap, moduleIntegerMap);

            CableManager cableManager = CableManager.getInstance();
            cableManager.setOutput(m1.getRight().get(PortType.valueOf(cable.getOutputName().toUpperCase())) ,m1.getLeft(), cable.getOutputName());
            cableManager.setInput(m2.getRight().get(PortType.valueOf(cable.getInputName().toUpperCase())), m2.getLeft(), cable.getInputName());

            getIhmController().workspace.getChildren().add(cableManager.getCurve());
        }
        Logger.getGlobal().info(String.valueOf(getIhmController().workspace.getChildren()));
    }

    private Tuple<Module,Map<PortType,Circle>> getModuleById(int idModuleIn, Map<Module, Map<PortType, Circle>> modulesMap, Map<Module, Integer> moduleIntegerMap) {
        Logger.getGlobal().info(modulesMap.values().toString());
        for (Module module : moduleIntegerMap.keySet()){
            if(moduleIntegerMap.get(module).intValue() == idModuleIn){
                Tuple<Module, Map<PortType, Circle>> tuple = new Tuple(module, modulesMap.get(module));
                return tuple;
            }

        }
        return null;
    }


    private void saveModuleForLoading(Module module, SuperController moduleController, FXMLLoader fxmlLoader, String fxmlModuleFileName, SavedModule... savedModules){
        loadedModule = module;
        tmpController = moduleController;
        moduleController = fxmlLoader.getController();
        Tuple<String, SuperController> save = new Tuple<>(fxmlModuleFileName, moduleController);
        toSave.put(module, save);

        ((Subject)fxmlLoader.getController()).register((Obseurveur) module);

        if(savedModules != null && savedModules.length > 0){
            SavedModule savedModuleOut = savedModules[0];
            moduleController.loadProperties(savedModuleOut);
        }
    }
}
