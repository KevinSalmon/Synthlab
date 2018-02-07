package ihm;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import utils.FxmlFilesNames;

import java.net.URL;

public class IHMImpl extends Application {

    public void start(Stage primaryStage) throws Exception {
        Scene scene;

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

        primaryStage.setOnCloseRequest(eh -> System.exit(0));

        primaryStage.show();

        new Controller(loader.getController());
    }

    public static void main(String[] args){
        launch(args);
    }
}
