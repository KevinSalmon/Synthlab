package utils;

import exceptions.PortTypeException;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import module.Module;

public class Cable {

    private String outputName;
    private String inputName;

    private Circle output;
    
    private Circle input;

    private QuadCurve curve;

    private Module moduleIn;

    private Module moduleOut;

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

    public void connect() throws PortTypeException {
        moduleOut.connect(moduleIn, outputName, inputName);
    }
    public void disconnect() {
        moduleOut.disconnect(moduleIn, outputName, inputName);
    }
}
