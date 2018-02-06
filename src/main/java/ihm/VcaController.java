package ihm;

import controller.Obseurveur;
import controller.SubjectVCA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import module.PortType;
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
    Circle out;
    
    private int minValue = -5;
    private int initialValue = 0;
    private int maxValue = 5;
    private CableManager cableManager;
    private Obseurveur<SubjectVCA> vcaObseurveur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(minValue, maxValue,
                initialValue);
        gainSelector.setValueFactory(valueFactory);
        gainSelector.setEditable(true);
        cableManager = CableManager.getInstance();






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
