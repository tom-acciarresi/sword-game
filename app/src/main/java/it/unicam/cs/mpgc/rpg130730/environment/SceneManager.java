package it.unicam.cs.mpgc.rpg130730.environment;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * Singleton class for adding and removing nodes from the scene tree
 *
 * @author Tommaso Acciarresi
 */
public class SceneManager extends Group {
    public static enum TileLayout {
        ROOM_1(0, "first_level.txt"),
        ROOM_2(1, "second_level.txt");

        private final int index;
        private final String filename;

        TileLayout(int index, String filename) {
            this.index = index;
            this.filename = filename;
        }

        public int index() {
            return index;
        }

        public String filename() {
            return filename;
        }
    }

    public void addChild(Node node) {
        getChildren().add(node);
    }

    public void removeChild(Node node) {
        getChildren().remove(node);
    }

    public void replaceChild(Node from, Node to) {
        removeChild(from);
        addChild(to);
    }
}
