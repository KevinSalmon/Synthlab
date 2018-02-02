package IHM;

import controller.Controller;
import controller.Subject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import utils.FxmlFilesNames;

import java.net.URL;

public class IHMImpl extends Application {

    private Scene scene;

    public void start(Stage primaryStage) throws Exception {
        /**
         * Chargement du FXML
         */
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getResource(FxmlFilesNames.MAIN);
        Parent root = loader.load(location.openStream());

        /**
         * Parametres de la fenetre
         */
        primaryStage.setTitle("SynthLab");
        primaryStage.setMaximized(true);

        /**
         * Affichage de la fenetre
         */
        scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

        c = new Controller(((IHMController)loader.getController()));
    }

    private Controller c;

    public static void main(String[] args){
        launch(args);
    }
}
