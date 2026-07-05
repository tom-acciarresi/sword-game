package it.unicam.cs.mpgc.rpg130730;

import java.io.IOException;
import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.entities.Enemy;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Level;
import it.unicam.cs.mpgc.rpg130730.util.AssetRegistry;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.GameLoop;
import it.unicam.cs.mpgc.rpg130730.util.InputMap;
import it.unicam.cs.mpgc.rpg130730.util.InputMap.KeyBind;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Creates application window and populates it with the first scene
 *
 * @author Tommaso Acciarresi
 */
public class Launcher extends Application {
    private static final boolean IS_RESIZABLE = false;
    private static final String APPLICATION_TITLE = "Sword Game";
    private static final Vector2 WINDOW_SIZE = new Vector2(768, 640);
    public static final int TARGET_FRAMERATE = 60;

    public static final int TILE_SIZE = 64;
    public static final Vector2 GRID_DIMENSIONS = new Vector2(12, 10);

    public static final Vector2 WINDOW_CENTER = new Vector2(
            Launcher.WINDOW_SIZE.x() / 2 - TILE_SIZE / 2,
            Launcher.WINDOW_SIZE.y() / 2 - TILE_SIZE / 2);

    private static final String ICON_FILENAME = "/images/icon.png";

    @SuppressWarnings("unused")
    private static final AssetRegistry assetRegistry = new AssetRegistry();

    private static final Stage stage = new Stage();
    private static final SceneManager sceneManager = new SceneManager();

    @Override
    public void start(@Nullable Stage defaultStage) throws IOException {
        setSettings();

        stage.setScene(new Scene(sceneManager));

        setInputListeners();

        loadFirstScene();

        stage.sizeToScene();
        stage.show();

        GameLoop.startLoop(stage);
    }

    private void setSettings() {
        // Set Window Settings
        stage.setWidth(WINDOW_SIZE.x());
        stage.setHeight(WINDOW_SIZE.y());
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);
        // Set icon
        stage.getIcons().add(new CustomImageLoader().loadImage(ICON_FILENAME));
    }

    private void setInputListeners() {
        stage.getScene().setOnKeyPressed(e -> {
            InputMap.getCurrentlyPressedKeys().put(e.getCode(), true);

            if (e.getCode() == KeyBind.QUIT.key())
                Platform.exit();
        });

        stage.getScene().setOnKeyReleased(e -> InputMap.getCurrentlyPressedKeys().put(e.getCode(), false));
    }

    // TODO: Change
    private void loadFirstScene() {
        // Add tiles
        sceneManager.addChild(new Tilemap(Level.ROOM_1));

        // Add enemy
        sceneManager.addChild(new Enemy(Enemy.EnemyType.PIG, WINDOW_CENTER.add(new Vector2(64 * 2, -64 * 3))));

        // Add player
        sceneManager.addChild(new Player(new Vector2(64 * 3, 64 * 5)));
    }
}
