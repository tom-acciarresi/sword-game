package it.unicam.cs.mpgc.rpg130730.environment;

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
}
