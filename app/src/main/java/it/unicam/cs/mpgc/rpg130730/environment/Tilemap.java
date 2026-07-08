package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Arrays;
import java.util.stream.Stream;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.LevelData;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tilemap extends GridPane {
    public static final int TILE_SIZE = 64;
    public static final Vector2 TILEMAP_DIMENSIONS = new Vector2(12, 10);
    public static final int TILE_AMOUNT = (int) (TILEMAP_DIMENSIONS.x() * TILEMAP_DIMENSIONS.y());

    private Tile[][] tileGrid = instantiateTiles();

    public Tilemap() {
    }

    public Tilemap(LevelData levelData) {
        this();
        changeTilemapTo(levelData);
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

    public void changeTilemapTo(LevelData levelData) {
        String levelString = AssetLibrary.getLevelData(levelData.filename());
        int[] levelBitMap = Arrays.stream(levelString.split(" ")).parallel().mapToInt(Integer::parseInt).toArray();

        Tile[] tiles = getListOfTiles();
        for (int i = 0; i < TILE_AMOUNT; i++) {
            Tile currTile = tiles[i];
            if (currTile.getInfo().canCollide)
                CollisionHandler.removeCollidableTile(currTile);

            currTile.changeTo(levelBitMap[i]);

            if (currTile.getInfo().canCollide)
                CollisionHandler.addCollidableTile(currTile);
        }
    }

    public void setTileTo(Vector2 coords, int index) {
        tileGrid[(int) coords.y()][(int) coords.x()].changeTo(index);
    }

    public Tile[] getListOfTiles() {
        Tile[] arr = Stream.of(tileGrid).flatMap(Stream::of).toArray(Tile[]::new);
        if (arr == null)
            throw new NullPointerException(arr + " is null");

        return arr;
    }

    public class Tile extends StackPane {
        public static final TileInfo NULL_TILE = new TileInfo(0,
                AssetLibrary.MISSING_SPRITE, false);
        private TileInfo info = NULL_TILE;
        private Rectangle sprite = new Rectangle(TILE_SIZE, TILE_SIZE,
                new ImagePattern(info.sprite()));

        public Tile() {
            getChildren().add(sprite);
        }

        public void changeTo(int index) {
            TileInfo tileInfo = AssetLibrary.getTileInfo(index);
            info = tileInfo;
            sprite.setFill(new ImagePattern(info.sprite()));
        }

        public TileInfo getInfo() {
            return info;
        }

        @Override
        public String toString() {
            return info + ", " + sprite;
        }
    }

    public record TileInfo(int index, Image sprite, boolean canCollide) {
    }
}
