package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.CollisionHandler;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.TileLayout;
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
    private static final TileInfo NULL_TILE = new TileInfo(0, new CustomImageLoader().loadImage(NULL_TILE_PATH), false);

    private Tile[][] tiles = new Tile[(int) Launcher.GRID_DIMENSIONS.y()][(int) Launcher.GRID_DIMENSIONS.x()];

    public Tilemap() {
        instantiateTiles();
    }

    public Tilemap(TileLayout layout) {
        this();
        setTileMapTo(layout);
    }

    private void instantiateTiles() {
        for (int i = 0; i < Launcher.GRID_DIMENSIONS.y(); i++) {
            for (int j = 0; j < Launcher.GRID_DIMENSIONS.x(); j++) {
                Tile newTile = new Tile();
                tiles[i][j] = newTile;
                add(newTile, j, i);
            }
        }
    }

    public void setTileMapTo(TileLayout layout) {
        int i = 0;
        String levelString = AssetLibrary.getTileLayouts().get(layout.index());
        int[] tileLayoutInfo = Arrays.stream(levelString.split(" ")).mapToInt(Integer::parseInt).toArray();

        for (Tile currTile : getTiles()) {
            currTile.changeTo(tileLayoutInfo[i++]);
            if (currTile.getInfo().canCollide)
                CollisionHandler.collTiles.add(currTile);
        }
    }

    public void setTile(Vector2 coords, int index) {
        tiles[(int) coords.x()][(int) coords.y()].changeTo(index);
    }

    public List<Tile> getTiles() {
        List<Tile> list = new ArrayList<Tile>();
        for (int i = 0; i < Launcher.GRID_DIMENSIONS.y(); i++) {
            for (int j = 0; j < Launcher.GRID_DIMENSIONS.x(); j++) {
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
            TileInfo tileInfo = AssetLibrary.getTileInfo().get(index);
            if (tileInfo == null) {
                System.err.println("Null tileInfo");
                return;
            }

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
