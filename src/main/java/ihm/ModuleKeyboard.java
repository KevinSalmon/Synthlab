package ihm;

import controller.Obseurveur;
import controller.SubjectKeyboard;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import module.Keyboard;
import sauvegarde.SavedKeyboard;
import sauvegarde.SavedModule;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ResourceBundle;

public class ModuleKeyboard implements Initializable, SubjectKeyboard, SuperController {

	@FXML
	Pane paneMain;

	private Obseurveur<SubjectKeyboard> obseurveur;

	@FXML
	Circle drawOutput;

	@FXML
	Label labNote;

	@FXML
	Label labOctave;

	@FXML
	Circle gate;

	static final int INITIAL_VALUE = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;

	private Timeline timeline;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
				new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
						INITIAL_VALUE);

		valueFactory.amountToStepByProperty().setValue(20);

		obseurveur = null;
		timeline = new Timeline();
	}


	@Override
	public void register(Obseurveur o) {
		if(o != null){
			obseurveur = o;
			CableManager cableManager;
			cableManager = CableManager.getInstance();
			cableManager.addListener(drawOutput, o.getReference(), PortType.OUTPUT, paneMain);
			o.update(this);

			timeline.stop();
			timeline = new Timeline(new KeyFrame(
					Duration.millis(100),
					ae -> obseurveur.update(this)));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		}
	}

	@Override
	public void remove(Obseurveur o) {
		timeline.stop();
		obseurveur = null;
	}

	@Override
	public void notifyObseurveur() {
		obseurveur.update(this);
	}

	@Override
	public SavedModule createMemento() {
		return new SavedKeyboard(paneMain.getLayoutX(), paneMain.getLayoutY(), Keyboard.Note.valueOf(labNote.getText()), Integer.parseInt(labOctave.getText()));
	}

	@Override
	public void loadProperties(SavedModule module) {

	}

	@Override
	public Circle getPort(PortType portType) {
		if (portType.equals(PortType.OUTPUT)) { return this.drawOutput; }
		return null;
	}

	@Override
	public void receiveNote(Keyboard.Note note) {
		labNote.setText(note.name());
	}

	@Override
	public void receiveOctave(int octave) {
		labOctave.setText(String.valueOf(octave));
	}
}