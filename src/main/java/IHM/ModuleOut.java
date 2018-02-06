package IHM;

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
import javafx.scene.shape.Line;
import module.PortType;
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

	final static int initialValue = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Value factory.
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
				initialValue);

		btnAttenuateur.setValueFactory(valueFactory);
		btnAttenuateur.setEditable(true);
        obseurveurList = new ArrayList<>();

		CableManager cableManager;
		cableManager = CableManager.getInstance();
		cableManager.addListener(drawInput, PortType.INPUT, paneMain);

        checkboxMute.setOnAction(event -> notifyObseurveur());
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
		return initialValue;
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
