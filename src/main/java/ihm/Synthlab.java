package ihm;

import controller.Controller;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.FxmlFilesNames;

import java.io.IOException;
import java.net.URL;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Synthlab extends Application {
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 500;
    private static final int SPLASH_HEIGHT = 500;

    public static void main(String[] args) throws Exception { launch(args); }

    @Override public void init() {
        ImageView splash = new ImageView(new Image("/style/logo_synthlab.png"));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label("Shiba is building your synthetiser . . .");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle("-fx-padding: 5; -fx-background-color: cornsilk; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, chocolate, derive(chocolate, 50%));");
        splashLayout.setEffect(new DropShadow());
    }

    @Override public void start(final Stage initStage) throws Exception {
        showSplash(initStage);
        Thread t = new Thread(() -> {
                load();
                if (initStage.isShowing()) {
                    loadProgress.progressProperty().unbind();
                    loadProgress.setProgress(1);
                    progressText.setText("Shiba is a good boy.");
                    mainStage.setIconified(false);
                    initStage.toFront();
                    FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                    fadeSplash.setFromValue(1.0);
                    fadeSplash.setToValue(0.0);
                    fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            initStage.hide();

                            mainStage.show();

                        }
                    });
                    fadeSplash.play();
                }

        });
        Platform.runLater(t);
    }

    private void showSplash(Stage initStage) {
        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.getIcons().add(new Image("Synthlab.ico"));
        initStage.setIconified(true);
        initStage.show();
    }

    private void load(){
        Scene scene;
        mainStage = new Stage();
        /**
         * Chargement du FXML
         */
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getResource(FxmlFilesNames.MAIN);
        Parent root = null;
        try {
            root = loader.load(location.openStream());
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }

        /**
         * Parametres de la fenetre
         */
        mainStage.setTitle("SynthLab");
        mainStage.setMaximized(true);
        mainStage.setMinWidth(640.0);
        mainStage.setMinHeight(480.0);
        /**
         * Affichage de la fenetre
         */
        scene = new Scene(root);


        mainStage.setScene(scene);

        mainStage.setOnCloseRequest(eh -> System.exit(0));
        mainStage.getIcons().add(new Image("Synthlab.ico"));
        mainStage.setIconified(true);
        Controller controller = Controller.getInstance();
        controller.setScene(scene);
        controller.setIhmController(loader.getController());
    }
}