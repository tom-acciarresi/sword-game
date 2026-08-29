package it.unicam.cs.mpgc.rpg130730;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the game
 *
 * @author Tommaso Acciarresi
 */
public class Launcher extends Application {
    // #region constants
    public static final String APPLICATION_TITLE = "Sword Game";
    private static final boolean IS_RESIZABLE = false;
    // #endregion

    @Override
    public void start(@Nullable Stage defaultStage) {
        // Load assets
        // TODO async
        AssetLibrary.initialize();

        // Create window
        Stage stage = new Stage();
        initializeStage(stage);

        // Create tree with SceneManager as root
        stage.setScene(new Scene(SceneManager.getInstance()));

        // Load main menu
        SceneManager.getInstance().loadMainMenu();

        // Adds title bar to window height
        stage.sizeToScene();

        // Show game window
        stage.show();
    }

    public static void saveAndQuit() {
        SaveSystem.save();
        Platform.exit();
    }

    public static void quitWithoutSaving() {
        Platform.exit();
    }

    private void initializeStage(Stage stage) {
        // Set Window Settings
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);

        // Set icon
        stage.getIcons().add(AssetLibrary.APP_ICON);
    }
}
