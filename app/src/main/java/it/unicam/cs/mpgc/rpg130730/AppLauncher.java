package it.unicam.cs.mpgc.rpg130730;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.util.SceneManager;
import it.unicam.cs.mpgc.rpg130730.util.Updatable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Creates application window and populates it with the first scene
 * <p>
 * Contains game loop
 *
 * @author Tommaso Acciarresi
 */
public class AppLauncher extends Application {
    public static final String FILEPATH_PREFIX = "/it/unicam/cs/mpgc/rpg130730/";
    public static final int TILE_SIZE = 64;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 10;

    public static final boolean IS_RESIZABLE = false;
    public static final int WINDOW_WIDTH = 768, WINDOW_HEIGHT = 640;
    public static final int TARGET_FRAMERATE = 60;
    public static final String APPLICATION_TITLE = "New Game", ICON_FILENAME = "images/icon.png";

    private static HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private static ArrayList<Updatable> objectsToUpdate;

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

    public static boolean subscribeToUpdates(Updatable obj) {
        return objectsToUpdate.add(obj);
    }

    public static HashMap<KeyCode, Boolean> getCurrentlyPressedKeys() {
        return keys;
    }

    private void setSettings(Stage stage) {
        // Set Window Settings
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);
        // Set icon
        stage.getIcons().add(new Image(getClass().getResource(ICON_FILENAME).toExternalForm()));
    }

    private SceneManager instantiateSceneManager(Stage stage) {
        SceneManager sceneManager = new SceneManager();
        Scene scene = new Scene(sceneManager);
        stage.setScene(scene);

        // Register key presses
        stage.getScene().setOnKeyPressed(e -> keys.put(e.getCode(), true));
        stage.getScene().setOnKeyReleased(e -> keys.put(e.getCode(), false));

        return sceneManager;
    }

    private void loadFirstScene(Stage stage, SceneManager sceneManager) throws IOException {
        // TODO: Change
        // Add tiles
        sceneManager.addNode(FXMLLoader.load(getClass().getResource("scenes/tilemap.fxml")));
        stage.sizeToScene();

        // Add player
        ImageView tempPlayer = FXMLLoader.load(getClass().getResource("scenes/player.fxml"));
        // Center player
        tempPlayer.setTranslateX((WINDOW_WIDTH / 2) - (64 / 2));
        tempPlayer.setTranslateY((WINDOW_HEIGHT / 2) - (64 / 2));
        sceneManager.addNode(tempPlayer);
    }

    private void startLoop(Stage stage) {
        Timeline loop = new Timeline(
                new KeyFrame(Duration.seconds(1.0 / TARGET_FRAMERATE), e -> update(1.0 / TARGET_FRAMERATE)));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    /**
     * Called 60 times per second (hopefully) at `timeDelta` intervals
     */
    private void update(double timeDelta) {
        updateObjects(timeDelta);
        // System.out.println(keys);
    }

    private void updateObjects(double timeDelta) {
        for (Updatable object : objectsToUpdate) {
            if (object != null)
                object.update(timeDelta);
            else
                System.err.println("Null updatable object");
        }
    }
}
