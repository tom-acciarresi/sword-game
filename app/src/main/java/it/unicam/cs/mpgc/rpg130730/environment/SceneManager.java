package it.unicam.cs.mpgc.rpg130730.environment;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.Enemy;
import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.ui.GUI;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.Group;
import javafx.scene.Node;

public final class SceneManager extends Group {
    public static enum LevelData {
        ROOM_1("first_level.txt"),
        ROOM_2("second_level.txt");

        private final String filename;

        LevelData(String filename) {
            this.filename = filename;
        }

        public String filename() {
            return filename;
        }
    }

    public void add(Node node) {
        getChildren().add(node);
    }

    public void remove(Node node) {
        getChildren().remove(node);
    }

    public void replace(Node previous, Node next) {
        remove(previous);
        add(next);
    }

    // TODO change
    public void loadFirstScene() {
        // Instance tiles
        Tilemap tilemap = new Tilemap(LevelData.ROOM_1);
        // TODO "set layout" probably bad
        tilemap.setLayoutY(GUI.GUI_SIZE.y());

        // Instance enemy
        Enemy pig_enemy = new Enemy(Enemy.EnemyType.PIG, new Vector2(2, 3).scalar(Launcher.TILE_SIZE));
        pig_enemy.setLayoutY(GUI.GUI_SIZE.y());

        // Instance player
        Player player = new Player(new Vector2(3, 5).scalar(Launcher.TILE_SIZE));
        player.setLayoutY(GUI.GUI_SIZE.y());

        add(new GUI(player));
        add(tilemap);
        add(pig_enemy);
        add(player);

        // add(new MainMenu());
    }
}
