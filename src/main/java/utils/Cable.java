package utils;

import exceptions.PortTypeException;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import module.Module;
import module.PortType;

import java.util.logging.Logger;

public class Cable {

    private Circle output;
    
    private Circle input;

    private Line line;

    private Module moduleIn;

    private Module moduleOut;

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

    public void setLine(Line line) {
        this.line = line;
    }

    public Line getLine(){
        return this.line;
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
        Logger.getGlobal().info("moduleOut "+moduleOut+ " moduleIn "+moduleIn);
       moduleOut.connect(moduleIn, PortType.OUTPUT.getType(), PortType.INPUT.getType());
//        ((VCO) moduleOut).getOutput().connect(((OutputModule) moduleIn).getInput());


    }
}
