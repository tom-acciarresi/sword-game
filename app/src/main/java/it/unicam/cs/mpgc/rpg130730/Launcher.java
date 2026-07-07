package it.unicam.cs.mpgc.rpg130730;

import java.io.IOException;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.entities.Enemy;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.LevelData;
import it.unicam.cs.mpgc.rpg130730.ui.GUI;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Launcher extends Application {
    private static final boolean IS_RESIZABLE = false;
    private static final String APPLICATION_TITLE = "Sword Game";

    public static final int TILE_SIZE = 64;
    public static final Vector2 TILEMAP_DIMENSIONS = new Vector2(12, 10);

    public static final Vector2 WINDOW_SIZE = new Vector2(768, 640);
    public static final Vector2 WINDOW_CENTER = new Vector2(
            Launcher.WINDOW_SIZE.x() / 2 - TILE_SIZE / 2,
            Launcher.WINDOW_SIZE.y() / 2 - TILE_SIZE / 2);
    public static final int TARGET_FRAMERATE = 60;

    private static final Stage stage = new Stage();
    private static final SceneManager sm = new SceneManager();

    private static GUI gui = new GUI();

    @Override
    public void start(@Nullable Stage defaultStage) throws IOException {
        initializeStage();

        AssetLibrary.initialize();
        InputMap.initialize(stage);
        GameLoop.initialize(stage);

        loadFirstScene();

        // Adds title bar to window height
        stage.sizeToScene();

        stage.show();
    }

    private void initializeStage() {
        // Set Window Settings
        stage.setWidth(WINDOW_SIZE.x());
        stage.setHeight(WINDOW_SIZE.y() + GUI.GUI_HEIGHT);
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(IS_RESIZABLE);

        // Set icon
        stage.getIcons().add(AssetLibrary.GAME_ICON);

        // Create tree with SceneManager as root
        stage.setScene(new Scene(sm));
    }

    private void loadFirstScene() throws IOException {
        // Instance tiles
        Tilemap tilemap = new Tilemap(LevelData.ROOM_1);
        tilemap.setLayoutY(GUI.GUI_HEIGHT);

        // Instance enemy
        Enemy pig_enemy = new Enemy(Enemy.EnemyType.PIG, new Vector2(2, 3).scalar(TILE_SIZE));
        pig_enemy.setLayoutY(GUI.GUI_HEIGHT);

        // Instance player
        Player player = new Player(new Vector2(3, 5).scalar(TILE_SIZE));
        player.setLayoutY(GUI.GUI_HEIGHT);

        sm.add(gui);
        sm.add(tilemap);
        sm.add(pig_enemy);
        sm.add(player);
    }

    public static GUI getGUI() {
        return gui;
    }
}
