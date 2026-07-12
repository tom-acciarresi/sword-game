package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
import it.unicam.cs.mpgc.rpg130730.entities.Enemy;
import it.unicam.cs.mpgc.rpg130730.entities.Enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem.SaveData;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.ui.GUI;
import it.unicam.cs.mpgc.rpg130730.ui.MainMenu;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.Group;

public class SceneManager extends Group {
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

    // #region constants
    private static final Level INITIAL_LEVEL = Level.ROOM_1;
    // #endregion

    private static Level currLevel = INITIAL_LEVEL;
    private static Tilemap tilemap = new Tilemap();

    @SuppressWarnings("null")
    private static Player player;

    private static Set<Enemy> loadedEnemies = new HashSet<Enemy>();

    private static Group levelContainer = new Group();

    // #region get-set
    public static Level getCurrLevel() {
        return currLevel;
    }

    public static Tilemap getTilemap() {
        return tilemap;
    }

    public static Player getPlayer() {
        return player;
    }
    // #endregion

    public void loadMainMenu() {
        this.getChildren().add(new MainMenu());
    }

    public void newGame() {
        initialize(INITIAL_LEVEL);
    }

    public void continueGame(SaveData savedata) {
        initialize(savedata.level());

        player.setKills(savedata.kills());
        player.setHealth(savedata.health());
    }

    public void initialize(Level level) {
        createLevelContainer();

        loadLevel(level);

        player = new Player();

        levelContainer.getChildren().addAll(tilemap, player);

        this.getChildren().addAll(new GUI(player));

        // Start reading input
        InputMap.initialize(this);
    }

    public void loadLevel(Level level) {
        LevelData levelData = AssetLibrary.getLevelData(level.filename());

        currLevel = level;
        loadTiles(levelData.tileArrangementData());
        loadEnemies(levelData.enemyData());
    }

    private void createLevelContainer() {
        levelContainer.setLayoutY(GUI.GUI_SIZE.y());
        this.getChildren().add(levelContainer);
    }

    private void loadTiles(int[] tileData) {
        tilemap.changeTilemapTo(tileData);
    }

    private void loadEnemies(Map<Vector2, EnemyType> enemyData) {
        deleteOldEnemies();
        loadNewEnemies(enemyData);
    }

    private void deleteOldEnemies() {
        for (Enemy enemy : loadedEnemies) {
            enemy.unsubscribeFromUpdates();
            CollisionHandler.removeEnemy(enemy);
            levelContainer.getChildren().remove(enemy);
        }
        loadedEnemies.clear();
    }

    private void loadNewEnemies(Map<Vector2, EnemyType> enemyData) {
        enemyData.entrySet().stream().forEach(e -> {
            EnemyType type = e.getValue();

            if (type == null)
                throw new NullPointerException(type + " enemy type is null");

            Vector2 pos = e.getKey();
            Enemy newEnemy = new Enemy(type, pos.scalar(Tilemap.TILE_SIZE));
            CollisionHandler.addEnemy(newEnemy);
            loadedEnemies.add(newEnemy);
            levelContainer.getChildren().add(newEnemy);

        });
    }
}
