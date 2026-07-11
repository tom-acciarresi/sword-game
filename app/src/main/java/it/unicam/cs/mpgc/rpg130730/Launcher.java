package it.unicam.cs.mpgc.rpg130730;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Launcher extends Application {
    private static final boolean IS_RESIZABLE = false;
    public static final String APPLICATION_TITLE = "Sword Game";

    public static final Vector2 LEVEL_SIZE = new Vector2(768, 640);
    public static final Vector2 LEVEL_CENTER = new Vector2(
            Launcher.LEVEL_SIZE.x() / 2 - Tilemap.TILE_SIZE / 2,
            Launcher.LEVEL_SIZE.y() / 2 - Tilemap.TILE_SIZE / 2);

    public static final int TARGET_FRAMERATE = 60;

    private static final Stage stage = new Stage();

    private static final SceneManager sceneManager = new SceneManager();

    public static SceneManager getSceneManager() {
        return sceneManager;
    }

    @Override
    public void start(@Nullable Stage defaultStage) {
        // Load assets
        AssetLibrary.initialize();

        // Create window
        initializeStage();

        // Create tree with SceneManager as root
        stage.setScene(new Scene(sceneManager));

        // Start reading input
        InputMap.initialize(stage);

        // Start game loop
        GameLoop.initialize();

        // Load main menu
        sceneManager.loadMainMenu();

        // Adds title bar to window height
        stage.sizeToScene();

        // Show game window
        stage.show();
    }

    private void initializeStage() {
        // Set Window Settings
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);
        // stage.setWidth(LEVEL_SIZE.x());
        // stage.setHeight(LEVEL_SIZE.y() + GUI.GUI_SIZE.y());

        // Set icon
        stage.getIcons().add(AssetLibrary.APP_ICON);
    }

    public static void quit() {
        SaveSystem.save();
        Platform.exit();
    }
}
