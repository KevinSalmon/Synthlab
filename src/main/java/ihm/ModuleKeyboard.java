package ihm;

import controller.Obseurveur;
import controller.SubjectKeyboard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sauvegarde.SavedModule;
import utils.CableManager;
import utils.PortType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModuleKeyboard implements Initializable, SubjectKeyboard, SuperController {

	@FXML
	Pane paneMain;

	private List<Obseurveur<SubjectKeyboard>> obseurveurList;

	@FXML
	Circle drawOutput;

	static final int INITIAL_VALUE = 0;
	int minValue = Integer.MIN_VALUE;
	int maxValue = 12;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
				new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue,
						INITIAL_VALUE);

		valueFactory.amountToStepByProperty().setValue(20);

		obseurveurList = new ArrayList<>();

	}


	@Override
	public void register(Obseurveur o) {
		if(o != null){
			obseurveurList.add(o);
			CableManager cableManager;
			cableManager = CableManager.getInstance();
			cableManager.addListener(drawOutput, o.getReference(), PortType.OUTPUT, paneMain);
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
		for(Obseurveur<SubjectKeyboard> o : obseurveurList){
			o.update(this);
		}
	}

	@Override
	public SavedModule createMemento() {
		return new SavedModule(paneMain.getLayoutX(), paneMain.getLayoutY());
	}

	@Override
	public void loadProperties(SavedModule module) {

	}

	@Override
	public Circle getPort(PortType portType) {
		if (portType.equals(PortType.OUTPUT)) { return this.drawOutput; }
		return null;
	}
}