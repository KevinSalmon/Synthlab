package IHM;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.net.URL;

public class IHMImpl extends Application {

    private Scene scene;

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getResource("/mainFXML.fxml");
        Parent root = loader.load(location.openStream());

        primaryStage.setTitle("Drag and Drop funny stuff");
        primaryStage.setMaximized(true);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
