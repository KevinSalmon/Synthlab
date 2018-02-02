package IHM;

import controller.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import module.OutputModule;
import utils.FxmlFilesNames;

import java.net.URL;
import java.util.ResourceBundle;

public class IHMController implements Initializable{

    /**
     * Variables FXML
     */
    @FXML
    private Pane workspace;

    /*

     */
    @FXML
    private Pane hoverPanel;

    @FXML
    private Pane moduleMenu;

    /**
     * Constantes locales
     */
    private final int PADDING = 30;

    /**
     * Variables locales
     */
    private Pane draggedModule;
    private boolean spawning = false;
    private double deltaX = 0;
    private double deltaY = PADDING;

    /**
     * Valeur de l'attribut style du module avant son drag
     */
    private String currentModulesStyle;
    /**
     * Valeur de l'attribut Style à appliqué à un module qui est dragged
     */
    private String defaultSelectionStyle = "-fx-background-color:#1FDE28";

    /**
     * Fonction appelee lors du clic sur File -> Close
     * @param event
     */

    @FXML
    void closeApplication(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Fonction appelee lors du clic sur Help -> About
     * @param event
     */
    @FXML
    void showAboutScene(ActionEvent event) { }

    private Controller controller;

    /**
     * Debut du Drag&Drop depuis le menu des modules, creation d'une nouvelle instance du module
     * selectionne
     * @param mouseEvent
     * @param source, module a instancier sur le plan de travail
     * @param fxml nom du fxml du module à instancier
     */
    private void onSpawnDragDetected(MouseEvent mouseEvent, Pane source, String fxml) {
        /**
         * Variables indispensables pour le Drag&Drop
         */
        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Creation du module");
        dragBoard.setContent(content);

        /**
         * Instanciantion du nouveau module
         */
        draggedModule = controller.createModule(fxml);
        draggedModule.setLayoutX(source.getLayoutX());
        draggedModule.setLayoutY(source.getLayoutY());
        draggedModule.setOnDragDetected(de -> onDragDetected(de, draggedModule));
        draggedModule.setOnDragDone(de -> onDragDone(de, draggedModule));
        draggedModule.setStyle(defaultSelectionStyle);

        spawning = true;
        deltaX = mouseEvent.getSceneX() - draggedModule.getLayoutX();
        deltaY = mouseEvent.getSceneY() - draggedModule.getLayoutY();

        /**
         * Ajout du nouveau module sur le hoverPanel, pour pouvoir le deplacer sur toute
         * la fenetre
         */
        hoverPanel.getChildren().add(draggedModule);
        mouseEvent.consume();
    }

    /**
     * Debut du Drag&Drop depuis le plan de travail, debut du deplacement d'un module
     * @param mouseEvent
     * @param source, module a deplacer
     */
    public void onDragDetected(MouseEvent mouseEvent, Pane source) {
        /**
         * Variables indispensables pour le Drag&Drop
         */
        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Deplacement du rectangle");
        dragBoard.setContent(content);

        /**
         * Le module est deja sur le plan de travail, on deplace donc l'instance
         */
        draggedModule = source;
        currentModulesStyle = draggedModule.getStyle();
        draggedModule.setStyle(defaultSelectionStyle);

        deltaX = mouseEvent.getSceneX() - draggedModule.getLayoutX();
        deltaY = mouseEvent.getSceneY() - draggedModule.getLayoutY();

        mouseEvent.consume();
    }

    /**
     * Fin du Drag&Drop depuis le menu des modules, ajout du nouveau module dans le plan de travail
     * @param dragEvent
     * @param source
     * @param fxml nom du fxml du module à instancier
     */
    private void onSpawnDragDone(DragEvent dragEvent, Pane source, String fxml) {
        dragEvent.getDragboard().clear();
        draggedModule.setStyle(currentModulesStyle);

        /**
         * Suppression du module sur le panel de Drag&drop
         */
        hoverPanel.getChildren().remove(draggedModule);


        /**
         * Instanciation du nouveau module si il est au dessus du workspace
         */

        if(draggedModule.getLayoutY() > moduleMenu.getHeight()){
            Pane module = controller.createModule(fxml);
            module.setLayoutX(draggedModule.getLayoutX());
            module.setLayoutY(draggedModule.getLayoutY() - moduleMenu.getHeight() -10);
            module.setOnDragDetected(de -> onDragDetected(de, module));
            module.setOnDragDone(de -> onDragDone(de, module));
            spawning = false;

            /**
             * Ajout du nouveau module sur le workspace
             */
            workspace.getChildren().add(module);
        }

        dragEvent.consume();
    }

    /**
     * Fin du Drag&Drop depuis le plan de travail, fin du deplacement d'un module
     * @param dragEvent
     * @param source
     */
    public void onDragDone(DragEvent dragEvent, Pane source) {
        dragEvent.getDragboard().clear();
        draggedModule.setStyle(currentModulesStyle);
        dragEvent.consume();
    }

    /**
     * Entree dans le plan de travail (zone de depot des modules) lors du Drag&Drop
     * @param dragEvent
     */
    private void onDragEntered(DragEvent dragEvent) {
        dragEvent.consume();
    }

    /**
     * Sortie du plan de travail (zone de depot des modules) lors du Drag&Drop
     * @param dragEvent
     */
    private void onDragExited(DragEvent dragEvent) {
        dragEvent.consume();
    }

    /**
     * Deplacement du module au lors du survol du plan de travail pendant le Drag&Drop
     * @param dragEvent
     */
    private void onDragOver(DragEvent dragEvent) {
        /**
         * Indique que le Drop est possible
         */
        dragEvent.acceptTransferModes(TransferMode.MOVE);

        /**
         * Deplacement du module
         */
        if(dragEvent.getSceneX() - deltaX > 0 && dragEvent.getSceneX() - deltaX < workspace.getWidth()){
            draggedModule.setLayoutX(dragEvent.getSceneX() - deltaX);
        }
        if(dragEvent.getSceneY() - deltaY > 0 && dragEvent.getSceneY() - deltaY < workspace.getScene().getHeight()){
            draggedModule.setLayoutY(dragEvent.getSceneY() - deltaY);
        }
        dragEvent.consume();
    }

    /**
     * Fin du Drag&Drop
     * @param dragEvent
     */
    private void onDragDropped(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    /**
     * Implémentation de l'interface Initializable
     * Ne fait rien, l'initialisation se fait avec init() qui est appelée depuis le Controller
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Initialisation du controller
     */
    public void init() {
        /**
         * Ajout des fonctions de Drag&Drop du plan de travail
         */
        workspace.setOnDragEntered(de -> onDragEntered(de));
        workspace.setOnDragExited(de -> onDragExited(de));

        workspace.setOnDragOver(de -> onDragOver(de));
        workspace.setOnDragDropped(de -> onDragDropped(de));

        /**
         * Fonction de Drag&Drop sur l'hoverPanel, utilise lors de l'instanciation des modules
         * depuis le menu
         */
        hoverPanel.setOnDragOver(de -> onDragOver(de));


        initModulesInModuleMenu();

        /**
         * Ajout par défaut d'un module de sortie au workspace
         */
        Pane out = controller.createModule(FxmlFilesNames.MODULE_OUT);
        workspace.getChildren().add(out);
        out.setLayoutX(250);
        out.setLayoutY(300);
        out.setOnDragDetected(de -> onDragDetected(de, out));
        out.setOnDragDone(de -> onDragDone(de, out));
    }

    private OutputModule mo ;

    /**
     * Ajout des modules au menu
     */
    private void initModulesInModuleMenu(){
        addModuleToMenu(FxmlFilesNames.VCA, 0, 0);
        addModuleToMenu(FxmlFilesNames.VCO, 353, 0);
    }
    
    /**
     * Ajouter un module dans le Pane du menu et ajout des event drag&drop au module
     * @param fxmlModuleFileName nom du fxml du module à ajouter
     * @param x
     * @param y
     */
    private void addModuleToMenu(String fxmlModuleFileName, double x , double y){
        Pane modulePane;
        modulePane = controller.createModule(fxmlModuleFileName);
        moduleMenu.getChildren().add(modulePane);
        modulePane.setLayoutX(x);
        modulePane.setLayoutY(y);

        modulePane.setOnDragDetected(de -> onSpawnDragDetected(de, modulePane, fxmlModuleFileName));
        modulePane.setOnDragDone(de -> onSpawnDragDone(de, modulePane, fxmlModuleFileName));
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}