package it.unicam.cs.mpgc.rpg130730;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.unicam.cs.mpgc.rpg130730.entities.Animation;
import it.unicam.cs.mpgc.rpg130730.environment.Level;
import it.unicam.cs.mpgc.rpg130730.environment.LevelData;
import it.unicam.cs.mpgc.rpg130730.environment.TileData;
import it.unicam.cs.mpgc.rpg130730.util.io.FileResourceReader;
import it.unicam.cs.mpgc.rpg130730.util.io.ImageResourceLoader;
import it.unicam.cs.mpgc.rpg130730.util.io.ObjectResourceDeserializer;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class AssetLibrary {
    // #region constants
    private static final String APP_ICON_PATH = "/images/icon.png",
            MISSING_SPRITE_PATH = "/images/null.png",
            SWORD_SPRITE_PATH = "/images/entities/knight/sword.png",
            TILE_DIR_PREFIX = "/images/tiles/", TILE_INFO_FILE = "/images/tiles/tiles.json",
            LEVEL_DIR_PREFIX = "/levels/",
            ENTITY_DIR_PREFIX = "/images/entities/", ENTITY_INFO_SUFFIX = "/animations.json";

    public static final Image APP_ICON = new ImageResourceLoader().load(APP_ICON_PATH);

    public static final Image MISSING_SPRITE = new ImageResourceLoader().load(MISSING_SPRITE_PATH);

    public static final Image SWORD_SPRITE = new ImageResourceLoader().load(SWORD_SPRITE_PATH);

    private static final Map<String, Image> TILE_SPRITES = new HashMap<String, Image>();
    private static final Map<Integer, TileData> TILE_INFO = new HashMap<Integer, TileData>();

    private static final Map<String, LevelData> LEVEL_DATA = new HashMap<String, LevelData>();

    private static final Map<String, Image> ANIMATION_SPRITES = new HashMap<String, Image>();
    private static final Map<String, Animation> ANIMATIONS = new HashMap<String, Animation>();

    @Nullable
    public static final Font TITLE_FONT = getFont("Fira Sans", FontWeight.BOLD, FontPosture.REGULAR, 64),
            GUI_FONT = getFont("Fira Sans", FontWeight.BOLD, FontPosture.REGULAR, 32),
            TEXT_FONT = getFont("Sans", FontWeight.NORMAL, FontPosture.REGULAR, 24);
    // #endregion

    // #region get-set
    public static TileData getTileInfo(int i) {
        TileData info = TILE_INFO.get(i);
        if (info == null)
            throw new NullPointerException(info + " is not valid tile info");
        return info;
    }

    public static LevelData getLevelData(String s) {
        LevelData levelData = LEVEL_DATA.get(s);
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

    public static @Nullable Font getFont(String family, FontWeight weight, FontPosture posture, double size) {
        return Font.font(family, weight, posture, size);
    }
    // #endregion

    public static void initialize() {
        FileResourceReader fr = new FileResourceReader();
        ImageResourceLoader il = new ImageResourceLoader();
        ObjectResourceDeserializer od = new ObjectResourceDeserializer();

        loadTileSprites(il, fr);

        loadEntitySprites("knight", il, fr);
        loadEntitySprites("pig", il, fr);

        loadLevelData(od);
    }

    private static void loadTileSprites(ImageResourceLoader il, FileResourceReader fr) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TileData.class, (JsonDeserializer<TileData>) (JsonElement json,
                        Type typeOfT, JsonDeserializationContext context) -> {
                    JsonObject jObject = json.getAsJsonObject();

                    int index = jObject.get("index").getAsInt();
                    String filename = jObject.get("filename").getAsString();
                    Image sprite = il.load(TILE_DIR_PREFIX + filename);
                    boolean collides = jObject.get("collides").getAsBoolean();

                    TileData tileData = new TileData(index, sprite, collides);

                    TILE_SPRITES.put(filename, sprite);
                    TILE_INFO.put(index, tileData);
                    return tileData;
                }).create();

        String tileInfoFile = fr.read(TILE_INFO_FILE);
        gson.fromJson(tileInfoFile, TileData[].class);
    }

    private static void loadEntitySprites(String entityIdentifier, ImageResourceLoader il,
            FileResourceReader fr) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Animation.class, (JsonDeserializer<Animation>) (JsonElement json,
                        Type typeOfT, JsonDeserializationContext context) -> {
                    JsonObject jObject = json.getAsJsonObject();

                    int fps = jObject.get("fps").getAsInt();

                    String animationName = jObject.get("name").getAsString();
                    String animationIdentifier = entityIdentifier + "/" + animationName;

                    JsonArray filenameArray = jObject.get("frames").getAsJsonArray();
                    Image[] frames = new Image[filenameArray.size()];

                    for (int i = 0; i < frames.length; i++) {
                        String filename = filenameArray.get(i).getAsString();
                        String frameIdentifier = entityIdentifier + "/" + filename;
                        Image currFrame;

                        // Don't load same sprite twice
                        if (!ANIMATION_SPRITES.containsKey(frameIdentifier)) {
                            String filepath = ENTITY_DIR_PREFIX + frameIdentifier;
                            currFrame = il.load(filepath);
                            ANIMATION_SPRITES.put(frameIdentifier, currFrame);
                        } else {
                            currFrame = ANIMATION_SPRITES.get(frameIdentifier);
                        }

                        frames[i] = currFrame;
                    }

                    Animation animation = new Animation(animationIdentifier, frames, fps);
                    ANIMATIONS.put(animationIdentifier, animation);
                    return animation;
                }).create();

        String animationsFile = fr.read(ENTITY_DIR_PREFIX + entityIdentifier + ENTITY_INFO_SUFFIX);
        gson.fromJson(animationsFile, Animation[].class);
    }

    private static void loadLevelData(ObjectResourceDeserializer od) {
        Level[] levels = Level.ROOM_1.getDeclaringClass().getEnumConstants();

        Arrays.stream(levels).forEach(l -> {
            String levelFilename = l.filename();
            LevelData levelData = od.read(LEVEL_DIR_PREFIX + levelFilename);
            LEVEL_DATA.put(levelFilename, levelData);
        });
    }
}
