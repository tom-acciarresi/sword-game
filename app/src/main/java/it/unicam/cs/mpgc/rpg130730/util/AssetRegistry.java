package it.unicam.cs.mpgc.rpg130730.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Level;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.TileInfo;
import javafx.scene.image.Image;

public class AssetRegistry {
    private static final String TILE_DIR_PREFIX = "/images/tiles/";
    private static final String LEVEL_DIR_PREFIX = "/levels/";

    private static final String TILE_INFO_FILE = "/images/tiles/tiles.json";

    private static HashMap<String, Image> tileSprites = new HashMap<String, Image>();
    private static HashMap<Integer, TileInfo> tileInfo = new HashMap<Integer, TileInfo>();
    // private static HashMap<Integer, CharacterAnimation> levelData = new
    // HashMap<Integer, CharacterAnimation>();
    private static ArrayList<String> levelData = new ArrayList<String>();

    public AssetRegistry() {
        CustomFileReader fr = new CustomFileReader();
        CustomImageLoader il = new CustomImageLoader();

        loadTileSprites(il, fr);
        // loadEntitySprites();
        loadLevelData(fr);
    }

    private void loadTileSprites(CustomImageLoader il, CustomFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.readFile(TILE_INFO_FILE);

        TypeToken<ArrayList<HashMap<String, String>>> dataType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        };

        ArrayList<HashMap<String, String>> arr = gson.fromJson(fileOut, dataType);

        for (HashMap<String, String> tile : arr) {
            Integer index = Integer.valueOf(tile.get("index"));
            String fileName = tile.get("fileName");
            boolean collides = Boolean.valueOf(tile.get("collides"));

            tileSprites.put(fileName, il.loadImage(TILE_DIR_PREFIX + fileName));

            Image image = tileSprites.get(fileName);
            if (image == null) {
                System.err.println("Null image");
                return;
            }

            tileInfo.put(index, new TileInfo(index, image, collides));
        }
    }

    private void loadLevelData(CustomFileReader fr) {
        for (Level level : Level.ROOM_1.getDeclaringClass().getEnumConstants()) {
            levelData.add(level.index(), fr.readFile(LEVEL_DIR_PREFIX + level.filename()).replaceAll("\n", " "));
        }
    }

    public static HashMap<Integer, TileInfo> getTileInfo() {
        return tileInfo;
    }

    public static ArrayList<String> getLevelData() {
        return levelData;
    }
}
