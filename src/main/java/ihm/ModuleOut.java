package ihm;

import ihm.observer.Obseurveur;
import ihm.observer.SubjectOutput;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sauvegarde.SavedModule;
import sauvegarde.SavedModuleOut;
import utils.CableManager;
import utils.PortType;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModuleOut implements Initializable, SubjectOutput, SuperController{

	@FXML
	Pane paneMain;

	@FXML
	Spinner<Integer> btnAttenuateur;

	@FXML
	CheckBox checkboxMute;

	@FXML
	CheckBox checkboxRecord;

	@FXML
	TextField filenameRecord;

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

		btnAttenuateur.setValueFactory(valueFactory);
		btnAttenuateur.setEditable(true);
		obseurveurList = new ArrayList<>();

		checkboxMute.setSelected(false);
		checkboxMute.setOnAction(event -> notifyObseurveur());

		checkboxRecord.setSelected(false);
		checkboxRecord.setOnAction(event -> notifyRecordObseurveur());

		btnAttenuateur.setOnInputMethodTextChanged(event -> notifyObseurveur());
		btnAttenuateur.setOnKeyReleased(e ->notifyObseurveur());
		btnAttenuateur.setOnMouseClicked(e -> onClickAttenuateur(valueFactory));

	}

	private void onClickAttenuateur(SpinnerValueFactory.IntegerSpinnerValueFactory f){
		if (f.getValue() > 0) {
			f.amountToStepByProperty().setValue(1);
		}
		else {
			f.amountToStepByProperty().setValue((Math.abs(f.getValue()) / 10) + 1);
		}
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
		return checkboxRecord.isSelected();
	}

	@Override
	public String getRecordFilename() {
		return filenameRecord.getText();
	}

	@Override
	public void loadProperties(SavedModule module) {
		SavedModuleOut savedModuleOut = (SavedModuleOut) module;
		btnAttenuateur.getValueFactory().setValue(savedModuleOut.getAttenuateur().intValue());
		checkboxMute.setSelected(savedModuleOut.isMute());
		checkboxRecord.setSelected(savedModuleOut.isRecording());
		filenameRecord.setText(savedModuleOut.getRecordFileName());
		notifyObseurveur();
	}

	@Override
	public Circle getPort(PortType portType) {
		if (portType.equals(PortType.INPUT)) {
			return drawInput;
		}
		return null;
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

		if (this.getRecordEnabled() && fileExists.exists()) { // SI on veux enregistrer, on vérifie qu'un fichier de sortie n'existe pas déjà
			Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setHeaderText("Le fichier de sortie \"" + this.filenameRecord.getText() + "\" existe déjà. Voulez-vous l'écraser ?");
			confirm.getButtonTypes().add(ButtonType.CANCEL);
			confirm.setTitle("Fichier de sortie existant");
			confirm.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					for(Obseurveur<SubjectOutput> o : obseurveurList){
						o.update(this);
					}
				}
				else {
					this.checkboxRecord.setSelected(false);
				}
			});
		}
		else {
			for(Obseurveur<SubjectOutput> o : obseurveurList){
				o.update(this);
			}
		}
	}

	@Override
	public SavedModule createMemento() {
		return new SavedModuleOut(paneMain.getLayoutX(), paneMain.getLayoutY(),
				checkboxMute.isSelected(), filenameRecord.getText(),
				checkboxRecord.isSelected(), btnAttenuateur.getValue());
	}
}