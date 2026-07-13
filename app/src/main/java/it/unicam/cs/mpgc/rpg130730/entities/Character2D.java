package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.Updatable;
import it.unicam.cs.mpgc.rpg130730.environment.TileGrid;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public abstract class Character2D extends StackPane implements Updatable {
    private double health;

    private Rectangle sprite = new Rectangle(
            TileGrid.TILE_SIZE,
            TileGrid.TILE_SIZE,
            new ImagePattern(AssetLibrary.MISSING_SPRITE));

    private Vector2 position = Vector2.ZERO;

    private Vector2 colliderSize = new Vector2(48, 24);
    private Vector2 colliderOffset = new Vector2(
            (TileGrid.TILE_SIZE - colliderSize.x()) / 2,
            TileGrid.TILE_SIZE - colliderSize.y());

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

    // #region get-set
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
                colliderSize.y()).intersects(Launcher.getSceneManager().getTileMap().getBoundsInParent()));
    }

    private Vector2 calculateTileCollision(Vector2 newPos, Vector2 oldPos) {
        // Check horizontal collision
        boolean intersectsX = getBoundsX(newPos, oldPos);

        // Check vertical collision
        boolean intersectsY = getBoundsY(newPos, oldPos);

        // Don't update X if colliding horizontally
        // Don't update Y if colliding vertically
        return new Vector2(intersectsX ? oldPos.x() : newPos.x(), intersectsY ? oldPos.y() : newPos.y());
    }

    private boolean getBoundsX(Vector2 newPos, Vector2 oldPos) {
        BoundingBox boundsX = new BoundingBox(
                newPos.x() + colliderOffset.x(),
                oldPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y());
        return CollisionSystem.collidesWithTiles(boundsX);
    }

    private boolean getBoundsY(Vector2 newPos, Vector2 oldPos) {
        BoundingBox boundsY = new BoundingBox(
                oldPos.x() + colliderOffset.x(),
                newPos.y() + colliderOffset.y(),
                colliderSize.x(),
                colliderSize.y());
        return CollisionSystem.collidesWithTiles(boundsY);
    }

}
