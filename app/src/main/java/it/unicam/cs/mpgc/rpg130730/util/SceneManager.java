package it.unicam.cs.mpgc.rpg130730.util;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * Singleton class for adding and removing nodes from the scene tree
 *
 * @author Tommaso Acciarresi
 */
public class SceneManager extends Group {
    public boolean addNode(Node node) {
        return getChildren().add(node);
    }

    public boolean removeNode(Node node) {
        return getChildren().remove(node);
    }
}
