package IHM;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    @FXML
    private Rectangle module1;

    /**
     * Variables locales
     */
    private Rectangle draggedRectangle = new Rectangle();

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
     */
    private void onSpawnDragDetected(MouseEvent mouseEvent, Rectangle source) {
        /**
         * Variables indispensables pour le Drag&Drop
         */
        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Creation du rectangle");
        dragBoard.setContent(content);

        /**
         * Instanciantion du nouveau module
         */
        draggedRectangle = new Rectangle(source.getX(), source.getY(), source.getWidth(), source.getHeight());
        draggedRectangle.setFill(Color.GREEN);

        /**
         * Ajout du nouveau module sur le hoverPanel, pour pouvoir le deplacer sur toute
         * la fenetre
         */
        hoverPanel.getChildren().add(draggedRectangle);
        mouseEvent.consume();
    }

    /**
     * Debut du Drag&Drop depuis le plan de travail, debut du deplacement d'un module
     * @param mouseEvent
     * @param source, module a deplacer
     */
    private void onDragDetected(MouseEvent mouseEvent, Rectangle source) {
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
        draggedRectangle = source;
        draggedRectangle.setFill(Color.GREEN);
        mouseEvent.consume();
    }

    /**
     * Fin du Drag&Drop depuis le menu des modules, ajout du nouveau module dans le plan de travail
     * @param dragEvent
     * @param source
     */
    private void onSpawnDragDone(DragEvent dragEvent, Rectangle source) {
        dragEvent.getDragboard().clear();
        draggedRectangle.setFill(Color.BLACK);

        /**
         * Suppression du module sur le panel de Drag&drop
         */
        hoverPanel.getChildren().remove(draggedRectangle);

        /**
         * Instanciation du nouveau module
         */
        Rectangle r = new Rectangle(draggedRectangle.getX(),
                draggedRectangle.getY()-moduleMenu.getHeight(),
                draggedRectangle.getWidth(),
                draggedRectangle.getHeight());

        r.setOnDragDetected(de -> onDragDetected(de, r));
        r.setOnDragDone(de -> onDragDone(de, r));
        r.setFill(Color.BLACK);

        /**
         * Ajout du nouveau module sur le workspace
         */
        workspace.getChildren().add(r);
        dragEvent.consume();
    }

    /**
     * Fin du Drag&Drop depuis le plan de travail, fin du deplacement d'un module
     * @param dragEvent
     * @param source
     */
    private void onDragDone(DragEvent dragEvent, Rectangle source) {
        dragEvent.getDragboard().clear();
        draggedRectangle.setFill(Color.BLACK);
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
        draggedRectangle.setX(dragEvent.getX());
        draggedRectangle.setY(dragEvent.getY());
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
         * Ajout des fonctions de Drag&Drop des modules
         */
        module1.setOnDragDetected(de -> onSpawnDragDetected(de, module1));
        module1.setOnDragDone(de -> onSpawnDragDone(de, module1));


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
    }
}