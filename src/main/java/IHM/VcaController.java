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
import module.PortType;
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
        cableManager.addListener(in, PortType.INPUT, pane);
        cableManager.addListener(out, PortType.OUTPUT, pane);
        cableManager = CableManager.getInstance();






    }
}
