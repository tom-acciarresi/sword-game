package it.unicam.cs.mpgc.rpg130730;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Levels;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.TileInfo;
import it.unicam.cs.mpgc.rpg130730.util.CustomResourceFileReader;
import it.unicam.cs.mpgc.rpg130730.util.CustomResourceImageLoader;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public final class AssetLibrary {
    public static final String RESOURCE_FOLDER_PATH = FileSystems.getDefault().getPath("").toAbsolutePath().toString()
            + "/src/main/resources";

    public static final String GAME_ICON_PATH = "/images/icon.png";
    public static final Image GAME_ICON = new CustomResourceImageLoader().load(GAME_ICON_PATH);

    public static final String MISSING_SPRITE_PATH = "/images/null.png";
    public static final Image MISSING_SPRITE = new CustomResourceImageLoader().load(MISSING_SPRITE_PATH);

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

    public static final Font TITLE_FONT = font("Fira Sans", FontWeight.BOLD, FontPosture.REGULAR, 64);
    public static final Font GUI_FONT = font("Fira Sans", FontWeight.BOLD, FontPosture.REGULAR, 32);

    public static void initialize() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                CustomResourceFileReader fr = new CustomResourceFileReader();
                CustomResourceImageLoader il = new CustomResourceImageLoader();

                loadTileSprites(il, fr);

                loadLevelData(fr);

                loadEntitySprites("knight", il, fr);
                loadEntitySprites("pig", il, fr);
            }
        };
        Thread t1 = new Thread(task);
        t1.run();
    }

    private static void loadTileSprites(CustomResourceImageLoader il, CustomResourceFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.read(TILE_INFO_FILE);
        List<Map<String, String>> arr = gson.fromJson(fileOut, new TypeToken<List<Map<String, String>>>() {
        });
        arr.parallelStream().forEach(t -> {
            Integer index = Integer.valueOf(t.get("index"));
            String fileName = t.get("fileName");
            boolean collides = Boolean.valueOf(t.get("collides"));
            TILE_SPRITES.put(fileName, il.load(TILE_DIR_PREFIX + fileName));
            if (fileName == null)
                throw new NullPointerException(fileName + " is not a valid file name");
            Image image = getTileSprite(fileName);
            TILE_INFO.put(index, new TileInfo(index, image, collides));
        });
    }

    private static void loadLevelData(CustomResourceFileReader fr) {
        Arrays.stream(Levels.ROOM_1.getDeclaringClass().getEnumConstants()).parallel().forEach(l -> {
            LEVEL_DATA.put(l.filename(), fr.read(LEVEL_DIR_PREFIX + l.filename()).replaceAll("\r\n|[\r\n]", " "));
        });
    }

    private static void loadEntitySprites(String entityIdentifier, CustomResourceImageLoader il,
            CustomResourceFileReader fr) {
        Gson gson = new Gson();
        String fileOut = fr.read(ENTITY_DIR_PREFIX + entityIdentifier + ENTITY_INFO_SUFFIX);
        TypeToken<List<Map<String, Object>>> typeOfT = new TypeToken<List<Map<String, Object>>>() {
        };
        List<Map<String, Object>> arr = gson.fromJson(fileOut, typeOfT);
        arr.parallelStream().forEach(a -> {
            String key = entityIdentifier + "/" + (String) a.get("name");

            // MULTI-CAST!!
            int fps = (int) (double) a.get("fps");

            @SuppressWarnings("unchecked")
            ArrayList<String> frameNames = (ArrayList<String>) a.get("frames");
            List<Image> sprites = new ArrayList<Image>();

            frameNames.parallelStream().forEachOrdered(s -> {
                Image frame = il.load(ENTITY_DIR_PREFIX + entityIdentifier + "/" + s);
                ANIMATION_SPRITES.put(s, frame);
                sprites.add(frame);
            });

            ANIMATIONS.put(key, new Animation(sprites, fps));
        });
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

    public static Font font(String family, FontWeight weight, FontPosture posture, double size) {
        Font font = Font.font(family, weight, posture, size);
        if (font == null) {
            Font defaultFont = Font.getDefault();
            if (defaultFont == null)
                throw new NullPointerException(defaultFont + " is null :(");
            return defaultFont;
        }
        return font;
    }
}
