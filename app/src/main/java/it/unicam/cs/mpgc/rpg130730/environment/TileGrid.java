package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Arrays;
import java.util.stream.Stream;

import it.unicam.cs.mpgc.rpg130730.entities.CollisionSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.layout.GridPane;

public class TileGrid extends GridPane {
    // #region constants
    public static final int TILE_SIZE = 64;
    public static final Vector2 TILEMAP_DIMENSIONS = new Vector2(12, 10);
    public static final int TILE_AMOUNT = (int) (TILEMAP_DIMENSIONS.x() * TILEMAP_DIMENSIONS.y());
    // #endregion

    private Tile[][] tileGrid = instantiateTiles();

    // #region constructors
    public TileGrid() {
    }

    public TileGrid(int[] tileArragementData) {
        this();
        changeTileMapTo(tileArragementData);
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

    public void changeTileMapTo(int[] tileArragementData) {
        Tile[] tiles = getTiles();
        for (int i = 0; i < TILE_AMOUNT; i++) {
            Tile currTile = tiles[i];
            if (currTile.getInfo().canCollide())
                CollisionSystem.removeCollidableTile(currTile);

            currTile.changeTileTo(tileArragementData[i]);

            if (currTile.getInfo().canCollide())
                CollisionSystem.addCollidableTile(currTile);
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
}
