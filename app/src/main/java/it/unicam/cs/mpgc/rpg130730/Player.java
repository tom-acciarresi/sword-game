package it.unicam.cs.mpgc.rpg130730;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class Player implements Updatable {

    @FXML
    private ImageView playerSprite;

    private int SPEED = 200; // px/s
    private double x, y;

    public Player() {
        subscribeToUpdates();
    }

    public void update(double timeDelta) {
        System.out.println("Hello");
        // handleInput();
        // handleAnimation();
    }

    public void subscribeToUpdates() {
        AppLauncher.objectsToUpdate.add(this);
    }
}
