package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Arrays;
import java.util.stream.Stream;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tilemap extends GridPane {
    // #region constants
    public static final int TILE_SIZE = 64;
    public static final Vector2 TILEMAP_DIMENSIONS = new Vector2(12, 10);
    public static final int TILE_AMOUNT = (int) (TILEMAP_DIMENSIONS.x() * TILEMAP_DIMENSIONS.y());
    // #endregion

    private Tile[][] tileGrid = instantiateTiles();

    // #region constructors
    public Tilemap() {
    }

    public Tilemap(int[] tileArragementData) {
        this();
        changeTilemapTo(tileArragementData);
    }
    // #endregion

    // #region set-get
    public Tile[] getTiles() {
        Tile[] arr = Arrays.stream(tileGrid).flatMap(Stream::of).toArray(Tile[]::new);

        if (arr == null)
            throw new NullPointerException(arr + " is null");

        return arr;
    }
    // #endregion

    public void changeTilemapTo(int[] tileArragementData) {
        Tile[] tiles = getTiles();
        for (int i = 0; i < TILE_AMOUNT; i++) {
            Tile currTile = tiles[i];
            if (currTile.getInfo().canCollide)
                CollisionHandler.removeCollidableTile(currTile);

            currTile.changeTileTo(tileArragementData[i]);

            if (currTile.getInfo().canCollide)
                CollisionHandler.addCollidableTile(currTile);
        }
    }

    private Tile[][] instantiateTiles() {
        Tile[][] tg = new Tile[(int) TILEMAP_DIMENSIONS.y()][(int) TILEMAP_DIMENSIONS.x()];
        for (int i = 0; i < TILEMAP_DIMENSIONS.y(); i++) {
            for (int j = 0; j < TILEMAP_DIMENSIONS.x(); j++) {
                Tile newTile = new Tile();
                tg[i][j] = newTile;
                add(newTile, j, i);
            }
        }
        return tg;
    }

    public class Tile extends StackPane {
        // #region constants
        public static final TileState NULL_TILE = new TileState(
                0,
                AssetLibrary.MISSING_SPRITE, false);
        // #endregion

        private TileState info = NULL_TILE;
        private Rectangle sprite = new Rectangle(
                TILE_SIZE,
                TILE_SIZE,
                new ImagePattern(info.sprite()));

        // #region constructors
        public Tile() {
            getChildren().add(sprite);
        }
        // #endregion

        // #region set-get
        public TileState getInfo() {
            return info;
        }
        // #endregion

        public void changeTileTo(int index) {
            TileState tileInfo = AssetLibrary.getTileInfo(index);
            info = tileInfo;
            sprite.setFill(new ImagePattern(info.sprite()));
        }

        @Override
        public @org.jspecify.annotations.Nullable String toString() {
            return String.format("info: %s, sprite: %s", info, sprite);
        }
    }

    public record TileState(int index, Image sprite, boolean canCollide) {
        @Override
        public final @org.jspecify.annotations.Nullable String toString() {
            return String.format("index:%d, sprite:%s, %s", index, sprite.toString(),
                    canCollide ? "this tile can collide" : "this tile can NOT collide");
        }
    }
}
