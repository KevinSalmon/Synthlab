package ihm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import module.Oscilloscope;

import java.net.URL;
import java.util.ResourceBundle;

public class OscilloController implements Initializable {

    @FXML
    private LineChart screen;

    @FXML
    private Circle in;

    @FXML
    private Circle out;

    private Line line;

    public OscilloController(Oscilloscope osc) {


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
