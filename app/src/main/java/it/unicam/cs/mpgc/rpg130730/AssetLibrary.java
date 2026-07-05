package it.unicam.cs.mpgc.rpg130730;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.TileLayout;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.TileInfo;
import it.unicam.cs.mpgc.rpg130730.util.CustomFileReader;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import javafx.scene.image.Image;

public class AssetLibrary {
    private static final String TILE_DIR_PREFIX = "/images/tiles/";
    private static final String TILE_INFO_FILE = "/images/tiles/tiles.json";

    private static Map<String, Image> tileSprites = new HashMap<String, Image>();
    private static Map<Integer, TileInfo> tileInfo = new HashMap<Integer, TileInfo>();

    private static final String LEVEL_DIR_PREFIX = "/levels/";

    private static List<String> tileLayouts = new ArrayList<String>();

    private static final String ENTITY_DIR_PREFIX = "/images/entities/";
    private static final String ENTITY_INFO_SUFFIX = "/animations.json";

    private static Map<String, Image> animationSprites = new HashMap<String, Image>();
    private static Map<String, Animation> animations = new HashMap<String, Animation>();

    public AssetLibrary() {
        CustomFileReader fr = new CustomFileReader();
        CustomImageLoader il = new CustomImageLoader();

        loadTileSprites(il, fr);
        loadLevelData(fr);

        loadEntitySprites("knight", il, fr);
        loadEntitySprites("pig_enemy", il, fr);
    }

    private void loadTileSprites(CustomImageLoader il, CustomFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.readFile(TILE_INFO_FILE);

        List<Map<String, String>> arr = gson.fromJson(fileOut, new TypeToken<List<Map<String, String>>>() {
        });

        for (Map<String, String> tile : arr) {
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
        for (TileLayout level : TileLayout.ROOM_1.getDeclaringClass().getEnumConstants()) {
            tileLayouts.add(level.index(), fr.readFile(LEVEL_DIR_PREFIX + level.filename()).replaceAll("\n", " "));
        }
    }

    private static void loadEntitySprites(String entityIdentifier, CustomImageLoader il, CustomFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.readFile(ENTITY_DIR_PREFIX + entityIdentifier + ENTITY_INFO_SUFFIX);

        TypeToken<List<Map<String, Object>>> typeOfT = new TypeToken<List<Map<String, Object>>>() {
        };

        List<Map<String, Object>> arr = gson.fromJson(fileOut, typeOfT);

        for (Map<String, Object> animation : arr) {
            String key = entityIdentifier + "/" + (String) animation.get("name");

            // MULTI-TRACK DRIFTING!!
            int fps = (int) (double) animation.get("fps");

            @SuppressWarnings("unchecked")
            ArrayList<String> frameNames = (ArrayList<String>) animation.get("frames");

            List<Image> sprites = new ArrayList<Image>();

            for (String frameName : frameNames) {
                if (frameName == null) {
                    System.err.println("Null frame");
                    break;
                }

                animationSprites.put(frameName, il.loadImage(ENTITY_DIR_PREFIX + entityIdentifier + "/" + frameName));
                sprites.add(animationSprites.get(frameName));
            }

            animations.put(key, new Animation(sprites, fps));
        }
    }

    public static Map<Integer, TileInfo> getTileInfo() {
        return tileInfo;
    }

    public static List<String> getTileLayouts() {
        return tileLayouts;
    }

    public static Map<String, Animation> getAnimations() {
        return animations;
    }
}
