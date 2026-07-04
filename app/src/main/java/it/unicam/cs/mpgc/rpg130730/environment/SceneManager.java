package it.unicam.cs.mpgc.rpg130730.environment;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * Singleton class for adding and removing nodes from the scene tree
 *
 * @author Tommaso Acciarresi
 */
public class SceneManager extends Group {
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
