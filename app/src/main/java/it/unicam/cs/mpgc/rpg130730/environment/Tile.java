package it.unicam.cs.mpgc.rpg130730.environment;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    // #region constants
    public static final TileData NULL_TILE = new TileData(
            0,
            AssetLibrary.MISSING_SPRITE, false);
    // #endregion

    private TileData info = NULL_TILE;
    private Rectangle sprite = new Rectangle(
            TileGrid.TILE_SIZE,
            TileGrid.TILE_SIZE,
            new ImagePattern(info.sprite()));

    // #region constructors
    public Tile() {
        getChildren().add(sprite);
    }
    // #endregion

    // #region set-get
    public TileData getInfo() {
        return info;
    }
    // #endregion

    public void changeTileTo(int index) {
        TileData tileInfo = AssetLibrary.getTileInfo(index);
        info = tileInfo;
        sprite.setFill(new ImagePattern(info.sprite()));
    }

    @Override
    public @org.jspecify.annotations.Nullable String toString() {
        return String.format("info: %s, sprite: %s", info, sprite);
    }
}
