package it.unicam.cs.mpgc.rpg130730.util;

import javafx.scene.image.Image;

/**
 * Loads image from resources directory
 *
 * @author Tommaso Acciarresi
 */
public class CustomImageLoader {
    public Image loadImage(String filepath) {
        return new Image(getClass().getResource(filepath).toExternalForm());
    }
}
