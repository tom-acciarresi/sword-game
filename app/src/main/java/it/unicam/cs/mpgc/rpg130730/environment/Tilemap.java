package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Arrays;
import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.AppLauncher;
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

        int[] info = loadTilemapInfoFromTextFile();

        tileDictionary.put(1, new Image(
                getClass().getResource(AppLauncher.FILEPATH_PREFIX + "images/tiles/testtile.png").toExternalForm()));
        tileDictionary.put(2, new Image(getClass().getResource(
                AppLauncher.FILEPATH_PREFIX + "images/tiles/testtile2.png").toExternalForm()));

        setTilemapTo(info);
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

    // TODO populate dict with sprites
    private boolean loadTileSpritesWithJSON() {
        // Gson gson = new Gson();
        // String jsonString = null;
        // try {
        // File file = new File();
        // FileReader in = new FileReader(file);
        // JsonReader reader = new JsonReader(in);
        // jsonString = gson.fromJson(reader, String.class);
        // } catch (Exception e) {
        // System.err.println("ERROR\n");
        // }
        // if (jsonString == null)
        return false;

        // System.out.println(jsonString);
        // return true;

    }

    // TODO make sure array size is correct and integers in dictionary as keys?
    private int[] loadTilemapInfoFromTextFile() {
        CustomFileReader fr = new CustomFileReader();
        String out = fr.readFile("text/layout.txt").replaceAll("\n", " ");
        return Arrays.stream(out.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    private void setTilemapTo(int[] stuff) {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                tiles[i][j].setImage(tileDictionary.get(stuff[i * GRID_WIDTH + j]));
            }
        }
    }
}
