package it.unicam.cs.mpgc.rpg130730;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppLauncher extends Application {
    public static final boolean IS_FULLSCREEN = false, IS_RESIZABLE = true;
    public static final int WINDOW_WIDTH = 768, WINDOW_HEIGHT = 640;
    public static final String APPLICATION_TITLE = "New Game", ICON_FILENAME = "images/icon.png";

    public static ArrayList<Updatable> objectsToUpdate;

    public AppLauncher() {
        objectsToUpdate = new ArrayList<Updatable>();
    }

    @Override
    public void start(Stage stage) throws IOException {
        setSettings(stage);

        SceneManager sceneManager = instantiateSceneManager(stage);

        loadFirstScene(stage, sceneManager);

        startLoop(stage);

        stage.show();
    }

    private void setSettings(Stage stage) {
        // Set Window Settings
        stage.setTitle(APPLICATION_TITLE);
        stage.setFullScreen(IS_FULLSCREEN);
        stage.setResizable(IS_RESIZABLE);
        // Set icon
        stage.getIcons().add(new Image(getClass().getResource(ICON_FILENAME).toExternalForm()));
    }

    private SceneManager instantiateSceneManager(Stage stage) {
        SceneManager sceneManager = SceneManager.get_instance();
        Scene scene = new Scene(sceneManager);
        stage.setScene(scene);
        return sceneManager;
    }

    private void loadFirstScene(Stage stage, SceneManager sceneManager) throws IOException {
        // TODO: Change
        // Add tiles
        sceneManager.addNode(FXMLLoader.load(getClass().getResource("scenes/tilegrid.fxml")));
        stage.sizeToScene();

        // Add player
        ImageView tempPlayer = FXMLLoader.load(getClass().getResource("scenes/player.fxml"));
        // Center player
        tempPlayer.setTranslateX((WINDOW_WIDTH / 2) - (64 / 2));
        tempPlayer.setTranslateY((WINDOW_HEIGHT / 2) - (64 / 2));
        sceneManager.addNode(tempPlayer);
    }

    private void startLoop(Stage stage) {
        Timeline loop = new Timeline(new KeyFrame(Duration.millis(1000.0 / 60), e -> update(1000.0 / 60)));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    private void update(double timeDelta) {
        for (Updatable object : objectsToUpdate) {
            if (object != null)
                object.update(timeDelta);
            else
                System.err.println("Null updatable object");
        }
    }
}
