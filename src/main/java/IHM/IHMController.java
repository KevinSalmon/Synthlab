package IHM;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;
import utils.FxmlFilesNames;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class IHMController implements Initializable {

    @FXML
    private Pane workspace;

    @FXML
    private Pane hoverPanel;

    @FXML
    private Pane moduleMenu;

    @FXML
    private Rectangle module1;

    private Rectangle draggedRectangle = new Rectangle();


    @FXML
    void closeApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void showAboutScene(ActionEvent event) { }

    private void onSpawnDragDetected(MouseEvent mouseEvent, Rectangle source) {
        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Creation du rectangle");
        dragBoard.setContent(content);
        draggedRectangle = new Rectangle(source.getX(), source.getY(), source.getWidth(), source.getHeight());
        draggedRectangle.setFill(Color.GREEN);
        hoverPanel.getChildren().add(draggedRectangle);
        mouseEvent.consume();
    }

    private void onDragDetected(MouseEvent mouseEvent, Rectangle source) {
        Dragboard dragBoard = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("Deplacement du rectangle");
        dragBoard.setContent(content);
        draggedRectangle = source;
        draggedRectangle.setFill(Color.GREEN);
        mouseEvent.consume();
    }

    private void onSpawnDragDone(DragEvent dragEvent, Rectangle source) {
        dragEvent.getDragboard().clear();
        draggedRectangle.setFill(Color.BLACK);

        hoverPanel.getChildren().remove(draggedRectangle);

        Rectangle r = new Rectangle(draggedRectangle.getX(),
                draggedRectangle.getY()-moduleMenu.getHeight(),
                draggedRectangle.getWidth(),
                draggedRectangle.getHeight());

        r.setOnDragDetected(de -> onDragDetected(de, r));
        r.setOnDragDone(de -> onDragDone(de, r));
        r.setFill(Color.BLACK);

        workspace.getChildren().add(r);
        dragEvent.consume();
    }

    private void onDragDone(DragEvent dragEvent, Rectangle source) {
        dragEvent.getDragboard().clear();
        draggedRectangle.setFill(Color.BLACK);
        dragEvent.consume();
    }

    private void onDragEntered(DragEvent dragEvent) {
        dragEvent.consume();
    }

    private void onDragExited(DragEvent dragEvent) {
        dragEvent.consume();
    }

    private void onDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.MOVE);     // State that a drop is possible
        draggedRectangle.setX(dragEvent.getX());
        draggedRectangle.setY(dragEvent.getY());
        dragEvent.consume();
    }

    private void onDragDropped(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        module1.setOnDragDetected(de -> onSpawnDragDetected(de, module1));
        module1.setOnDragDone(de -> onSpawnDragDone(de, module1));

        workspace.setOnDragEntered(de -> onDragEntered(de));
        workspace.setOnDragExited(de -> onDragExited(de));

        workspace.setOnDragOver(de -> onDragOver(de));
        workspace.setOnDragDropped(de -> onDragDropped(de));

        hoverPanel.setOnDragOver(de -> onDragOver(de));


        initModulesInModuleMenu();

        addModule(workspace, FxmlFilesNames.MODULE_OUT, 250, 300);
    }

    private void initModulesInModuleMenu(){
        addModule(moduleMenu, FxmlFilesNames.MODULE_OUT, 200, 0);
    }

    private void addModule(Pane pane, String FxmlModuleFileName, double x , double y){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FxmlModuleFileName));
        Pane modulePane = null;
        try {
            modulePane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pane.getChildren().add(modulePane);
        modulePane.setLayoutX(x);
        modulePane.setLayoutY(y);
    }
}