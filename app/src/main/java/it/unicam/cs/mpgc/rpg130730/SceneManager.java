package it.unicam.cs.mpgc.rpg130730;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * Singleton class for adding and removing nodes from the scene tree
 *
 * @author Tommaso Acciarresi
 */
public class SceneManager extends Group {
    private static SceneManager instance = null;

    private SceneManager() {
    }

    public static SceneManager get_instance() {
        if (instance == null) {
            instance = new SceneManager();
        }

        return instance;
    }

    public boolean addNode(Node node) {
        return getChildren().add(node);
    }

    public boolean removeNode(Node node) {
        return getChildren().remove(node);
    }
}
