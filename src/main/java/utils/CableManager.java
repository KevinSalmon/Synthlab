package utils;

import Exceptions.OutputException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import module.PortType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Singleton which manages the creation and updates of cables
 */
public class CableManager {

    protected List<Cable> cables;

    protected Cable currentCable;

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
    public void setOutput(Circle point2D){
        for(Cable c : cables){
            if(c.getOutput().equals(point2D)) return;
        }
        currentCable = new Cable();
        currentCable.setOutput(point2D);
    }

    /**
     * Set the input of a cable and creates a line
     * @param point2D the input
     * @return the line created
     * @throws OutputException when the user chooses the input first
     */
    public Line setInput(Circle point2D) throws OutputException {
        if(currentCable == null) throw new OutputException("Must choose a output first");
        else {
            currentCable.setInput(point2D);
            Line line = new Line();
            Point2D in = currentCable.getInput().getParent().localToParent(currentCable.getInput().getLayoutX(), currentCable.getInput().getLayoutY());
            Point2D out = currentCable.getOutput().getParent().localToParent(currentCable.getOutput().getLayoutX(), currentCable.getOutput().getLayoutY());
            line.setStartX(in.getX());
            line.setStartY(in.getY());
            line.setEndX(out.getX());
            line.setEndY(out.getY());
            currentCable.setLine(line);
            cables.add(currentCable);
            currentCable = null;
            return line;
        }
    }


    /**
     * Update the  x output
     * @param draw_input the output
     */
    public void updateOutputX(Circle draw_input) {
        for(Cable c : cables){
            if(c.getOutput().equals(draw_input)) {
                c.getLine().setEndX(draw_input.getParent().localToParent(draw_input.getLayoutX(), draw_input.getLayoutY()).getX());
                return;
            }
        }

    }

    /**
     * Update the y output
     * @param draw_input the output
     */
    public void updateOutputY(Circle draw_input) {
        for(Cable c : cables){
            if(c.getOutput().equals(draw_input)) {
                c.getLine().setEndY(draw_input.getParent().localToParent(draw_input.getLayoutX(), draw_input.getLayoutY()).getY());
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

    public void addListener(Circle port, PortType type, Pane pane){
        Logger.getGlobal().info("ajout de listener : "+ type.getType());

        DoubleProperty xValue = new SimpleDoubleProperty();
        xValue.bind(port.getParent().layoutXProperty());
        DoubleProperty yValue = new SimpleDoubleProperty();
        yValue.bind(port.getParent().layoutYProperty());

        if(type.equals(PortType.INPUT)) {
            xValue.addListener((observable, oldValue, newValue) -> instance.updateInputX(port)

            );
            yValue.addListener((observable, oldValue, newValue) -> instance.updateInputY(port)

            );
            port.setOnMouseClicked(event -> {
                try {
                    Line line = instance.setInput(port);
                    ((Pane) pane.getParent()).getChildren().add(line);
                    line.toFront();
                    Logger.getGlobal().info("line added");
                } catch (OutputException e) {
                    e.printStackTrace();
                }
            });

        }
        else if(type.equals(PortType.OUTPUT)){
            DoubleProperty xValueOut = new SimpleDoubleProperty();
            DoubleProperty yValueOut = new SimpleDoubleProperty();
            xValueOut.bind(port.getParent().layoutXProperty());
            xValueOut.addListener((observable, oldValue, newValue) -> instance.updateOutputX(port)

            );
            yValueOut.bind(port.getParent().layoutYProperty());
            yValueOut.addListener((observable, oldValue, newValue) -> instance.updateOutputY(port)

            );

            port.setOnMouseClicked(event -> {
                instance.setOutput(port);
            });
        }

    }
}
