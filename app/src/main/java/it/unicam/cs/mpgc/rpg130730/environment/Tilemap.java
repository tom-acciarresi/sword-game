package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg130730.util.CustomFileReader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Draws tiles in a grid
 *
 * @author Tommaso Acciarresi
 */
public class Tilemap extends GridPane {
    private static final String TILE_INFO_FILE = "/text/tiles.json";
    private static final String TILE_DIR_PREFIX = "/images/tiles/";
    public static final int TILE_SIZE = 64;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 10;

    private HashMap<Integer, Image> tileDictionary = new HashMap<Integer, Image>();

    private Rectangle[][] tiles;

    public Tilemap() {
        createTiles();

        loadTileSpritesWithJSON();
    }

    public Tilemap(String filepath) {
        this();
        setTileMapTo(loadTileBitmapFromTextFile(filepath));
    }

    private void createTiles() {
        tiles = new Rectangle[GRID_HEIGHT][GRID_WIDTH];

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                Rectangle newTile = new Rectangle(64, 64, Color.MAGENTA);
                this.add(newTile, j, i);
                tiles[i][j] = newTile;
            }
        }
    }

    private void loadTileSpritesWithJSON() {
        Gson gson = new Gson();
        CustomFileReader fr = new CustomFileReader();
        String fileOut = fr.readFile(TILE_INFO_FILE);

        TypeToken<Map<Integer, String>> mapType = new TypeToken<Map<Integer, String>>() {
        };
        Map<Integer, String> map = gson.fromJson(fileOut, mapType);

        for (Entry<Integer, String> entry : map.entrySet()) {
            Integer index = entry.getKey();
            Image tileSprite = new Image(getClass().getResource(TILE_DIR_PREFIX + entry.getValue()).toExternalForm());

            tileDictionary.put(index, tileSprite);
        }
    }

    public void setTileMapTo(String filepath) {
        setTileMapTo(loadTileBitmapFromTextFile(filepath));
    }

    private void setTileMapTo(int[] tileLayoutBitmap) {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                tiles[i][j].setFill(new ImagePattern(
                        tileDictionary.get(tileLayoutBitmap[i * GRID_WIDTH + j])));
            }
        }
    }

    private int[] loadTileBitmapFromTextFile(String filepath) {
        CustomFileReader fr = new CustomFileReader();
        String out = fr.readFile(filepath).replaceAll("\n", " ");
        return Arrays.stream(out.split(" ")).mapToInt(Integer::parseInt).toArray();
    }
}
