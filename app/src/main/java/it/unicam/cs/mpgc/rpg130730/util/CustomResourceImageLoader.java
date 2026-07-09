package it.unicam.cs.mpgc.rpg130730.util;

import javafx.scene.image.Image;

public final class CustomResourceImageLoader {
    public Image load(String filepath) {
        return new Image(getClass().getResource(filepath).toExternalForm());
    }
}
