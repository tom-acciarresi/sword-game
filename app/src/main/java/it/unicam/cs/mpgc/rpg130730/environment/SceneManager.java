package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
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

public class SceneManager extends Group {
    // #region constants
    private static final Level INITIAL_LEVEL = Level.ROOM_1;
    // #endregion

    private Level currLevel = INITIAL_LEVEL;
    private TileGrid tilemap = new TileGrid();

    @SuppressWarnings("null")
    private Player player;

    private Set<Enemy> loadedEnemies = new HashSet<Enemy>();
    private Set<RoomTransition> roomTransitions = new HashSet<RoomTransition>();

    private Group levelContainer = new Group();

    // #region get-set
    public Level getCurrLevel() {
        return currLevel;
    }

    public TileGrid getTileMap() {
        return tilemap;
    }

    public Player getPlayer() {
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

        Vector2 spawnPos = calculatePlayerSpawnPos(savedata);
        player.setPosition(spawnPos);
    }

    public void initialize(Level level) {
        createLevelContainer();

        loadLevel(level);

        player = new Player();

        levelContainer.getChildren().addAll(tilemap, player);

        this.getChildren().addAll(new UI(player));

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
        tilemap.changeTileMapTo(tileData);
    }

    private void loadEnemies(Map<Vector2, EnemyType> enemyData) {
        deleteOldEnemies();
        loadNewEnemies(enemyData);
    }

    private void deleteOldEnemies() {
        for (Enemy enemy : loadedEnemies) {
            enemy.unsubscribeFromUpdates();
            CollisionSystem.removeEnemy(enemy);
            levelContainer.getChildren().remove(enemy);
        }
        loadedEnemies.clear();
    }

    private void loadNewEnemies(Map<Vector2, EnemyType> enemyData) {
        enemyData.entrySet().stream().forEach(enemyEntry -> {
            EnemyType type = enemyEntry.getValue();

            if (type == null)
                throw new NullPointerException(type + " enemy type is null");

            Vector2 pos = enemyEntry.getKey();

            if (pos == null)
                throw new NullPointerException(pos + " enemy pos is null");

            Enemy newEnemy = new Enemy(type, pos);

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
                throw new NullPointerException(transitionData + " rt data is null");

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
        for (RoomTransition roomTransition : roomTransitions) {

            if (roomTransition == null)
                throw new NullPointerException(roomTransition + " rt data is null");

            CollisionSystem.removeRoomTransition(roomTransition);
            levelContainer.getChildren().remove(roomTransition);
        }
        roomTransitions.clear();
    }

    private Vector2 calculatePlayerSpawnPos(SaveData savedata) {
        Set<Vector2> possibleSpawnCoords = new HashSet<Vector2>();
        for (RoomTransition roomTransition : roomTransitions) {
            RoomTransitionData data = roomTransition.getTransitionData();
            if (data.roomA().equals(currLevel))
                possibleSpawnCoords.add(data.playerSpawnA());
            else
                possibleSpawnCoords.add(data.playerSpawnB());
        }

        Vector2 playerPos = savedata.playerPos();
        Vector2 spawnPos = possibleSpawnCoords.stream().min((vector1, vector2) -> {
            if (vector1 == null || vector2 == null)
                throw new NullPointerException(String.format("one of %s %s is null", vector1, vector2));

            return Double.compare(playerPos.distanceValueTo(vector1), playerPos.distanceValueTo(vector2));
        }).get();

        if (spawnPos == null)
            throw new NullPointerException(spawnPos + " spawn pos is null");
        return spawnPos;
    }
}
