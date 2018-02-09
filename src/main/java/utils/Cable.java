package utils;

import exceptions.PortTypeException;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
import module.Module;

/**
 * Pojo for a cable
 */
public class Cable {

    private String outputName;
    private String inputName;

    private Circle output;
    
    private Circle input;

    private QuadCurve curve;

    private Module moduleIn;

    private Module moduleOut;
    private static final Color[] colors = {Color.BLACK, Color.PURPLE, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
    private int i = 1;

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public void setOutput(Circle output) {
        this.output = output;
    }

    public void setInput(Circle input) {
        this.input = input;
    }

    public Circle getOutput() {
        return output;
    }

    public Circle getInput() {
        return input;
    }

    public void setCurve(QuadCurve curve) {
        this.curve = curve;
    }

    public QuadCurve getCurve(){
        return this.curve;
    }

    public Module getModuleIn() {
        return moduleIn;
    }

    public void setModuleIn(Module moduleIn) {
        this.moduleIn = moduleIn;
    }

    public Module getModuleOut() {
        return moduleOut;
    }

    public void setModuleOut(Module moduleOut) {
        this.moduleOut = moduleOut;
    }

    /**
     * Connect two cables and add a eventHandler
     * @throws PortTypeException when the two ports ar not compatible
     */
    public void connect() throws PortTypeException {
        moduleOut.connect(moduleIn, outputName, inputName);
        curve.setOnMousePressed(event ->
        {
            if (event.getButton() == MouseButton.PRIMARY) {
                curve.setStroke(colors[i % colors.length]);
                input.setFill(colors[i % colors.length]);
                output.setFill(colors[i % colors.length]);
                i++;
            }else if(event.getButton() == MouseButton.SECONDARY){
                disconnect();
                CableManager.getInstance().getCables().remove(this);
                Pane node = (Pane) curve.getParent();
                node.getChildren().removeAll(curve);
            }
        });
    }

    /**
     * Disconnect the two modules
     */
    public void disconnect() {
        moduleOut.disconnect(moduleIn, outputName, inputName);
    }

}
