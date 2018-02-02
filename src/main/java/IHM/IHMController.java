package IHM;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import utils.FxmlFilesNames;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IHMController implements Initializable {

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
     * Variables locales
     */
    private Pane draggedModule;

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
        draggedModule = createModule(fxml);
        draggedModule.setLayoutX(source.getLayoutX());
        draggedModule.setLayoutY(source.getLayoutY());
        draggedModule.setOnDragDetected(de -> onDragDetected(de, draggedModule));
        draggedModule.setOnDragDone(de -> onDragDone(de, draggedModule));

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
    private void onDragDetected(MouseEvent mouseEvent, Pane source) {
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
         * Instanciation du nouveau module
         */
        Pane module = createModule(fxml);
        module.setLayoutX(draggedModule.getLayoutX());
        module.setLayoutY(draggedModule.getLayoutY());
        module.setOnDragDetected(de -> onDragDetected(de, module));
        module.setOnDragDone(de -> onDragDone(de, module));
        /**
         * Ajout du nouveau module sur le workspace
         */
        workspace.getChildren().add(module);
        dragEvent.consume();
    }

    /**
     * Fin du Drag&Drop depuis le plan de travail, fin du deplacement d'un module
     * @param dragEvent
     * @param source
     */
    private void onDragDone(DragEvent dragEvent, Pane source) {
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
        draggedModule.setLayoutX(dragEvent.getX());
        draggedModule.setLayoutY(dragEvent.getY());
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
     * Initialisation du controller
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        Pane out = createModule(FxmlFilesNames.MODULE_OUT);
        workspace.getChildren().add(out);
        out.setLayoutX(250);
        out.setLayoutY(300);
        out.setOnDragDetected(de -> onDragDetected(de, out));
        out.setOnDragDone(de -> onDragDone(de, out));
    }

    /**
     * Ajout des modules au menu
     */
    private void initModulesInModuleMenu(){
        addModuleToMenu(FxmlFilesNames.MODULE_OUT, 353, 0);
        addModuleToMenu(FxmlFilesNames.VCA, 0, 0);
        addModuleToMenu(FxmlFilesNames.VCO, 732, 0);
    }

    /**
     * Création d'un module en chargeant le fxml associé
     * @param FxmlModuleFileName nom du fxml du module à ajouter
     * @return module
     */
    private Pane createModule(String FxmlModuleFileName){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FxmlModuleFileName));
        Pane modulePane = null;
        try {
            modulePane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return modulePane;
    }

    /**
     * Ajouter un module dans le Pane du menu et ajout des event drag&drop au module
     * @param fxmlModuleFileName nom du fxml du module à ajouter
     * @param x
     * @param y
     */
    private void addModuleToMenu(String fxmlModuleFileName, double x , double y){
        Pane modulePane;
        modulePane = createModule(fxmlModuleFileName);
        moduleMenu.getChildren().add(modulePane);
        modulePane.setLayoutX(x);
        modulePane.setLayoutY(y);

        modulePane.setOnDragDetected(de -> onSpawnDragDetected(de, modulePane, fxmlModuleFileName));
        modulePane.setOnDragDone(de -> onSpawnDragDone(de, modulePane, fxmlModuleFileName));
    }
}