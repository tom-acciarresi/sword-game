package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.Tile;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public abstract class Entity extends StackPane implements Updatable {
    private double health;

    private Rectangle sprite = new Rectangle(
            Tilemap.TILE_SIZE,
            Tilemap.TILE_SIZE,
            new ImagePattern(AssetLibrary.MISSING_SPRITE));

    private Vector2 position = Vector2.ZERO;

    private static final Vector2 COLLIDER_SIZE = new Vector2(48, 12);
    private static final Vector2 COLLIDER_OFFSET = new Vector2(
            (Tilemap.TILE_SIZE - COLLIDER_SIZE.x()) / 2,
            Tilemap.TILE_SIZE - COLLIDER_SIZE.y());

    public Entity() {
        subscribeToUpdates();

        getChildren().add(getSprite());

        setPosition(Launcher.LEVEL_CENTER);
    }

    public Entity(Vector2 position) {
        this();
        setPosition(position);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void move(Vector2 newPos) {
        setPosition(checkTileCollision(newPos));
    }

    protected Vector2 checkTileCollision(Vector2 newPos) {
        boolean intersectsX = false;
        boolean intersectsY = false;
        for (Tile tile : CollisionHandler.getCollTiles()) {
            Bounds tileBounds = tile.getBoundsInParent();

            // Collider's horizontal component
            double newX = newPos.x() + COLLIDER_OFFSET.x();
            double oldY = getPosition().y() + COLLIDER_OFFSET.y();
            Bounds boundsX = new Rectangle(newX, oldY, COLLIDER_SIZE.x(), COLLIDER_SIZE.y()).getBoundsInParent();
            if (tileBounds.intersects(boundsX))
                intersectsX = true;

            // Collider's vertical component
            double oldX = getPosition().x() + COLLIDER_OFFSET.x();
            double newY = newPos.y() + COLLIDER_OFFSET.y();
            Bounds boundsY = new Rectangle(oldX, newY, COLLIDER_SIZE.x(), COLLIDER_SIZE.y()).getBoundsInParent();
            if (tileBounds.intersects(boundsY))
                intersectsY = true;
        }

        // Don't update X if colliding horizontally
        // Don't update Y if colliding vertically
        return new Vector2(intersectsX ? getPosition().x() : newPos.x(), intersectsY ? getPosition().y() : newPos.y());
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
