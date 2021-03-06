package ihm;

import controller.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.QuadCurve;
import utils.FxmlFilesNames;
import java.awt.*;

import utils.SkinNames;

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
    private ScrollPane moduleScrollPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Pane moduleMenu;

    @FXML
    private Menu menuSkin;

    /**
     * Constantes locales
     */
    private static final int PADDING = 60;

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
     * @param event l'event à l'origine de l'appel
     */
    @FXML
    void closeApplication(ActionEvent event) {
        Platform.exit();
        controller.close();
    }

    /**
     * Fonction appelee lors du clic sur Workspace -> Save
     * @param event l'event à l'origine de l'appel
     */
    @FXML
    void saveWorkspace(ActionEvent event){
        controller.saveWorkspace();
    }

    /**
     * Fonction appelee lors du clic sur Workspace -> Load
     * @param event l'event à l'origine de l'appel
     */
    @FXML
    void loadWorkspace(ActionEvent event){
        controller.loadWorkspace();
    }

    /**
     * Fonction appelee lors du clic sur Workspace -> Clean
     * @param event l'event à l'origine de l'appel
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
     * @param event l'event à l'origine de l'appel
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
     * @param mouseEvent l'event à l'origine de l'appel
     * @param source, module a instancier sur le plan de travail
     * @param fxml nom du fxml du module à instancier
     */
    private void onSpawnDragDetected(MouseEvent mouseEvent, Pane source, String fxml) {


        // Variables indispensables pour le Drag&Drop

        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Creation du module");
        dragBoard.setContent(content);


        // Instanciantion du nouveau module

        Point2D mouseEventToModuleMenu = moduleScrollPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        draggedModule = controller.createModule(fxml);
        draggedModule.setLayoutX(mouseEventToModuleMenu.getX() - PADDING);
        draggedModule.setLayoutY(source.getLayoutY());
        draggedModule.setOnDragDetected(de -> onDragDetected(de, draggedModule));
        draggedModule.setOnDragDone(de -> onDragDone(de, draggedModule));
        currentModulesStyle = draggedModule.getStyle();
        draggedModule.setStyle(defaultSelectionStyle);


        Point2D mouseEventToWorkspace = workspace.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        deltaX = mouseEventToWorkspace.getX() - draggedModule.getLayoutX();
        deltaY = mouseEventToWorkspace.getY() - draggedModule.getLayoutY();


        /* Ajout du nouveau module sur le hoverPanel, pour pouvoir le deplacer sur toute
        la fenetre*/

        hoverPanel.getChildren().add(draggedModule);
        mouseEvent.consume();
    }

    /**
     * Debut du DragAndDrop depuis le plan de travail, debut du déplacement d'un module
     * @param mouseEvent l'event à l'origine de l'appel
     * @param source module a déplacer
     */
    public void onDragDetected(MouseEvent mouseEvent, Pane source) {

        //Variables indispensables pour le Drag&Drop

        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Deplacement du rectangle");
        dragBoard.setContent(content);


        // Le module est deja sur le plan de travail, on deplace donc l'instance

        draggedModule = source;
        currentModulesStyle = draggedModule.getStyle();
        draggedModule.setStyle(defaultSelectionStyle);
        draggedModule.toFront();

        Point2D mouseEventToWorkspace = workspace.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        deltaX = mouseEventToWorkspace.getX() - draggedModule.getLayoutX();
        deltaY = mouseEventToWorkspace.getY() - draggedModule.getLayoutY();

        mouseEvent.consume();
    }

    /**
     * Fin du DragAndDrop depuis le menu des modules, ajout du nouveau module dans le plan de travail
     * @param dragEvent l'event à l'origine de l'appel
     */
    private void onSpawnDragDone(DragEvent dragEvent) {
        dragEvent.getDragboard().clear();
        draggedModule.setStyle(currentModulesStyle);


        // Suppression du module sur le panel de Drag&drop

        hoverPanel.getChildren().remove(draggedModule);


        // Instanciation du nouveau module si il est au dessus du workspace

        if(draggedModule.getLayoutY() > moduleMenu.getHeight()){
            Pane module = draggedModule;

            Point2D draggedModuleToWorkspace = workspace.sceneToLocal(draggedModule.getLayoutX(),
                    draggedModule.getLayoutY());
            module.setLayoutX(draggedModuleToWorkspace.getX());
            module.setLayoutY(draggedModuleToWorkspace.getY() + 30);
            module.setOnDragDetected(de -> onDragDetected(de, module));
            module.setOnDragDone(de -> onDragDone(de, module));
            module.setStyle(currentModulesStyle);

            // Ajout du nouveau module sur le workspace

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
     * Fin du DragAndDrop depuis le plan de travail, fin du deplacement d'un module
     * @param dragEvent l'event à l'origine de l'appel
     * @param source le pane qui à été déplacé
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
     * @param dragEvent l'event à l'origine de l'appel
     */
    private void onDragEntered(DragEvent dragEvent) {
        dragEvent.consume();
    }

    /**
     * Sortie du plan de travail (zone de depot des modules) lors du Drag&Drop
     * @param dragEvent l'event à l'origine de l'appel
     */
    private void onDragExited(DragEvent dragEvent) {
        dragEvent.consume();
    }

    /**
     * Deplacement du module au lors du survol du plan de travail pendant le Drag&Drop
     * @param dragEvent l'event à l'origine de l'appel
     */
    private void onDragOver(DragEvent dragEvent) {

        //Indique que le Drop est possible

        dragEvent.acceptTransferModes(TransferMode.MOVE);

        // Deplacement du module

        Point2D dragEventToWorkspace = workspace.sceneToLocal(dragEvent.getSceneX(), dragEvent.getSceneY());

        if(dragEventToWorkspace.getX() - deltaX > 0 && dragEventToWorkspace.getX() - deltaX < workspace.getWidth()){
            draggedModule.setLayoutX(dragEventToWorkspace.getX() - deltaX);
        }
        if(dragEventToWorkspace.getY() - deltaY > 0 && dragEventToWorkspace.getY() - deltaY < workspace.getHeight()){
            draggedModule.setLayoutY(dragEventToWorkspace.getY() - deltaY);
        }

        dragEvent.consume();
    }

    /**
     * Fin du Drag&Drop
     * @param dragEvent l'event à l'origine de l'appel
     */
    private void onDragDropped(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    /**
     * Implémentation de l'interface Initializable
     * Ne fait rien, l'initialisation se fait avec init() qui est appelée depuis le Controller
     * @param location inutilisé
     * @param resources inutilisé
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setHvalue(-500.0);
        scrollPane.setVvalue(-500.0);
    }

    /**
     * Initialisation du controller
     * @param initModuleMenu indique si le menu est à initialiser
     */
    public void init(boolean initModuleMenu) {
        if (initModuleMenu) {
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setPannable(true);


            // Ajout des fonctions de Drag&Drop du plan de travail

            workspace.setOnDragEntered(this::onDragEntered);
            workspace.setOnDragExited(this::onDragExited);

            workspace.setOnDragOver(this::onDragOver);
            workspace.setOnDragDropped(this::onDragDropped);


            /*
              Fonction de Drag&Drop sur l'hoverPanel, utilise lors de l'instanciation des modules
              depuis le menu
             */
            hoverPanel.setOnDragOver(this::onDragOver);

            initModulesInModuleMenu();

            MenuItem itemSkin1 = new MenuItem();
            itemSkin1.setText(SkinNames.SKIN_MOCHE_NAME);
            itemSkin1.setOnAction(event -> Controller.getInstance().setSkin(SkinNames.SKIN_MOCHE_NAME));

            MenuItem itemSkin2 = new MenuItem();
            itemSkin2.setText(SkinNames.SKIN_METAL);
            itemSkin2.setOnAction(event -> Controller.getInstance().setSkin(SkinNames.SKIN_METAL));

            menuSkin.getItems().addAll(itemSkin1,itemSkin2);
        }

        // Ajout par défaut d'un module de sortie au workspace

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Pane out = controller.createModule(FxmlFilesNames.MODULE_OUT);
        workspace.getChildren().add(out);
        out.setLayoutX(screenSize.getWidth() - 420);
        out.setLayoutY(screenSize.getHeight() - 330);
        out.setOnDragDetected(de -> onDragDetected(de, out));
        out.setOnDragDone(de -> onDragDone(de, out));

        // Ajout par défaut d'un module keyboard

        Pane key = controller.createModule(FxmlFilesNames.KEYBOARD);
        workspace.getChildren().add(key);
        key.setLayoutX(20);
        key.setLayoutY(screenSize.getHeight() - 330);
        key.setOnDragDetected(de -> onDragDetected(de, key));
        key.setOnDragDone(de -> onDragDone(de, key));
    }

    /**
     * Ajout des modules au menu
     */
    private void initModulesInModuleMenu(){
        addModuleToMenu(FxmlFilesNames.VCA, FxmlFilesNames.MINIATURE_VCA,0, 0);
        addModuleToMenu(FxmlFilesNames.VCO, FxmlFilesNames.MINIATURE_VCO,100, 0);
        addModuleToMenu(FxmlFilesNames.REP, FxmlFilesNames.MINIATURE_REP,200, 0);
        addModuleToMenu(FxmlFilesNames.VCFLP, FxmlFilesNames.MINIATURE_VCFLP,300, 0);
        addModuleToMenu(FxmlFilesNames.VCFHP, FxmlFilesNames.MINIATURE_VCFHP,400, 0);
        addModuleToMenu(FxmlFilesNames.OSCILLOSCOPE, FxmlFilesNames.MINIATURE_OSCILLOSCOPE, 500, 0);
        addModuleToMenu(FxmlFilesNames.EG, FxmlFilesNames.MINIATURE_EG,600, 0);
        addModuleToMenu(FxmlFilesNames.BRUITBLANC, FxmlFilesNames.MINIATURE_BRUITBLANC, 700, 0);
        addModuleToMenu(FxmlFilesNames.SEQ, FxmlFilesNames.MINIATURE_SEQ, 800, 0);
        addModuleToMenu(FxmlFilesNames.MIX, FxmlFilesNames.MINIATURE_MIX, 900, 0);
    }

    /**
     * Ajouter un module dans le Pane du menu et ajout des event drag&drop au module
     * @param fxmlModuleFileName nom du fxml du module à ajouter
     * @param miniature le nom de la miniature associée au module
     * @param x la position x de la miniature
     * @param y la position y de la miniature
     */
    private void addModuleToMenu(String fxmlModuleFileName, String miniature, double x , double y){
        Pane miniaturePane = controller.createMiniature(miniature);
        moduleMenu.getChildren().add(miniaturePane);
        miniaturePane.setLayoutX(x);
        miniaturePane.setLayoutY(y);
        miniaturePane.setOnDragDetected(de -> onSpawnDragDetected(de, miniaturePane, fxmlModuleFileName));
        miniaturePane.setOnDragDone(this::onSpawnDragDone);
    }

    /**
     * Verifie les collisions entre les modules sur le workspace lors d'un
     * Drag&Drop, et corrige la position le cas echeant
     * @param module deplace lors du Drag&Drop
     */
    private boolean checkNoCollisionInWorkspace(Pane module) {
        for (Node moduleToCheck : workspace.getChildren()) {
            /*
              Evite que le module se compare avec lui-meme et
              verifie l'intersection de deux modules
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
     * @param module le pane déplacé
     * @param moduleInCollision le pane avec lequel il y a collision
     */
    private void handleCollisionOnWorkspace(Pane module, Node moduleInCollision) {
        boolean goRight;
        double rightDelta;
        double bottomDelta;


        // Recuperation des centres des deux modules qui se chevauchent

        Point2D moduleCenter = new Point2D(
                module.getLayoutX() + module.getWidth() / 2,
                module.getLayoutY() + module.getHeight() / 2);

        Pane moduleInCollisionCenter = (Pane) moduleInCollision;
        Point2D moduleToCheckCenter = new Point2D(
                moduleInCollisionCenter.getLayoutX() + moduleInCollisionCenter.getWidth() / 2,
                moduleInCollisionCenter.getLayoutY() + (moduleInCollisionCenter.getHeight() / 2));


        // Reinitialisation des variables

        goRight = false;


        // Calcule la position relative en X

        rightDelta = moduleCenter.getX() - moduleToCheckCenter.getX();


        // Calcule la position relative en Y

        bottomDelta = moduleCenter.getY() - moduleToCheckCenter.getY();


        // Choix de la direction parmi les deux restantes selon l'ecart en X et Y

        if (Math.abs(rightDelta) > Math.abs(bottomDelta)) {
            goRight = true;
        }


        // Deplacement du module vers le bon cote

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

    public Pane getWorkspace() {
        return workspace;
    }

    public void addModuleToWorkspace(Pane pane, double xPos, double yPos){
        workspace.getChildren().add(pane);
        pane.setLayoutY(yPos);
        pane.setLayoutX(xPos);
        pane.setOnDragDetected(de -> onDragDetected(de, pane));
        pane.setOnDragDone(de -> onDragDone(de, pane));
    }
}