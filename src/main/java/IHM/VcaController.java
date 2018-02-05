package IHM;

import Exceptions.OutputException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import utils.CableManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class VcaController implements Initializable{

    @FXML
    Pane pane;

    @FXML
    Spinner<Integer> gainSelector;

    @FXML
    Circle in;


    @FXML
    Circle out;
    
    private int minValue =0;
    private int initialValue = 0;
    private int maxValue = 100;
    private boolean inputClicked;
    private CableManager cableManager;
    private Line line;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
                initialValue);
        gainSelector.setValueFactory(valueFactory);
        gainSelector.setEditable(true);
        this.inputClicked = false;
        cableManager = CableManager.getInstance();
        DoubleProperty xValue = new SimpleDoubleProperty();
        xValue.bind(in.getParent().layoutXProperty());
        xValue.addListener((observable, oldValue, newValue) ->
                cableManager.updateInputX(in)

        );
        DoubleProperty yValue = new SimpleDoubleProperty();
        yValue.bind(in.getParent().layoutYProperty());
        yValue.addListener((observable, oldValue, newValue) ->
                cableManager.updateInputY(in)

        );
        in.setOnMouseClicked(event -> {
            try {
                Line line = cableManager.setInput(in);
                ((Pane) pane.getParent()).getChildren().add(line);
                line.toFront();
                Logger.getGlobal().info("line added");
            } catch (OutputException e) {
                e.printStackTrace();
            }
        });

        DoubleProperty xValueOut = new SimpleDoubleProperty();
        xValueOut.bind(out.getParent().layoutXProperty());
        xValueOut.addListener((observable, oldValue, newValue) ->
                {
                    cableManager.updateOutputX(out);
                 }

        );
        DoubleProperty yValueOut = new SimpleDoubleProperty();
        yValueOut.bind(out.getParent().layoutYProperty());
        yValueOut.addListener((observable, oldValue, newValue) ->{
                    cableManager.updateOutputY(out);

                }

        );

        out.setOnMouseClicked(event -> cableManager.setOutput(out));


    }
}
