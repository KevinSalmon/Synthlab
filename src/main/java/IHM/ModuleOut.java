package IHM;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import Exceptions.OutputException;
import controller.Obseurveur;
import controller.SubjectOutput;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import module.Module;
import utils.CableManager;

import javax.swing.event.ChangeEvent;


public class ModuleOut implements Initializable, SubjectOutput{

	@FXML
	Pane pane_main;

	@FXML
	Spinner<Integer> btn_attenuateur;
	
	@FXML
	CheckBox checkbox_mute;

	@FXML
	Circle draw_input;

	private Line line;

	final int initialValue = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;
	private CableManager cableManager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Value factory.
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
				initialValue);

		btn_attenuateur.setValueFactory(valueFactory);
		btn_attenuateur.setEditable(true);
        obseurveurList = new ArrayList<>();
		cableManager = CableManager.getInstance();

		DoubleProperty xValue = new SimpleDoubleProperty();
		xValue.bind(draw_input.getParent().layoutXProperty());
		xValue.addListener((observable, oldValue, newValue) -> cableManager.updateInputX(draw_input));

		DoubleProperty yValue = new SimpleDoubleProperty();
		yValue.bind(draw_input.getParent().layoutYProperty());
		yValue.addListener((observable, oldValue, newValue) -> cableManager.updateInputY(draw_input));
		draw_input.setOnMouseClicked(event -> {
			try {
				line = cableManager.setInput(draw_input);
				((Pane) pane_main.getParent()).getChildren().add(line);
				line.toFront();
				Logger.getGlobal().info("line added");
			} catch (OutputException e) {
				e.printStackTrace();
			}
		});
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
