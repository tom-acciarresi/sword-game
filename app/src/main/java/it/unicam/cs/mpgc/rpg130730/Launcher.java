package it.unicam.cs.mpgc.rpg130730;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.environment.TileGrid;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.stage.Stage;

public class Launcher extends javafx.application.Application {
    // #region constants
    private static final boolean IS_RESIZABLE = false;
    public static final String APPLICATION_TITLE = "Sword Game";

    public static final Vector2 LEVEL_SIZE = TileGrid.TILEMAP_DIMENSIONS.scalar(TileGrid.TILE_SIZE);
    public static final Vector2 LEVEL_CENTER = new Vector2(
            LEVEL_SIZE.x() / 2 - TileGrid.TILE_SIZE / 2,
            LEVEL_SIZE.y() / 2 - TileGrid.TILE_SIZE / 2);

    public static final int TARGET_FRAMERATE = 60;
    // #endregion

    private static Stage stage = new Stage();
    private static SceneManager sceneManager = new SceneManager();

    // #region get-set
    public static SceneManager getSceneManager() {
        return sceneManager;
    }
    // #endregion

    @Override
    public void start(@org.jspecify.annotations.Nullable Stage defaultStage) {
        // Load assets
        AssetLibrary.initialize();

        // Create window
        initializeStage();

        // Create tree with SceneManager as root
        stage.setScene(new javafx.scene.Scene(sceneManager));

        // Start game loop
        GameLoop.initialize();

        // Load main menu
        sceneManager.loadMainMenu();

        // Adds title bar to window height
        stage.sizeToScene();

        // Show game window
        stage.show();
    }

    public static void saveAndQuit() {
        SaveSystem.save();
        javafx.application.Platform.exit();
    }

    public static void quitWithoutSaving() {
        javafx.application.Platform.exit();
    }

    private void initializeStage() {
        // Set Window Settings
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);

        // Set icon
        stage.getIcons().add(AssetLibrary.APP_ICON);
    }
}
