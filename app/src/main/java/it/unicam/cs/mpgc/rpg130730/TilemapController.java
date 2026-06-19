package it.unicam.cs.mpgc.rpg130730;

// import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class TilemapController {
    public static final int TILE_SIZE = 64;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 10;

    @FXML
    private GridPane gridPane;

    private ImageView[][] tileset;

    @FXML
    public void initialize() {
        tileset = new ImageView[GRID_HEIGHT][GRID_WIDTH];

        for (Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (node instanceof ImageView)
                tileset[rowIndex == null ? 0 : rowIndex][columnIndex == null ? 0 : columnIndex] = (ImageView) node;
            else
                System.err.println("Not ImageView");
        }

        Image testImage = new Image(getClass().getResource("images/testtile.png").toExternalForm(), 64.0, 64.0, true,
                false);
        Image testImage2 = new Image(getClass().getResource("images/testtile2.png").toExternalForm(), 64.0, 64.0, true,
                false);

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                tileset[i][j].setImage(
                        (i * GRID_HEIGHT + j) % 7 == 0 || (i * GRID_HEIGHT + j) % 13 == 0 ? testImage2 : testImage);
            }
        }
    }
}
