package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.Tile;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public abstract class Character2D extends StackPane implements Updatable {
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

    // #region constructors
    public Character2D() {
        subscribeToUpdates();

        this.getChildren().add(getSprite());

        setPosition(Launcher.LEVEL_CENTER);
    }

    public Character2D(Vector2 position) {
        this();
        setPosition(position);
    }
    // #endregion

    // #region set-get
    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
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

    public BoundingBox getCollisionBounds() {
        BoundingBox bounds = new BoundingBox(
                position.x() + colliderOffset.x(),
                position.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y());
        return bounds;
    }
    // #endregion

    public void move(Vector2 newPos) {
        setPosition(calculateCollision(newPos, getPosition()));
    }

    private Vector2 calculateCollision(Vector2 newPos, Vector2 oldPos) {
        // No going out of bounds
        if (isOutOfBounds(newPos))
            return oldPos;

        return calculateTileCollision(newPos, oldPos);
    }

    private boolean isOutOfBounds(Vector2 newPos) {
        return !(new BoundingBox(
                newPos.x() + colliderOffset.x(),
                newPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y()).intersects(SceneManager.getTilemap().getBoundsInParent()));
    }

    private Vector2 calculateTileCollision(Vector2 newPos, Vector2 oldPos) {
        Set<Tile> collTiles = CollisionHandler.getCollTiles();

        // Check horizontal collision
        boolean intersectsX = getBoundsX(newPos, oldPos, collTiles);

        // Check vertical collision
        boolean intersectsY = getBoundsY(newPos, oldPos, collTiles);

        // Don't update X if colliding horizontally
        // Don't update Y if colliding vertically
        return new Vector2(intersectsX ? getPosition().x() : newPos.x(), intersectsY ? getPosition().y() : newPos.y());
    }

    private boolean getBoundsY(Vector2 newPos, Vector2 oldPos, Set<Tile> collTiles) {
        boolean intersectsY = false;
        BoundingBox boundsY = new BoundingBox(
                oldPos.x() + colliderOffset.x(),
                newPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y());
        intersectsY = collTiles.stream().anyMatch(t -> {
            Bounds tileBounds = t.getBoundsInParent();
            return tileBounds.intersects(boundsY);
        });
        return intersectsY;
    }

    private boolean getBoundsX(Vector2 newPos, Vector2 oldPos, Set<Tile> collTiles) {
        boolean intersectsX = false;
        BoundingBox boundsX = new BoundingBox(
                newPos.x() + colliderOffset.x(),
                oldPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y());
        intersectsX = collTiles.stream().anyMatch(t -> {
            Bounds tileBounds = t.getBoundsInParent();
            return tileBounds.intersects(boundsX);
        });
        return intersectsX;
    }
}
