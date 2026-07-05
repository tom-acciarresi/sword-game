package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public abstract class Entity extends StackPane implements Updatable {
    private int health;

    private Rectangle sprite = new Rectangle(Launcher.TILE_SIZE, Launcher.TILE_SIZE);

    private Vector2 position = Vector2.ZERO;

    public Entity() {
        subscribeToUpdates();

        getChildren().add(getSprite());

        setPosition(Launcher.WINDOW_CENTER);
    }

    public Entity(Vector2 position) {
        this();
        setPosition(position);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void move(Vector2 newPos) {
        setPosition(newPos);
    }

    public Vector2 getPosition() {
        return position;
    }

    protected void setPosition(Vector2 newPos) {
        position = newPos;

        setTranslateX(position.x());
        setTranslateY(position.y());
    }

    public Rectangle getSprite() {
        return sprite;
    }

    protected void setSprite(Image newImage) {
        sprite.setFill(new ImagePattern(newImage));
    }
}
