package ihm;

import controller.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.stage.Screen;
import utils.Cable;
import utils.CableManager;
import utils.FxmlFilesNames;

import java.net.URL;
import java.util.ResourceBundle;

public class IHMController implements Initializable{

    /**
     * Variables FXML
     */
    @FXML
    private Pane workspace;

    @FXML
    private Pane hoverPanel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Pane moduleMenu;

    /**
     * Constantes locales
     */
    private static final int PADDING = 44;

    /**
     * Variables locales
     */
    private Pane draggedModule;
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
        controller.close();
    }

    /**
     * Fonction appelee lors du clic sur Workspace -> Clean
     * @param event
     */
    @FXML
    void cleanWorkspace(ActionEvent event){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Voulez-vous vraiment nettoyer le workspace ?");
        confirm.setTitle("Nettoyage du workspace");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controller.close();
                workspace.getChildren().clear();
                init(false);
                controller.getSynth().start();
            }
        });
    }

    /**
     * Fonction appelee lors du clic sur Help -> About
     * @param event
     */
    @FXML
    void showAboutScene(ActionEvent event) {
        String aboutText = "Application SynthLab\n\n" +
                "Projet de M2 IL\n\n" +
                "Réalisé par le Groupe A composé de :\n" +
                "Aurélien ANNE\n" +
                "Marie COISNARD-SIMON\n" +
                "Nicolas FORTUN\n" +
                "Julien LAURENT\n" +
                "Ahmed NOMANE\n" +
                "Kevin SALMON";
        Alert about = new Alert(Alert.AlertType.INFORMATION, aboutText);
        about.setTitle("A propos");
        about.setHeaderText("A propos de l'application");
        about.show();
    }

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
        currentModulesStyle = draggedModule.getStyle();
        draggedModule.setStyle(defaultSelectionStyle);

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
        draggedModule.toFront();

        deltaX = mouseEvent.getSceneX() - draggedModule.getLayoutX();
        deltaY = mouseEvent.getSceneY() - draggedModule.getLayoutY();

        mouseEvent.consume();
    }

    /**
     * Fin du Drag&Drop depuis le menu des modules, ajout du nouveau module dans le plan de travail
     * @param dragEvent
     * @param fxml nom du fxml du module à instancier
     */
    private void onSpawnDragDone(DragEvent dragEvent, String fxml) {
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
            Pane module = draggedModule;//controller.createModule(fxml);
            module.setLayoutX(draggedModule.getLayoutX());
            module.setLayoutY(draggedModule.getLayoutY() - moduleMenu.getHeight() -10);
            module.setOnDragDetected(de -> onDragDetected(de, module));
            module.setOnDragDone(de -> onDragDone(de, module));
            module.setStyle(currentModulesStyle);
            /**
             * Ajout du nouveau module sur le workspace
             */
            workspace.getChildren().add(module);

            boolean collision = true;
            while(collision) {
                if(checkNoCollisionInWorkspace(module)){
                    collision = false;
                }
            }
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

        boolean collision = true;
        while(collision) {
            if(checkNoCollisionInWorkspace(draggedModule)){
                collision = false;
            }
        }

        dragEvent.consume();
        draggedModule.toBack();
        draggedModule.setStyle(currentModulesStyle);

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

        // TODO: Corriger le décalage de la souris lorsque l'on ramènne un modules qui était en bas à droite
        // Piste : Rajouter les valeurs de scrollPane.getViewportBounds() ci-dessous
        if(dragEvent.getSceneX() - deltaX > 0 && dragEvent.getSceneX() - deltaX < workspace.getWidth()){
            draggedModule.setLayoutX(dragEvent.getSceneX() - deltaX);
        }
        if(dragEvent.getSceneY() - deltaY > 0 && dragEvent.getSceneY() - deltaY < workspace.getHeight()){
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
        //Initialisation non-nécessaire
    }

    /**
     * Initialisation du controller
     */
    public void init(boolean initModuleMenu) {
        if (initModuleMenu) {
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setPannable(true);

            /**
             * Ajout des fonctions de Drag&Drop du plan de travail
             */
            workspace.setOnDragEntered(this::onDragEntered);
            workspace.setOnDragExited(this::onDragExited);

            workspace.setOnDragOver(this::onDragOver);
            workspace.setOnDragDropped(this::onDragDropped);


            /**
             * Fonction de Drag&Drop sur l'hoverPanel, utilise lors de l'instanciation des modules
             * depuis le menu
             */
            hoverPanel.setOnDragOver(this::onDragOver);

            initModulesInModuleMenu();
        }

        /**
         * Ajout par défaut d'un module de sortie au workspace
         */
        Pane out = controller.createModule(FxmlFilesNames.MODULE_OUT);
        workspace.getChildren().add(out);
        out.setLayoutX(splitPane.getWidth()- 350);
        out.setLayoutY(70);
        out.setOnDragDetected(de -> onDragDetected(de, out));
        out.setOnDragDone(de -> onDragDone(de, out));
    }

    /**
     * Ajout des modules au menu
     */
    private void initModulesInModuleMenu(){
        addModuleToMenu(FxmlFilesNames.VCA, FxmlFilesNames.MINIATURE_VCA,0, 0);
        addModuleToMenu(FxmlFilesNames.VCO, FxmlFilesNames.MINIATURE_VCO,100, 0);
        addModuleToMenu(FxmlFilesNames.REP, FxmlFilesNames.MINIATURE_REP,200, 0);
        addModuleToMenu(FxmlFilesNames.VCFLP, FxmlFilesNames.MINIATURE_VCFLP,300, 0);
        addModuleToMenu(FxmlFilesNames.OSCILLOSCOPE, FxmlFilesNames.MINIATURE_OSCILLOSCOPE, 400, 0);
        addModuleToMenu(FxmlFilesNames.EG, FxmlFilesNames.MINIATURE_EG,500, 0);
    }

    /**
     * Ajouter un module dans le Pane du menu et ajout des event drag&drop au module
     * @param fxmlModuleFileName nom du fxml du module à ajouter
     * @param x
     * @param y
     */
    private void addModuleToMenu(String fxmlModuleFileName, String miniature, double x , double y){
        Pane miniaturePane = controller.createMiniature(miniature);
        moduleMenu.getChildren().add(miniaturePane);
        miniaturePane.setLayoutX(x);
        miniaturePane.setLayoutY(y);
        miniaturePane.setOnDragDetected(de -> onSpawnDragDetected(de, miniaturePane, fxmlModuleFileName));
        miniaturePane.setOnDragDone(de -> onSpawnDragDone(de, fxmlModuleFileName));
    }

    /**
     * Verifie les collisions entre les modules sur le workspace lors d'un
     * Drag&Drop, et corrige la position le cas echeant
     * @param module deplace lors du Drag&Drop
     */
    private boolean checkNoCollisionInWorkspace(Pane module) {
        for (Node moduleToCheck : workspace.getChildren()) {
            /**
             * Evite que le module se compare avec lui-meme et
             * verifie l'intersection de deux modules
             */
            if (!(moduleToCheck instanceof QuadCurve) && moduleToCheck != module &&
                    moduleToCheck.getBoundsInParent().intersects(module.getBoundsInParent())) {
                handleCollisionOnWorkspace(module, moduleToCheck);
                return false;
            }
        }
        return true;
    }

    /**
     * Deplace le module en dehors du module en collision
     * @param module
     * @param moduleInCollision
     */
    private void handleCollisionOnWorkspace(Pane module, Node moduleInCollision) {
        boolean goRight;
        double rightDelta;
        double bottomDelta;

        /**
         * Recuperation des centres des deux modules qui se chevauchent
         */
        Point2D moduleCenter = new Point2D(
                module.getLayoutX() + module.getWidth() / 2,
                module.getLayoutY() + module.getHeight() / 2);

        Pane moduleInCollisionCenter = (Pane) moduleInCollision;
        Point2D moduleToCheckCenter = new Point2D(
                moduleInCollisionCenter.getLayoutX() + moduleInCollisionCenter.getWidth() / 2,
                moduleInCollisionCenter.getLayoutY() + (moduleInCollisionCenter.getHeight() / 2));

        /**
         * Reinitialisation des variables
         */
        goRight = false;

        /**
         * Calcule la position relative en X
         */
        rightDelta = moduleCenter.getX() - moduleToCheckCenter.getX();

        /**
         * Calcule la position relative en Y
         */
        bottomDelta = moduleCenter.getY() - moduleToCheckCenter.getY();

        /**
         * Choix de la direction parmi les deux restantes selon l'ecart en X et Y
         */
        if (Math.abs(rightDelta) > Math.abs(bottomDelta)) {
            goRight = true;
        }

        /**
         * Deplacement du module vers le bon cote
         */
        if (goRight) {
            module.setLayoutX(moduleInCollisionCenter.getLayoutX() + moduleInCollisionCenter.getWidth() + 2);
        } else { //goBottom
            module.setLayoutY(moduleInCollisionCenter.getLayoutY() + moduleInCollisionCenter.getHeight() + 2);
        }

    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void onRezize(Number width, Number height) {
        if(width != null) {
            splitPane.setPrefWidth(Double.parseDouble(width.toString()));
        }
        else if(height != null) {
            splitPane.setPrefHeight(Double.parseDouble(height.toString())-PADDING);
        }
    }
}