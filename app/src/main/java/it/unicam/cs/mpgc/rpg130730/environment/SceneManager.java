package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.GameLoop;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionSystem;
import it.unicam.cs.mpgc.rpg130730.entities.Enemy;
import it.unicam.cs.mpgc.rpg130730.entities.EnemyType;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveData;
import it.unicam.cs.mpgc.rpg130730.ui.UI;
import it.unicam.cs.mpgc.rpg130730.ui.MainMenu;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.Group;

/**
 * Handles loading and changing levels (scenes)
 *
 * @author Tommaso Acciarresi
 */
public class SceneManager extends Group {
    // #region constants
    public static final Vector2 LEVEL_SIZE = TileGrid.TILEMAP_SIZE;
    public static final Vector2 LEVEL_CENTER = new Vector2(
            LEVEL_SIZE.x() / 2 - TileGrid.TILE_SIZE / 2,
            LEVEL_SIZE.y() / 2 - TileGrid.TILE_SIZE / 2);

    private static final Level INITIAL_LEVEL = Level.ROOM_1;
    // #endregion

    private static @Nullable SceneManager instance;

    private Level currLevel = INITIAL_LEVEL;
    private TileGrid tilemap = new TileGrid();

    private @Nullable Player player;

    private Set<Enemy> loadedEnemies = new HashSet<Enemy>();
    private Set<RoomTransition> roomTransitions = new HashSet<RoomTransition>();

    private Group levelContainer = new Group();

    // #region constructors
    private SceneManager() {
    };
    // #endregion

    // #region get-set
    public Level getCurrLevel() {
        return currLevel;
    }

    public TileGrid getTileMap() {
        return tilemap;
    }

    public Player getPlayer() {
        return Objects.requireNonNull(player);
    }

    public Group getLevelContainer() {
        return levelContainer;
    }
    // #endregion

    public static SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();
        return Objects.requireNonNull(instance);
    }

    public void loadMainMenu() {
        this.getChildren().add(new MainMenu());
        GameLoop.getInstance().start();
    }

    public void initialize(@Nullable SaveData savedata) {
        createLevelContainer();

        player = new Player();
        Player p = Objects.requireNonNull(player);

        if (savedata != null) {
            p.setKills(savedata.kills());
            p.setHealth(savedata.health());
            loadLevel(savedata.level());
            Vector2 spawnPos = calculatePlayerSpawnPos(savedata);
            p.setPosition(spawnPos);
        } else {
            loadLevel(INITIAL_LEVEL);
        }

        levelContainer.getChildren().addAll(tilemap, player);

        this.getChildren().addAll(new UI(p));

        // Start reading input
        InputMap.initialize(this);
    }

    public void loadLevel(Level level) {
        LevelData levelData = AssetLibrary.getLevelData(level.filename());

        currLevel = level;
        loadTiles(levelData.tileData());
        loadEnemies(levelData.enemyData());
        loadRoomTransitions(levelData.transitions());
    }

    private void createLevelContainer() {
        levelContainer.setLayoutY(UI.GUI_SIZE.y());
        this.getChildren().add(levelContainer);
    }

    private void loadTiles(int[] tileData) {
        tilemap.changeTo(tileData);
    }

    private void loadEnemies(Map<Vector2, EnemyType> enemyData) {
        deleteOldEnemies();
        loadNewEnemies(enemyData);
    }

    private void deleteOldEnemies() {
        loadedEnemies.stream().forEach(enemy -> {
            if (enemy == null)
                throw new NullPointerException();
            deleteEnemy(enemy);
        });
        loadedEnemies.clear();
    }

    public void deleteEnemy(Enemy enemy) {
        enemy.unsubscribeFromUpdates();
        CollisionSystem.removeEnemy(enemy);
        levelContainer.getChildren().remove(enemy);
    }

    private void loadNewEnemies(Map<Vector2, EnemyType> enemyData) {
        enemyData.entrySet().stream().forEach(enemyEntry -> {
            EnemyType type = enemyEntry.getValue();

            if (type == null)
                throw new NullPointerException();

            Vector2 pos = enemyEntry.getKey();

            if (pos == null)
                throw new NullPointerException();

            Enemy newEnemy = new Enemy(type);
            newEnemy.setPosition(pos);

            CollisionSystem.addEnemy(newEnemy);
            loadedEnemies.add(newEnemy);
            levelContainer.getChildren().add(newEnemy);
        });
    }

    private void loadRoomTransitions(Set<RoomTransitionData> transitions) {
        deleteOldTransitions();
        loadNewTransitions(transitions);
    }

    private void loadNewTransitions(Set<RoomTransitionData> transitions) {
        transitions.stream().forEach(transitionData -> {
            if (transitionData == null)
                throw new NullPointerException();

            Vector2 pos;
            if (currLevel.equals(transitionData.roomA())) {
                pos = transitionData.transitionLocationA();
            } else if (currLevel.equals(transitionData.roomB())) {
                pos = transitionData.transitionLocationB();
            } else {
                throw new IllegalArgumentException("Transition has nothing to do with current room");
            }

            RoomTransition newTransition = new RoomTransition(
                    pos.x(),
                    pos.y(),
                    TileGrid.TILE_SIZE,
                    TileGrid.TILE_SIZE,
                    transitionData);

            CollisionSystem.addRoomTransition(newTransition);
            roomTransitions.add(newTransition);
            levelContainer.getChildren().add(newTransition);
        });
    }

    private void deleteOldTransitions() {
        roomTransitions.stream().forEach(roomTransition -> {
            if (roomTransition == null)
                throw new NullPointerException();
            deleteRoomTransition(roomTransition);

        });
        roomTransitions.clear();
    }

    private void deleteRoomTransition(RoomTransition roomTransition) {
        CollisionSystem.removeRoomTransition(roomTransition);
        levelContainer.getChildren().remove(roomTransition);
    }

    private Vector2 calculatePlayerSpawnPos(SaveData savedata) {
        Set<Vector2> possibleSpawnCoords = new HashSet<Vector2>();
        roomTransitions.stream().forEach(roomTransition -> {
            RoomTransitionData data = roomTransition.getTransitionData();
            possibleSpawnCoords.add(data.roomA().equals(currLevel) ? data.playerSpawnA() : data.playerSpawnB());
        });

        Vector2 playerPos = savedata.playerPos();
        Vector2 spawnPos = possibleSpawnCoords.stream().min((vector1, vector2) -> {
            if (vector1 == null || vector2 == null)
                throw new NullPointerException();

            return Double.compare(playerPos.distanceValueTo(vector1), playerPos.distanceValueTo(vector2));
        }).get();

        if (spawnPos == null)
            throw new NullPointerException();
        return spawnPos;
    }
}
