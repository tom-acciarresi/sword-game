package it.unicam.cs.mpgc.rpg130730.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.TileInfo;
import javafx.scene.image.Image;

public class AssetRegistry {
    public static final String TILE_DIR_PREFIX = "/images/tiles/";
    public static final String LEVEL_DIR_PREFIX = "/levels/";

    public static final String TILE_INFO_FILE = "/images/tiles/tiles.json";
    public static final String LEVEL_INFO_FILE = "/levels/levels.json";

    private static HashMap<String, Image> tileSprites = new HashMap<String, Image>();
    private static HashMap<Integer, TileInfo> tileInfo = new HashMap<Integer, TileInfo>();
    // private static HashMap<Integer, CharacterAnimation> levelData = new
    // HashMap<Integer, CharacterAnimation>();
    private static HashMap<String, String> levelData = new HashMap<String, String>();

    public AssetRegistry() {
        CustomFileReader fr = new CustomFileReader();
        CustomImageLoader il = new CustomImageLoader();

        loadTileSprites(il);
        // loadEntitySprites();
        loadLevelData(fr);
    }

    private void loadTileSprites(CustomImageLoader il) {
        Gson gson = new Gson();
        CustomFileReader fr = new CustomFileReader();
        String fileOut = fr.readFile(TILE_INFO_FILE);

        TypeToken<ArrayList<HashMap<String, String>>> mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        };

        ArrayList<HashMap<String, String>> arr = gson.fromJson(fileOut, mapType);

        for (HashMap<String, String> tile : arr) {
            Integer index = Integer.valueOf(tile.get("index"));
            String fileName = tile.get("fileName");
            boolean canCollide = Boolean.valueOf(tile.get("canCollide"));

            tileSprites.put(fileName, il.loadImage(TILE_DIR_PREFIX + fileName));

            tileInfo.put(index, new TileInfo(index, tileSprites.get(fileName), canCollide));
        }
    }

    private void loadLevelData(CustomFileReader fr) {
        // TODO Change
        levelData.put("first_level.txt", fr.readFile("/levels/first_level.txt"));
    }

    public static HashMap<Integer, TileInfo> getTileInfo() {
        return tileInfo;
    }

    public static HashMap<String, String> getLevelData() {
        return levelData;
    }
}
