package utils;

import Exceptions.OutputException;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton which manages the creation and updates of cables
 */
public class CableManager {

    protected List<Cable> cables;

    protected Cable currentCable;

    protected static volatile CableManager instance = null;

    protected CableManager(){
        cables = new ArrayList<Cable>();
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
}
