package it.unicam.cs.mpgc.rpg130730.util.io;

import javafx.scene.image.Image;

public class ImageResourceLoader {
    public Image load(String filepath) {
        return new Image(getClass().getResource(filepath).toExternalForm());
    }
}
