package controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import ihm.IHMController;
import com.jsyn.Synthesizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import module.*;
import utils.Cable;
import utils.CableManager;
import utils.FxmlFilesNames;
import utils.SkinNames;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

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

    /**
     *
     */
    protected Controller(){
        synth = createSynthesizer();
        synth.start();
    }

    /**
     *
     * @param ihmController principal de l'appli
     */
    public Controller(IHMController ihmController){
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
                VCF vcflp = new VCF(true);
                ((Subject)fxmlLoader.getController()).register(vcflp);
                synth.add(vcflp);
                break;
            case FxmlFilesNames.VCFHP:
                VCF vcfhp = new VCF(false);
                ((Subject)fxmlLoader.getController()).register(vcfhp);
                synth.add(vcfhp);
                break;
            case FxmlFilesNames.OSCILLOSCOPE:
                Oscilloscope osc = new Oscilloscope();
                ((Subject)fxmlLoader.getController()).register(osc);
                synth.add(osc);
                break;
            case FxmlFilesNames.EG:
                EG eg = new EG();
                ((Subject) fxmlLoader.getController()).register(eg);
                synth.add(eg);
                break;
            case FxmlFilesNames.BRUITBLANC:
                BruitBlancModule bruitBlancModule = new BruitBlancModule();
                ((Subject) fxmlLoader.getController()).register(bruitBlancModule);
                synth.add(bruitBlancModule);
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
     * @param workspace
     */
    public void saveWorkspace(Pane workspace){
        CableManager cableManager = CableManager.getInstance();

        Collection<Module> modulesToSave = new HashSet<>();

        for (Cable cable : cableManager.getCables() ) {

            modulesToSave.add(cable.getModuleIn());
            modulesToSave.add(cable.getModuleOut());
        }


        //TODO : Gerer l'enregistrement dans un fichier
        /*ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File("result.json"), modulesToSave);
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }*/
    }

    /**
     * Chargement du workspace
     */
    public void loadWorkspace(){

    }
}
