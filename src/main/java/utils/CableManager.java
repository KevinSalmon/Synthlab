package utils;

import exceptions.OutputException;
import exceptions.PortTypeException;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
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

    protected QuadCurve curve;

    protected static volatile CableManager instance = null;

    protected CableManager(){
        cables = new ArrayList<>();
    }


    public static CableManager getInstance() {
        synchronized (CableManager.class){
            if(instance == null) instance = new CableManager();
        }
        return CableManager.instance;
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
    }

    /**
     * Set the input of a cable and creates a curve
     * @param point2D the input
     * @return the curve created
     * @throws OutputException when the user chooses the input first
     */
    public void setInput(Circle point2D, Module moduleIn, String name) {
            for(Cable c : cables){http:
                if(c.getInput().equals(point2D)) return;
            }
            currentCable.setInput(point2D);
            currentCable.setInputName(name);
            curve = new QuadCurve();
            Logger.getGlobal().info("in parent "+currentCable.getInput().getParent()+" out parent "+currentCable.getOutput().getParent());
            Point2D in = currentCable.getInput().getParent().localToParent(currentCable.getInput().getLayoutX(), currentCable.getInput().getLayoutY());
            Point2D out = currentCable.getOutput().getParent().localToParent(currentCable.getOutput().getLayoutX(), currentCable.getOutput().getLayoutY());
        Logger.getGlobal().info("in : "+in +" out"+out);
        curve.setStartX(in.getX());
            curve.setStartY(in.getY());
            curve.setEndX(out.getX());
            curve.setEndY(out.getY());
            curve.setControlX((in.getX() + out.getX())/2);
            curve.setControlY(in.getY() + out.getY());
            curve.setStroke(Color.BLACK);
            curve.setStrokeWidth(10);
            curve.setFill( null);

        try {
            currentCable.setCurve(curve);
            cables.add(currentCable);
            currentCable.setModuleIn(moduleIn);
            currentCable.connect();
            curve.toFront();

        } catch (PortTypeException e) {
            Logger.getGlobal().severe(e.getMessage()
            );
        }
        Logger.getGlobal().info(String.valueOf(curve));
        currentCable = null;

    }


    /**
     * Update the  x output
     * @param output the output
     */
    public void updateOutputX(Circle output) {
        for(Cable c : cables){
            if(c.getOutput().equals(output)) {
                double xPoint = output.getParent().localToParent(output.getLayoutX(), output.getLayoutY()).getX();
                c.getCurve().setEndX(xPoint);
                c.getCurve().setControlX((c.getInput().getParent().localToParent(c.getInput().getLayoutX(), c.getInput().getLayoutY()).getX() + xPoint)/2);
                break;
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
                double yPoint = output.getParent().localToParent(output.getLayoutX(), output.getLayoutY()).getY();
                c.getCurve().setEndY(yPoint);
                c.getCurve().setControlY(c.getInput().getParent().localToParent(c.getInput().getLayoutX(), c.getInput().getLayoutY()).getY() + yPoint);
                break;
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
                double xPoint = in.getParent().localToParent(in.getLayoutX(), in.getLayoutY()).getX();
                c.getCurve().setStartX(xPoint);
                c.getCurve().setControlX((c.getOutput().getParent().localToParent(c.getOutput().getLayoutX(), c.getOutput().getLayoutY()).getX() + xPoint)/2);
                break;
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
                double yPoint = in.getParent().localToParent(in.getLayoutX(), in.getLayoutY()).getY();
                c.getCurve().setStartY(yPoint);
                c.getCurve().setControlY(c.getOutput().getParent().localToParent(c.getOutput().getLayoutX(), c.getOutput().getLayoutY()).getY() + yPoint);
                break;
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
                    if(curve != null) {
                        ((Pane) pane.getParent()).getChildren().add(curve);
                        curve.toFront();
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

    public List<Cable> getCables() {
        return cables;
    }

    public QuadCurve getCurve() {
        return curve;
    }
}
