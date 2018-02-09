package ihm;

import controller.Obseurveur;
import controller.SubjectOutput;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import utils.CableManager;
import utils.PortType;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModuleOut implements Initializable, SubjectOutput{

	@FXML
	Pane paneMain;

	@FXML
	Spinner<Integer> btnAttenuateur;

	@FXML
	CheckBox checkboxMute;

	@FXML
	CheckBox checkbox_record;

	@FXML
	TextField filename_record;

	@FXML
	Circle drawInput;

	static final int INITIAL_VALUE = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
				new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
						INITIAL_VALUE);

		valueFactory.amountToStepByProperty().setValue(20);

		btnAttenuateur.setValueFactory(valueFactory);
		btnAttenuateur.setEditable(true);
		obseurveurList = new ArrayList<>();

		checkboxMute.setSelected(false);
		checkboxMute.setOnAction(event -> notifyObseurveur());

		checkbox_record.setSelected(false);
		checkbox_record.setOnAction(event -> notifyRecordObseurveur());

		btnAttenuateur.setOnInputMethodTextChanged(event -> notifyObseurveur());
		btnAttenuateur.setOnKeyReleased(e ->notifyObseurveur());
		btnAttenuateur.setOnMouseClicked(e -> onClickAttenuateur(valueFactory));

	}

	private void onClickAttenuateur(SpinnerValueFactory.IntegerSpinnerValueFactory f){
		int newStep = (Math.abs(f.getValue())/10)+1;
		f.amountToStepByProperty().setValue(newStep);
		notifyObseurveur();
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
	public boolean getRecordEnabled() {
		return checkbox_record.isSelected();
	}

	@Override
	public String getRecordFilename() {
		return filename_record.getText();
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

	public void notifyRecordObseurveur() {
		File fileExists = new File(this.getRecordFilename());

		if (this.getRecordEnabled() && fileExists.exists()) { // Si on veux enregistrer, on vérifie qu'un fichier de sortie n'existe pas déjà
			Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setHeaderText("Le fichier de sortie \"" + this.filename_record.getText() + "\" existe déjà. Voulez-vous l'écraser ?");
			confirm.getButtonTypes().add(ButtonType.CANCEL);
			confirm.setTitle("Fichier de sortie existant");
			confirm.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					for(Obseurveur<SubjectOutput> o : obseurveurList){
						o.update(this);
					}
				}
				else {
					this.checkbox_record.setSelected(false);
				}
			});
		}
		else {
			for(Obseurveur<SubjectOutput> o : obseurveurList){
				o.update(this);
			}
		}
	}
}