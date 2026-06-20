package it.unicam.cs.mpgc.rpg130730;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * Player logic
 *
 * @author Tommaso Acciarresi
 */
public class Player implements Updatable {

    @FXML
    private ImageView playerSprite;

    private final int SPEED = 200; // px/s

    private Tuple<Integer, Integer> input;
    private double x, y;

    public Player() {
        subscribeToUpdates();
    }

    public void update(double timeDelta) {
        handleInput(timeDelta);
    }

    private void handleInput(double timeDelta) {
        input = new Tuple<Integer, Integer>(
                (AppLauncher.keys.getOrDefault(KeyCode.A, false) ? -1 : 0)
                        + (AppLauncher.keys.getOrDefault(KeyCode.D, false) ? +1 : 0),
                (AppLauncher.keys.getOrDefault(KeyCode.W, false) ? -1 : 0)
                        + (AppLauncher.keys.getOrDefault(KeyCode.S, false) ? +1 : 0));
        System.out.println(input);

        move(input, timeDelta);
        // System.out.println("x: " + x + "; y: " + y);
    }

    public void move(Tuple<Integer, Integer> input, double timeDelta) {
        if (input.equals(new Tuple<Integer, Integer>(0, 0)))
            return;
        double r = SPEED * timeDelta;
        // WHY ARE THIS FUNCTION'S INPUTS INVERTED??? WHO PUTS Y BEFORE X???
        double angle = Math.atan2(input.y, input.x);

        this.x += r * Math.cos(angle);
        this.y += r * Math.sin(angle);

        updateSprite();
    }

    private void updateSprite() {
        playerSprite.setX(x);
        playerSprite.setY(y);
    };
}
