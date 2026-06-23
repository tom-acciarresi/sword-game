package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg130730.util.CustomFileReader;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.GlobalConstants;
import it.unicam.cs.mpgc.rpg130730.util.Vector2i;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Draws tiles in a grid
 *
 * @author Tommaso Acciarresi
 */
public class Tilemap extends GridPane {

    private HashMap<Integer, Image> tileDictionary = new HashMap<Integer, Image>();

    private Tile[][] tiles = new Tile[GlobalConstants.GRID_HEIGHT][GlobalConstants.GRID_WIDTH];

    public Tilemap() {
        instantiateTiles();

        loadTileSpritesWithJSON();
    }

    public Tilemap(String filepath) {
        this();
        setTileMapTo(loadTileBitmapFromTextFile(filepath));
    }

    private void instantiateTiles() {
        for (int i = 0; i < GlobalConstants.GRID_HEIGHT; i++) {
            for (int j = 0; j < GlobalConstants.GRID_WIDTH; j++) {
                Tile newTile = new Tile();
                tiles[i][j] = newTile;
                this.add(newTile, j, i);
            }
        }
    }

    private void loadTileSpritesWithJSON() {
        Gson gson = new Gson();
        CustomFileReader fr = new CustomFileReader();
        String fileOut = fr.readFile(GlobalConstants.TILE_INFO_FILE);

        TypeToken<ArrayList<HashMap<String, String>>> mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        };
        ArrayList<HashMap<String, String>> map = gson.fromJson(fileOut, mapType);

        CustomImageLoader il = new CustomImageLoader();
        for (HashMap<String, String> tileData : map) {
            tileDictionary.put(Integer.valueOf(tileData.get("index")),
                    il.loadImage(GlobalConstants.TILE_DIR_PREFIX + tileData.get("fileName")));
        }
    }

    public void setTileMapTo(String filepath) {
        setTileMapTo(loadTileBitmapFromTextFile(filepath));
    }

    private void setTileMapTo(int[] tileLayoutBitmap) {
        for (int i = 0; i < GlobalConstants.GRID_HEIGHT; i++) {
            for (int j = 0; j < GlobalConstants.GRID_WIDTH; j++) {
                Image newSprite = tileDictionary.get(tileLayoutBitmap[i * GlobalConstants.GRID_WIDTH + j]);

                if (newSprite == null) {
                    System.err.println("Null sprite");
                    return;
                }

                tiles[i][j].getSprite().setFill(new ImagePattern(newSprite));
            }
        }
    }

    public void setTile(Vector2i coords, Image img) {
        tiles[coords.x()][coords.y()].getSprite().setFill(new ImagePattern(img));
    }

    private int[] loadTileBitmapFromTextFile(String filepath) {
        CustomFileReader fr = new CustomFileReader();
        String out = fr.readFile(filepath).replaceAll("\n", " ");
        int[] array = Arrays.stream(out.split(" ")).mapToInt(Integer::parseInt).toArray();

        if (array == null) {
            System.err.println("Null array");
            return new int[0];
        }

        return array;
    }

    public class Tile extends StackPane {
        private Rectangle sprite = new Rectangle(GlobalConstants.TILE_SIZE, GlobalConstants.TILE_SIZE);
        private boolean canCollide;

        public Tile() {
            getChildren().add(sprite);
        }

        public void setSprite(Paint value) {
            sprite.setFill(value);
        }

        public Rectangle getSprite() {
            return this.sprite;
        }

        public boolean canCollide() {
            return canCollide;
        }
    }
}
