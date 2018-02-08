package ihm;

import controller.Obseurveur;
import controller.SubjectOscillo;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import module.Oscilloscope;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Timeline;


public class OscilloController implements Initializable, SubjectOscillo{

    @FXML
    Pane border;

    @FXML
    private LineChart lineChart;

    @FXML
    private Circle in;

    @FXML
    private Circle out;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Line line;

    /**
     * module observateur
     */
    private Obseurveur<SubjectOscillo> oscilloObseurveur;

    /**
     * Thread qui va interroger le module pour récupérer ses valeurs à afficher
     */
    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(Oscilloscope.TMAX);
        xAxis.setTickUnit(200);
        xAxis.setSide(Side.BOTTOM);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(-14);
        yAxis.setUpperBound(14);
        yAxis.setTickUnit(0.1);
        yAxis.setSide(Side.LEFT);

        lineChart.setCreateSymbols(false);
        lineChart.setAxisSortingPolicy( LineChart.SortingPolicy.NONE);

        lineChart.setAnimated(false);
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            oscilloObseurveur = o;
            CableManager.getInstance().addListener(in, oscilloObseurveur.getReference(), PortType.INPUT, border);
            CableManager.getInstance().addListener(out, oscilloObseurveur.getReference(), PortType.OUTPUT, border);
            SubjectOscillo me = this;

            timeline = new Timeline(new KeyFrame(
                    Duration.millis(2500),
                    ae -> oscilloObseurveur.update(this)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    @Override
    public void remove(Obseurveur o) {
        oscilloObseurveur = null;
    }

    @Override
    public void notifyObseurveur() {
        if(oscilloObseurveur != null){
            oscilloObseurveur.update(this);
        }
    }

    @Override
    public void receiveSeries(double[] series) {
        XYChart.Series<Integer, Double> serie = new XYChart.Series<>();
        serie.setName("Values");
        for(int i = 0 ; i < series.length ; i++){
            serie.getData().add(new XYChart.Data(i, series[i]));
        }

        lineChart.getData().clear();
        lineChart.getData().add(serie);
    }

}
