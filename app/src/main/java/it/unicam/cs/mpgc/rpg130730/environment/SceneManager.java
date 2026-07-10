package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Map;
import java.util.Map.Entry;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.entities.Enemy;
import it.unicam.cs.mpgc.rpg130730.entities.Enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.tools.LevelEditor.LevelData;
import it.unicam.cs.mpgc.rpg130730.ui.GUI;
import it.unicam.cs.mpgc.rpg130730.ui.MainMenu;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.Group;

public final class SceneManager extends Group {
    public static enum Level {
        ROOM_1("level1.dat"),
        ROOM_2("level2.dat");

        private final String filename;

        Level(String filename) {
            this.filename = filename;
        }

        public String filename() {
            return filename;
        }
    }

    // TODO change
    public void loadMainMenu() {
        getChildren().add(new MainMenu());
    }

    public void loadFirstLevel() {
        LevelData FIRST_LEVEL = AssetLibrary.getLevelData(Level.ROOM_2.filename());
        Tilemap tilemap = new Tilemap(FIRST_LEVEL.tileArrangementData());

        tilemap.setLayoutY(GUI.GUI_SIZE.y());

        Map<Vector2, EnemyType> enemyData = FIRST_LEVEL.enemyData();
        Enemy[] enemyArr = new Enemy[enemyData.size()];
        int i = 0;
        for (Entry<Vector2, EnemyType> enemy : enemyData.entrySet()) {
            Vector2 enemyPos = enemy.getKey();
            EnemyType enemyType = enemy.getValue();
            @SuppressWarnings("null")
            Enemy e = new Enemy(enemyType, enemyPos.scalar(Tilemap.TILE_SIZE));
            e.setLayoutY(GUI.GUI_SIZE.y());
            enemyArr[i++] = e;
        }

        Player player = new Player(new Vector2(3, 5).scalar(Tilemap.TILE_SIZE));
        player.setLayoutY(GUI.GUI_SIZE.y());

        GUI gui = new GUI(player);

        getChildren().add(gui);
        getChildren().add(tilemap);
        getChildren().addAll(enemyArr);
        getChildren().add(player);
    }

    public void loadLevel(Level level) {
        // TODO implement
    }
}
