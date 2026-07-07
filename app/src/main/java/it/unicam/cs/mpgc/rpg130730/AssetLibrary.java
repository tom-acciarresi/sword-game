package it.unicam.cs.mpgc.rpg130730;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.LevelData;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.TileInfo;
import it.unicam.cs.mpgc.rpg130730.util.CustomFileReader;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import javafx.scene.image.Image;

public class AssetLibrary {
    public static final String GAME_ICON_PATH = "/images/icon.png";
    public static final Image GAME_ICON = new CustomImageLoader().load(GAME_ICON_PATH);

    public static final String MISSING_SPRITE_PATH = "/images/null.png";
    public static final Image MISSING_SPRITE = new CustomImageLoader().load(MISSING_SPRITE_PATH);

    private static final String TILE_DIR_PREFIX = "/images/tiles/";
    private static final String TILE_INFO_FILE = "/images/tiles/tiles.json";
    private static final Map<String, Image> TILE_SPRITES = new HashMap<String, Image>();
    private static final Map<Integer, TileInfo> TILE_INFO = new HashMap<Integer, TileInfo>();

    private static final String LEVEL_DIR_PREFIX = "/levels/";
    private static final Map<String, String> LEVEL_DATA = new HashMap<String, String>();

    private static final String ENTITY_DIR_PREFIX = "/images/entities/";
    private static final String ENTITY_INFO_SUFFIX = "/animations.json";
    private static final Map<String, Image> ANIMATION_SPRITES = new HashMap<String, Image>();
    private static final Map<String, Animation> ANIMATIONS = new HashMap<String, Animation>();

    public static void initialize() {
        CustomFileReader fr = new CustomFileReader();
        CustomImageLoader il = new CustomImageLoader();

        loadTileSprites(il, fr);

        loadLevelData(fr);

        loadEntitySprites("knight", il, fr);
        loadEntitySprites("pig", il, fr);
    }

    private static void loadTileSprites(CustomImageLoader il, CustomFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.read(TILE_INFO_FILE);
        List<Map<String, String>> arr = gson.fromJson(fileOut, new TypeToken<List<Map<String, String>>>() {
        });
        for (Map<String, String> tile : arr) {
            Integer index = Integer.valueOf(tile.get("index"));
            String fileName = tile.get("fileName");
            boolean collides = Boolean.valueOf(tile.get("collides"));
            TILE_SPRITES.put(fileName, il.load(TILE_DIR_PREFIX + fileName));
            if (fileName == null)
                throw new NullPointerException(fileName + " is not a valid file name");
            Image image = getTileSprite(fileName);
            TILE_INFO.put(index, new TileInfo(index, image, collides));
        }
    }

    private static void loadLevelData(CustomFileReader fr) {
        for (LevelData level : LevelData.ROOM_1.getDeclaringClass().getEnumConstants()) {
            LEVEL_DATA.put(level.filename(), fr.read(LEVEL_DIR_PREFIX + level.filename()).replaceAll("\n", " "));
        }
    }

    private static void loadEntitySprites(String entityIdentifier, CustomImageLoader il, CustomFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.read(ENTITY_DIR_PREFIX + entityIdentifier + ENTITY_INFO_SUFFIX);
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
                ANIMATION_SPRITES.put(frameName, il.load(ENTITY_DIR_PREFIX + entityIdentifier + "/" + frameName));
                sprites.add(ANIMATION_SPRITES.get(frameName));
            }
            ANIMATIONS.put(key, new Animation(sprites, fps));
        }
    }

    private static Image getTileSprite(String s) {
        Image image = TILE_SPRITES.get(s);
        if (image == null)
            throw new NullPointerException(image + " is not a valid image");
        return image;
    }

    public static TileInfo getTileInfo(int i) {
        TileInfo info = TILE_INFO.get(i);
        if (info == null)
            throw new NullPointerException(info + " is not valid tile info");
        return info;
    }

    public static String getLevelData(String s) {
        String levelData = LEVEL_DATA.get(s);
        if (levelData == null)
            throw new NullPointerException(levelData + " is not valid level data");
        return levelData;
    }

    public static Animation getAnimation(String s) {
        Animation animation = ANIMATIONS.get(s);
        if (animation == null)
            throw new NullPointerException(animation + " is not a valid animation");
        return animation;
    }
}
