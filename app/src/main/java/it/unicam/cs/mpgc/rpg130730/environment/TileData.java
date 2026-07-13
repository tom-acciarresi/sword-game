package it.unicam.cs.mpgc.rpg130730.environment;

import javafx.scene.image.Image;

public record TileData(int index, Image sprite, boolean canCollide) {
    @Override
    public final @org.jspecify.annotations.Nullable String toString() {
        return String.format("index:%d, sprite:%s, %s", index, sprite.toString(),
                canCollide ? "this tile can collide" : "this tile can NOT collide");
    }
}
