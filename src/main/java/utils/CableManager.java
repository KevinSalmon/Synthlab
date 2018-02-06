package utils;

import exceptions.OutputException;
import exceptions.PortTypeException;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Singleton which manages the creation and updates of cables
 */
public class CableManager {

    protected List<Cable> cables;

    protected Cable currentCable;

    protected Line line;

    protected static volatile CableManager instance = null;

    protected CableManager(){
        cables = new ArrayList<>();
    }


    public static CableManager getInstance() {
        synchronized (CableManager.class){
            if(instance == null) instance = new CableManager();
            return instance;
        }
    }


    /**
     * Set the output point of the cable
     * @param point2D the output
     */
    public void setOutput(Circle point2D, Module moduleOut, String name){
        for(Cable c : cables){
            if(c.getOutput().equals(point2D)) return;
        }
        currentCable = new Cable();
        currentCable.setOutput(point2D);
        currentCable.setModuleOut(moduleOut);
        currentCable.setOutputName(name);
        Logger.getGlobal().info("je suis passé par ici");
    }

    /**
     * Set the input of a cable and creates a line
     * @param point2D the input
     * @return the line created
     * @throws OutputException when the user chooses the input first
     */
    public void setInput(Circle point2D, Module moduleIn, String name) {
            for(Cable c : cables){
                if(c.getInput().equals(point2D)) return;
            }
            Logger.getGlobal().info("je repasserai par la ");
            currentCable.setInput(point2D);
            currentCable.setInputName(name);
            line = new Line();
            Point2D in = currentCable.getInput().getParent().localToParent(currentCable.getInput().getLayoutX(), currentCable.getInput().getLayoutY());
            Point2D out = currentCable.getOutput().getParent().localToParent(currentCable.getOutput().getLayoutX(), currentCable.getOutput().getLayoutY());
            line.setStartX(in.getX());
            line.setStartY(in.getY());
            line.setEndX(out.getX());

        try {
            line.setEndY(out.getY());
            currentCable.setLine(line);
            cables.add(currentCable);
            currentCable.setModuleIn(moduleIn);
            currentCable.connect();
        } catch (PortTypeException e) {
            Logger.getGlobal().severe(e.getMessage()
            );
        }
        currentCable = null;

    }


    /**
     * Update the  x output
     * @param output the output
     */
    public void updateOutputX(Circle output) {
        for(Cable c : cables){
            if(c.getOutput().equals(output)) {
                c.getLine().setEndX(output.getParent().localToParent(output.getLayoutX(), output.getLayoutY()).getX());
                return;
            }
        }

    }

    /**
     * Update the y output
     * @param output the output
     */
    public void updateOutputY(Circle output) {
        for(Cable c : cables){
            if(c.getOutput().equals(output)) {
                c.getLine().setEndY(output.getParent().localToParent(output.getLayoutX(), output.getLayoutY()).getY());
                return;
            }
        }
    }

    /**
     * Update the x input
     * @param in the input
     */
    public void updateInputX(Circle in) {
        for(Cable c : cables){
            if(c.getInput().equals(in)) {
                c.getLine().setStartX(in.getParent().localToParent(in.getLayoutX(), in.getLayoutY()).getX());
                return;
            }
        }
    }

    /**
     * Update the y input
     * @param in the input
     */
    public void updateInputY(Circle in) {
        for(Cable c : cables){
            if(c.getInput().equals(in)) {
                c.getLine().setStartY(in.getParent().localToParent(in.getLayoutX(), in.getLayoutY()).getY());
                return;
            }
        }
    }

    /**
     * Add a listener to a module
     * @param port the port to listen
     * @param type the type of the port
     * @param pane the pane
     */
    public void addListener(Circle port, Module module, PortType type, Pane pane){


        if(type.getType().contains(PortType.INPUT.getType())) {
            port.getParent().layoutXProperty().addListener((observable, oldValue, newValue) -> instance.updateInputX(port));
            port.getParent().layoutYProperty().addListener((observable, oldValue, newValue) -> instance.updateInputY(port));
            port.setOnMouseClicked(event -> {
                if(currentCable != null) {
                    instance.setInput(port, module, type.getType());
                    if(line != null) {
                        ((Pane) pane.getParent()).getChildren().add(line);
                        line.toFront();
                    }
                }
            });

        }
        else if(type.getType().contains(PortType.OUTPUT.getType())){
            port.getParent().layoutXProperty().addListener((observable, oldValue, newValue) -> instance.updateOutputX(port));
            port.getParent().layoutYProperty().addListener((observable, oldValue, newValue) -> instance.updateOutputY(port));
            port.setOnMouseClicked(event -> instance.setOutput(port, module, type.getType()));
        }

    }
}
