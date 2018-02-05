package IHM;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.Obseurveur;
import controller.SubjectOutput;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import module.Module;

import javax.swing.event.ChangeEvent;


public class ModuleOut implements Initializable, SubjectOutput{

	@FXML
	Pane pane_main;

	@FXML
	Spinner<Integer> btn_attenuateur;
	
	@FXML
	CheckBox checkbox_mute;

	final int initialValue = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Value factory.
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
				initialValue);

		btn_attenuateur.setValueFactory(valueFactory);
		btn_attenuateur.setEditable(true);
        obseurveurList = new ArrayList<>();

        checkbox_mute.setOnAction(event -> notifyObseurveur());
        btn_attenuateur.setOnInputMethodTextChanged(event -> notifyObseurveur());
        btn_attenuateur.setOnKeyReleased(e ->notifyObseurveur());
        btn_attenuateur.setOnMouseClicked(e -> notifyObseurveur());
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
        return checkbox_mute.isSelected();
    }

    @Override
    public double getDecibelValue() {
        return btn_attenuateur.getValue();
    }

    @Override
    public void register(Obseurveur o) {
        if(o != null){
            obseurveurList.add(o);
        }
    }

    @Override
    public void remove(Obseurveur o) {

    }

    @Override
    public void notifyObseurveur() {
        for(Obseurveur<SubjectOutput> o : obseurveurList){
            o.update(this);
        }
    }
}
