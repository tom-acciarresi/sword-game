package it.unicam.cs.mpgc.rpg130730.util;

public record GlobalConstants() {
    public static final String APPLICATION_TITLE = "Sword Game";
    public static final int WINDOW_WIDTH = 768, WINDOW_HEIGHT = 640;
    public static final boolean IS_RESIZABLE = false;
    public static final int TARGET_FRAMERATE = 60;

    public static final String ICON_FILENAME = "/images/icon.png";
    public static final String TILE_INFO_FILE = "/levels/tiles.json";
    public static final String TILE_DIR_PREFIX = "/images/tiles/";

    public static final int TILE_SIZE = 64;
    public static final int GRID_WIDTH = 12, GRID_HEIGHT = 10;

    public static final int PLAYER_SPEED = 400; // px/s
}
