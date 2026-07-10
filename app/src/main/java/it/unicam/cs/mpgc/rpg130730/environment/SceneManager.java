package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
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

    private static Tilemap tilemap = new Tilemap();
    private static Set<Enemy> enemies = new HashSet<Enemy>();
    private static Player player;

    public void loadMainMenu() {
        getChildren().add(new MainMenu());
    }

    public void initialize(Level level) {
        player = new Player();

        loadLevel(level);

        Group levelContainer = new Group(tilemap, player);
        levelContainer.getChildren().addAll(enemies);
        levelContainer.setLayoutY(GUI.GUI_SIZE.y());

        this.getChildren().addAll(levelContainer, new GUI(player));
    }

    public void newGame() {
        initialize(Level.ROOM_1);
    }

    public void continueGame(
    // SaveData savedata
    ) {
        initialize(
                // savedata.level();
                Level.ROOM_2);

        // Other savedata stuffs
    }

    private void loadTiles(LevelData levelData) {
        tilemap.changeTilemapTo(levelData.tileArrangementData());
    }

    private void loadEnemies(Map<Vector2, EnemyType> enemyData) {
        // Delete old enemies
        for (Enemy enemy : enemies) {
            enemy.unsubscribeFromUpdates();
            CollisionHandler.removeEnemy(enemy);
            this.getChildren().remove(enemy);
        }
        enemies.clear();

        // Load new enemies
        enemyData.entrySet().stream().forEach(e -> {
            EnemyType type = e.getValue();
            Vector2 pos = e.getKey();
            Enemy newEnemy = new Enemy(type, pos.scalar(Tilemap.TILE_SIZE));
            CollisionHandler.addEnemy(newEnemy);
            enemies.add(newEnemy);
        });
    }

    public void loadLevel(Level level) {
        LevelData levelData = AssetLibrary.getLevelData(level.filename());

        loadTiles(levelData);
        loadEnemies(levelData.enemyData());
    }
}
