package ihm;

import controller.Controller;
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
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import module.Oscilloscope;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.*;

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

    @FXML
    private Button delete;

    @FXML
    private Spinner<Integer> refresh;

    @FXML
    private Spinner<Integer> spinnerAxisX;

    @FXML
    private Spinner<Integer> spinnerAxisY;

    private int defaultRefreshTime = 500;

    private int minRefreshTime = 50;

    private int maxRefreshTime = 5000;

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
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryX =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(100, Oscilloscope.TMAX, Oscilloscope.TMAX);

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryY =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 14);

        spinnerAxisX.setValueFactory(valueFactoryX);
        spinnerAxisY.setValueFactory(valueFactoryY);

        valueFactoryX.amountToStepByProperty().setValue(50);

        spinnerAxisY.setOnInputMethodTextChanged(event -> updateAxisY());
        spinnerAxisY.setOnKeyReleased(e ->updateAxisY());
        spinnerAxisY.setOnMouseClicked(e -> updateAxisY());
        spinnerAxisY.setEditable(true);

        spinnerAxisX.setOnInputMethodTextChanged(event -> updateAxisX());
        spinnerAxisX.setOnKeyReleased(e ->updateAxisX());
        spinnerAxisX.setOnMouseClicked(e -> updateAxisX());
        spinnerAxisX.setEditable(true);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(Oscilloscope.TMAX);
        xAxis.setTickUnit(50);

        xAxis.setSide(Side.BOTTOM);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(-14);
        yAxis.setUpperBound(14);
        yAxis.setTickUnit(0.1);
        yAxis.setSide(Side.LEFT);

        lineChart.setCreateSymbols(false);
        lineChart.setAxisSortingPolicy( LineChart.SortingPolicy.NONE);

        lineChart.setAnimated(false);

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryRefresh =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minRefreshTime, maxRefreshTime, defaultRefreshTime);
        valueFactoryRefresh.amountToStepByProperty().setValue(20);

        timeline = new Timeline();

        refresh.setValueFactory(valueFactoryRefresh);
        refresh.setEditable(true);
        refresh.setOnInputMethodTextChanged(event -> updateRefreshTime());
        refresh.setOnKeyReleased(e ->updateRefreshTime());
        refresh.setOnMouseClicked(e -> updateRefreshTime());

        delete.setOnMouseClicked(eh -> Controller.getInstance().removeWithConfirmPopup(oscilloObseurveur, border));
    }

    private void updateAxisY() {
        yAxis.setLowerBound(-spinnerAxisY.getValue());
        yAxis.setUpperBound(spinnerAxisY.getValue());
    }

    private void updateAxisX(){
        xAxis.setUpperBound(spinnerAxisX.getValue());
    }

    private void updateRefreshTime(){
        timeline.stop();
        timeline = new Timeline(new KeyFrame(
                Duration.millis(refresh.getValue()),
                ae -> oscilloObseurveur.update(this)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            oscilloObseurveur = o;
            CableManager.getInstance().addListener(in, oscilloObseurveur.getReference(), PortType.INPUT, border);
            CableManager.getInstance().addListener(out, oscilloObseurveur.getReference(), PortType.OUTPUT, border);
            updateRefreshTime();
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
        lineChart.getData().clear();
                XYChart.Series<Integer, Double> serie = new XYChart.Series<>();
        serie.setName("Values");
        for(int i = 0 ; i < series.length ; i++){
            serie.getData().add(new XYChart.Data(i, series[i]));
        }
        lineChart.getData().add(serie);
    }

}
