package IHM;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class VcaController implements Initializable{

    @FXML
    Pane pane;

    @FXML
    Spinner<Integer> gainSelector;

    @FXML
    Circle in;
    
    private int minValue =0;
    private int initialValue = 0;
    private int maxValue = 100;
    private boolean inputClicked;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
                initialValue);
        gainSelector.setValueFactory(valueFactory);
        gainSelector.setEditable(true);
        this.inputClicked = false;



    }
}
