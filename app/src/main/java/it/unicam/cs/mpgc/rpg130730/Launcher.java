package it.unicam.cs.mpgc.rpg130730;

import java.io.IOException;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.ui.GUI;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Launcher extends Application {
    private static final boolean IS_RESIZABLE = false;
    public static final String APPLICATION_TITLE = "Sword Game";

    public static final int TILE_SIZE = 64;
    public static final Vector2 TILEMAP_DIMENSIONS = new Vector2(12, 10);

    public static final Vector2 LEVEL_SIZE = new Vector2(768, 640);
    public static final Vector2 LEVEL_CENTER = new Vector2(
            Launcher.LEVEL_SIZE.x() / 2 - TILE_SIZE / 2,
            Launcher.LEVEL_SIZE.y() / 2 - TILE_SIZE / 2);
    public static final int TARGET_FRAMERATE = 60;

    private static final Stage stage = new Stage();
    private static final SceneManager sm = new SceneManager();

    @Override
    public void start(@Nullable Stage defaultStage) throws IOException {
        initializeStage();

        AssetLibrary.initialize();
        InputMap.initialize(stage);
        GameLoop.initialize(stage);

        sm.loadFirstScene();

        // Adds title bar to window height
        stage.sizeToScene();

        stage.show();
    }

    private void initializeStage() {
        // Set Window Settings
        stage.setWidth(LEVEL_SIZE.x());
        stage.setHeight(LEVEL_SIZE.y() + GUI.GUI_SIZE.y());
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);

        // Set icon
        stage.getIcons().add(AssetLibrary.GAME_ICON);

        // Create tree with SceneManager as root
        stage.setScene(new Scene(sm));
    }

    public static SceneManager getSceneManager() {
        return sm;
    }
}
