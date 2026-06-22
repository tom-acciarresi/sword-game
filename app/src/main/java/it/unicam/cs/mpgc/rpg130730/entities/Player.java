package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.AppLauncher;
import it.unicam.cs.mpgc.rpg130730.util.Tuple;
import it.unicam.cs.mpgc.rpg130730.util.Updatable;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Player logic
 *
 * @author Tommaso Acciarresi
 */
public class Player extends StackPane implements Updatable {

    private static final int SPEED = 400; // px/s

    private Rectangle playerSprite = new Rectangle(AppLauncher.TILE_SIZE, AppLauncher.TILE_SIZE);

    private Tuple<Integer, Integer> input;
    private Tuple<Double, Double> position;

    public Player(Tuple<Double, Double> position) {
        subscribeToUpdates();
        getChildren().add(playerSprite);

        setPosition(position);
        setSprite("/images/player.png");
    }

    public void update(double timeDelta) {
        handleInput(timeDelta);
    }

    private void handleInput(double timeDelta) {
        handleMovement(timeDelta);

        checkIfExitPressed();
    }

    private void handleMovement(double timeDelta) {
        HashMap<KeyCode, Boolean> currentlyPressedKeys = AppLauncher.getCurrentlyPressedKeys();
        input = new Tuple<Integer, Integer>(
                (currentlyPressedKeys.getOrDefault(KeyCode.A, false) ? -1 : 0)
                        + (currentlyPressedKeys.getOrDefault(KeyCode.D, false) ? +1 : 0),
                (currentlyPressedKeys.getOrDefault(KeyCode.W, false) ? -1 : 0)
                        + (currentlyPressedKeys.getOrDefault(KeyCode.S, false) ? +1 : 0));
        // System.out.println(input);

        move(input, timeDelta);
        // System.out.println("x: " + x + "; y: " + y);
    }

    private void checkIfExitPressed() {
        if (AppLauncher.getCurrentlyPressedKeys().getOrDefault(KeyCode.ESCAPE, false))
            Platform.exit();
    }

    public void move(Tuple<Integer, Integer> input, double timeDelta) {
        if (input.equals(new Tuple<Integer, Integer>(0, 0)))
            return;
        double r = SPEED * timeDelta;
        // WHY ARE THIS FUNCTION'S INPUTS INVERTED??? WHO PUTS Y BEFORE X???
        double angle = Math.atan2(input.y, input.x);

        setPosition(new Tuple<Double, Double>(
                position.x + r * Math.cos(angle),
                position.y + r * Math.sin(angle)));
    }

    public void setPosition(Tuple<Double, Double> pos) {
        this.position = pos;

        updateSprite();
    }

    private void setSprite(Image image) {
        playerSprite.setFill(new ImagePattern(image));
    }

    public void setSprite(String filepath) {
        setSprite(loadSprite(filepath));
    }

    private Image loadSprite(String filepath) {
        return new Image(getClass().getResource(filepath).toExternalForm());

    }

    private void updateSprite() {
        this.setTranslateX(position.x);
        this.setTranslateY(position.y);
    };
}
