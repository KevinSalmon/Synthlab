package ihm;

import controller.Obseurveur;
import controller.SubjectVCA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import utils.PortType;
import utils.CableManager;

import java.net.URL;
import java.util.ResourceBundle;

public class VcaController implements Initializable, SubjectVCA {

    @FXML
    Pane pane;

    @FXML
    Spinner<Double> gainSelector;

    @FXML
    Circle in;

    @FXML
    Circle am;

    @FXML
    Circle out;
    
    private double minValue = Double.MIN_EXPONENT;
    private double initialValue = 0.0;
    private double maxValue = 0.0;
    private CableManager cableManager;
    private Obseurveur<SubjectVCA> vcaObseurveur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(minValue, maxValue,
                        initialValue,20);

        gainSelector.setValueFactory(valueFactory);
        gainSelector.setEditable(true);
        cableManager = CableManager.getInstance();

        gainSelector.setOnInputMethodTextChanged(event -> notifyObseurveur());
        gainSelector.setOnKeyReleased(e ->notifyObseurveur());
        gainSelector.setOnMouseClicked(e -> onClickAttenuateur(valueFactory));
    }

    private void onClickAttenuateur(SpinnerValueFactory.DoubleSpinnerValueFactory f){
        double newStep = (Math.abs(f.getValue())/10)+1;
        f.amountToStepByProperty().setValue(newStep);
        notifyObseurveur();
    }

    @Override
    public Double getDecibel() {
        return gainSelector.getValue();
    }

    @Override
    public void register(Obseurveur o) {
        if( o != null){
            vcaObseurveur = o;
            cableManager.addListener(in, vcaObseurveur.getReference(), PortType.INPUT, pane);
            cableManager.addListener(am, vcaObseurveur.getReference(), PortType.AM, pane);
            cableManager.addListener(out, vcaObseurveur.getReference(), PortType.OUTPUT, pane);

        }

    }

    @Override
    public void remove(Obseurveur o) {
        if(o != null){
            vcaObseurveur = null;
        }
    }

    @Override
    public void notifyObseurveur() {
        vcaObseurveur.update(this);
    }
}
