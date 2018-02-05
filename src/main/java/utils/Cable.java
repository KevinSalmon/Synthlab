package utils;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Cable {

    private Circle output;
    
    private Circle input;

    private Line line;

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
}
