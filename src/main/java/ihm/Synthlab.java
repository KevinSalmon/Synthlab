package ihm;

import controller.Controller;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.w3c.dom.Document;
import utils.FxmlFilesNames;

import java.io.IOException;
import java.net.URL;

public class Synthlab extends Application {
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private WebView webView;
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
        showMainStage();

        webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override public void changed(ObservableValue<? extends Document> observableValue, Document document, Document document1) {
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
                            showAppli(initStage);
                        }
                    });
                    fadeSplash.play();
                }
            }
        });
    }

    private void showMainStage() {
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("FX Experience");
        mainStage.setIconified(true);

        // create a WebView.
        webView = new WebView();
        webView.getEngine().load("http://fxexperience.com/");
        loadProgress.progressProperty().bind(webView.getEngine().getLoadWorker().workDoneProperty().divide(100));

        // layout the scene.
        Scene scene = new Scene(webView, 1000, 600);
        webView.prefWidthProperty().bind(scene.widthProperty());
        webView.prefHeightProperty().bind(scene.heightProperty());
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void showSplash(Stage initStage) {
        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();
    }

    private void showAppli(Stage initStage){
        Scene scene;

        /**
         * Chargement du FXML
         */
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getResource(FxmlFilesNames.MAIN);
        Parent root = null;
        try {
            root = loader.load(location.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Parametres de la fenetre
         */
        initStage.setTitle("SynthLab");
        initStage.setMaximized(true);
        initStage.setMinWidth(640.0);
        initStage.setMinHeight(480.0);
        /**
         * Affichage de la fenetre
         */
        scene = new Scene(root);

        initStage.setScene(scene);

        primaryStage.setOnCloseRequest(eh -> System.exit(0));
        primaryStage.getIcons().add(new Image("Synthlab.ico"));
        primaryStage.show();

        Controller controller = Controller.getInstance();
        controller.setScene(scene);
        controller.setIhmController(loader.getController());
    }
}