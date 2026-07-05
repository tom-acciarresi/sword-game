package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.ArrayList;
import java.util.Arrays;
import it.unicam.cs.mpgc.rpg130730.util.AssetRegistry;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Draws tiles in a grid
 *
 * @author Tommaso Acciarresi
 */
public class Tilemap extends GridPane {
    private static final String NULL_TILE_PATH = "/images/tiles/null.png";
    public static final TileInfo NULL_TILE = new TileInfo(0, new CustomImageLoader().loadImage(NULL_TILE_PATH), false);

    private Tile[][] tiles = new Tile[Launcher.GRID_HEIGHT][Launcher.GRID_WIDTH];

    public Tilemap() {
        instantiateTiles();
    }

    public Tilemap(String level_identifier) {
        this();
        setTileMapTo(level_identifier);
    }

    private void instantiateTiles() {
        for (int i = 0; i < Launcher.GRID_HEIGHT; i++) {
            for (int j = 0; j < Launcher.GRID_WIDTH; j++) {
                Tile newTile = new Tile();
                tiles[i][j] = newTile;
                this.add(newTile, j, i);
            }
        }
    }

    public void setTileMapTo(String level_identifier) {
        setTileMapTo(loadTileBitmapFromRegistry(level_identifier));
    }

    private void setTileMapTo(int[] tileLayoutBitmap) {
        int i = 0;
        for (Tile currTile : getTiles()) {
            currTile.changeTo(tileLayoutBitmap[i++]);
            if (currTile.getInfo().canCollide)
                CollisionHandler.collTiles.add(currTile);
        }
    }

    private int[] loadTileBitmapFromRegistry(String level_identifier) {
        String info = AssetRegistry.getLevelData().get(level_identifier).replaceAll("\n", " ");
        int[] array = Arrays.stream(info.split(" ")).mapToInt(Integer::parseInt).toArray();

        if (array == null) {
            System.err.println("Null array");
            return new int[0];
        }

        return array;
    }

    public void setTile(Vector2 coords, int index) {
        tiles[(int) coords.x()][(int) coords.y()].changeTo(index);
    }

    public ArrayList<Tile> getTiles() {
        ArrayList<Tile> list = new ArrayList<Tile>(Launcher.GRID_HEIGHT * Launcher.GRID_WIDTH);
        for (int i = 0; i < Launcher.GRID_HEIGHT; i++) {
            for (int j = 0; j < Launcher.GRID_WIDTH; j++) {
                list.add(tiles[i][j]);
            }
        }

        return list;
    }

    public class Tile extends StackPane {
        private TileInfo info = NULL_TILE;
        private Rectangle sprite = new Rectangle(Launcher.TILE_SIZE, Launcher.TILE_SIZE,
                new ImagePattern(info.sprite()));

        public Tile() {
            getChildren().add(sprite);
        }

        public void changeTo(int index) {
            TileInfo tileInfo = AssetRegistry.getTileInfo().get(index);
            if (tileInfo == null) {
                System.err.println("Null tileInfo");
                return;
            }
            this.info = tileInfo;
            this.sprite.setFill(new ImagePattern(info.sprite()));
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
