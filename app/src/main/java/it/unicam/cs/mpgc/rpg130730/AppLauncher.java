package it.unicam.cs.mpgc.rpg130730;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AppLauncher extends Application {
    // #region Constants
    public static final String APPLICATION_TITLE = "New Game";
    public static final boolean IS_FULLSCREEN = false;
    public static final boolean IS_RESIZABLE = true;
    public static final int WINDOW_WIDTH = 768;
    public static final int WINDOW_HEIGHT = 640;
    private static final String ICON_FILENAME = "images/icon.png";
    // #endregion

    @Override
    public void start(Stage stage) throws IOException {
        // Set Window Settings
        stage.setTitle(APPLICATION_TITLE);
        stage.setFullScreen(IS_FULLSCREEN);
        stage.setResizable(IS_RESIZABLE);
        // Set icon
        stage.getIcons().add(new Image(getClass().getResource(ICON_FILENAME).toExternalForm()));

        // double scalingFactor =
        // Toolkit.getDefaultToolkit().getScreenSize().getHeight() / WINDOW_HEIGHT;
        // if (scalingFactor < 1)
        // throw new IllegalStateException("Screen size too small");

        SceneManager sceneManager = SceneManager.get_instance();
        Scene scene = new Scene(sceneManager);

        stage.setScene(scene);

        Node tilesetScene = FXMLLoader.load(getClass().getResource("scenes/tilegrid.fxml"));

        sceneManager.addNode(tilesetScene);

        stage.sizeToScene();

        stage.show();

        ImageView tempPlayer = FXMLLoader.load(getClass().getResource("scenes/player.fxml"));
        tempPlayer.setImage(
                new Image(getClass().getResource("images/player.png").toExternalForm(), 64.0, 64.0, true, false));
        tempPlayer.setTranslateX((WINDOW_WIDTH / 2) - (64 / 2));
        tempPlayer.setTranslateY((WINDOW_HEIGHT / 2) - (64 / 2));
        sceneManager.addNode(tempPlayer);

        stage.setForceIntegerRenderScale(true);
    }
}
