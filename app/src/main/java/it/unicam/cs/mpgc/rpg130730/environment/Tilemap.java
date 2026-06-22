package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg130730.util.CustomFileReader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Draws tiles in a grid
 *
 * @author Tommaso Acciarresi
 */
public class Tilemap {
    public static final int TILE_SIZE = 64;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 10;

    private HashMap<Integer, Image> tileDictionary = new HashMap<Integer, Image>();

    @FXML
    private GridPane gridPane;

    private ImageView[][] tiles;

    @FXML
    public void initialize() {
        getTilePointersFromGridPane();

        loadTileSpritesWithJSON();

        int[] bitmap = loadTileBitmapFromTextFile();

        setTilemapTo(bitmap);
    }

    private void getTilePointersFromGridPane() {
        tiles = new ImageView[GRID_HEIGHT][GRID_WIDTH];

        for (Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (node instanceof ImageView)
                tiles[rowIndex == null ? 0 : rowIndex][columnIndex == null ? 0 : columnIndex] = (ImageView) node;
            else
                System.err.println("Not ImageView");
        }
    }

    private void loadTileSpritesWithJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CustomFileReader fr = new CustomFileReader();
        String fileOut = fr.readFile("/text/tiles.json");

        TypeToken<Map<Integer, String>> mapType = new TypeToken<Map<Integer, String>>() {
        };
        Map<Integer, String> map = gson.fromJson(fileOut, mapType);

        for (Entry<Integer, String> entry : map.entrySet()) {
            Integer index = entry.getKey();
            Image tileSprite = new Image(getClass().getResource("/images/tiles/" + entry.getValue()).toExternalForm());

            tileDictionary.put(index, tileSprite);
        }
    }

    private int[] loadTileBitmapFromTextFile() {
        CustomFileReader fr = new CustomFileReader();
        String out = fr.readFile("/text/layout.txt").replaceAll("\n", " ");
        return Arrays.stream(out.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    private void setTilemapTo(int[] tileLayoutBitmap) {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                tiles[i][j].setImage(tileDictionary.get(tileLayoutBitmap[i * GRID_WIDTH + j]));
            }
        }
    }
}
