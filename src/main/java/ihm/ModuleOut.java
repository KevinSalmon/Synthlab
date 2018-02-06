package ihm;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.Obseurveur;
import controller.SubjectOutput;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import utils.PortType;
import utils.CableManager;

public class ModuleOut implements Initializable, SubjectOutput{

	@FXML
	Pane paneMain;

	@FXML
	Spinner<Integer> btnAttenuateur;
	
	@FXML
	CheckBox checkboxMute;

	@FXML
	Circle drawInput;

	final static int INITIAL_VALUE = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Value factory.
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
				INITIAL_VALUE);

		btnAttenuateur.setValueFactory(valueFactory);
		btnAttenuateur.setEditable(true);
        obseurveurList = new ArrayList<>();

        checkboxMute.setOnAction(event -> notifyObseurveur());
        checkboxMute.setSelected(false);
        btnAttenuateur.setOnInputMethodTextChanged(event -> notifyObseurveur());
        btnAttenuateur.setOnKeyReleased(e ->notifyObseurveur());
        btnAttenuateur.setOnMouseClicked(e -> notifyObseurveur());
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getInitialValue() {
		return INITIAL_VALUE;
	}


    private List<Obseurveur<SubjectOutput>> obseurveurList;

    @Override
    public boolean getMuteValue() {
        return checkboxMute.isSelected();
    }

    @Override
    public double getDecibelValue() {
        return btnAttenuateur.getValue();
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseurveurList.add(o);
			CableManager cableManager;
			cableManager = CableManager.getInstance();
			cableManager.addListener(drawInput, o.getReference(), PortType.INPUT, paneMain);
            o.update(this);
        }
    }

    @Override
    public void remove(Obseurveur o) {
		if(o != null){
			obseurveurList.remove(o);
		}
    }

    @Override
    public void notifyObseurveur() {
        for(Obseurveur<SubjectOutput> o : obseurveurList){
            o.update(this);
        }
    }
}
