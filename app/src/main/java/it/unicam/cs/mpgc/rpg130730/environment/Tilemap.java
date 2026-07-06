package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.LevelData;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tilemap extends GridPane {
    private Tile[][] tileGrid = new Tile[(int) Launcher.TILEMAP_DIMENSIONS.y()][(int) Launcher.TILEMAP_DIMENSIONS.x()];

    public Tilemap() {
        instantiateTiles();
    }

    public Tilemap(LevelData levelData) {
        this();
        changeTilemapTo(levelData);
    }

    private void instantiateTiles() {
        for (int i = 0; i < Launcher.TILEMAP_DIMENSIONS.y(); i++) {
            for (int j = 0; j < Launcher.TILEMAP_DIMENSIONS.x(); j++) {
                Tile newTile = new Tile();
                tileGrid[i][j] = newTile;
                add(newTile, j, i);
            }
        }
    }

    public void changeTilemapTo(LevelData levelData) {
        int i = 0;
        String levelString = AssetLibrary.getLevelData(levelData.filename());
        int[] levelBitMap = Arrays.stream(levelString.split(" ")).mapToInt(Integer::parseInt).toArray();
        for (Tile currTile : getListOfTiles()) {
            currTile.changeTo(levelBitMap[i++]);
            if (currTile.getInfo().canCollide)
                CollisionHandler.collTiles.add(currTile);
        }
    }

    public void setTileTo(Vector2 coords, int index) {
        tileGrid[(int) coords.x()][(int) coords.y()].changeTo(index);
    }

    public List<Tile> getListOfTiles() {
        List<Tile> list = new ArrayList<Tile>();
        for (int i = 0; i < Launcher.TILEMAP_DIMENSIONS.y(); i++) {
            for (int j = 0; j < Launcher.TILEMAP_DIMENSIONS.x(); j++) {
                list.add(tileGrid[i][j]);
            }
        }
        return list;
    }

    public class Tile extends StackPane {
        private static final TileInfo NULL_TILE = new TileInfo(0,
                AssetLibrary.NULL_TILE_SPRITE, false);
        private TileInfo info = NULL_TILE;
        private Rectangle sprite = new Rectangle(Launcher.TILE_SIZE, Launcher.TILE_SIZE,
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
