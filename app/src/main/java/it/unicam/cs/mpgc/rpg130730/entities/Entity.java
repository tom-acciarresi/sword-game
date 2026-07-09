package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
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

    private Vector2 colliderSize = new Vector2(48, 24);
    private Vector2 colliderOffset = new Vector2(
            (Tilemap.TILE_SIZE - colliderSize.x()) / 2,
            Tilemap.TILE_SIZE - colliderSize.y());

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
        setPosition(checkTileCollision(newPos, getPosition()));
    }

    private Vector2 checkTileCollision(Vector2 newPos, Vector2 oldPos) {
        // Collider's horizontal component
        boolean intersectsX = false;
        Bounds boundsX = new Rectangle(
                newPos.x() + colliderOffset.x(),
                oldPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y()).getBoundsInParent();
        intersectsX = CollisionHandler.getCollTiles().stream().anyMatch(t -> {
            Bounds tileBounds = t.getBoundsInParent();
            return tileBounds.intersects(boundsX);
        });

        // Collider's vertical component
        boolean intersectsY = false;
        Bounds boundsY = new Rectangle(
                oldPos.x() + colliderOffset.x(),
                newPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y()).getBoundsInParent();
        intersectsY = CollisionHandler.getCollTiles().stream().anyMatch(t -> {
            Bounds tileBounds = t.getBoundsInParent();
            return tileBounds.intersects(boundsY);
        });

        // Don't update X if colliding horizontally
        // Don't update Y if colliding vertically
        return new Vector2(intersectsX ? getPosition().x() : newPos.x(), intersectsY ? getPosition().y() : newPos.y());
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 newPos) {
        position = newPos;

        setTranslateX(position.x());
        setTranslateY(position.y());
    }

    public Rectangle getSprite() {
        return sprite;
    }

    public void setSprite(Image newImage) {
        sprite.setFill(new ImagePattern(newImage));
    }

    public Vector2 getColliderOffset() {
        return colliderOffset;
    }

    public Vector2 getColliderSize() {
        return colliderSize;
    }

    public Bounds getCollisionBounds() {
        Bounds bounds = new Rectangle(
                position.x() + colliderOffset.x(),
                position.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y()).getBoundsInParent();
        if (bounds == null)
            throw new NullPointerException(bounds + " bounds are null");
        return bounds;
    }
}
