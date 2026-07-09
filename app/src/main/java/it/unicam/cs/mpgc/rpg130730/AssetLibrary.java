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
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Levels;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.TileState;
import it.unicam.cs.mpgc.rpg130730.util.CustomResourceFileReader;
import it.unicam.cs.mpgc.rpg130730.util.CustomResourceImageLoader;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public final class AssetLibrary {
    public static final String GAME_ICON_PATH = "/images/icon.png";
    public static final Image GAME_ICON = new CustomResourceImageLoader().load(GAME_ICON_PATH);

    public static final String MISSING_SPRITE_PATH = "/images/null.png";
    public static final Image MISSING_SPRITE = new CustomResourceImageLoader().load(MISSING_SPRITE_PATH);

    private static final String TILE_DIR_PREFIX = "/images/tiles/";
    private static final String TILE_INFO_FILE = "/images/tiles/tiles.json";
    private static final Map<String, Image> TILE_SPRITES = new HashMap<String, Image>();
    private static final Map<Integer, TileState> TILE_INFO = new HashMap<Integer, TileState>();

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
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(TileState.class, new JsonDeserializer<TileState>() {
            @Override
            public TileState deserialize(@Nullable JsonElement json, @Nullable Type typeOfT,
                    @Nullable JsonDeserializationContext context)
                    throws JsonParseException {
                if (json == null)
                    throw new NullPointerException(json + " is null json");
                JsonObject jObject = json.getAsJsonObject();
                int index = jObject.get("index").getAsInt();
                String filename = jObject.get("filename").getAsString();
                if (filename == null)
                    throw new NullPointerException(filename + " is not a valid file name");
                Image sprite = il.load(TILE_DIR_PREFIX + filename);
                boolean collides = jObject.get("collides").getAsBoolean();
                TILE_SPRITES.put(filename, sprite);
                TileState tileState = new TileState(index, sprite, collides);
                TILE_INFO.put(index, tileState);
                return tileState;
            }
        });
        Gson gson = gb.create();
        String fileOut = fr.read(TILE_INFO_FILE);

        gson.fromJson(fileOut, TileState[].class);
    }

    private static void loadLevelData(CustomResourceFileReader fr) {
        Arrays.stream(Levels.ROOM_1.getDeclaringClass().getEnumConstants()).parallel().forEach(l -> {
            LEVEL_DATA.put(l.filename(), fr.read(LEVEL_DIR_PREFIX + l.filename()).replaceAll("\r\n|[\r\n]", " "));
        });
    }

    private static void loadEntitySprites(String entityIdentifier, CustomResourceImageLoader il,
            CustomResourceFileReader fr) {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Animation.class, new JsonDeserializer<Animation>() {
            @Override
            public Animation deserialize(@Nullable JsonElement json, @Nullable Type typeOfT,
                    @Nullable JsonDeserializationContext context)
                    throws JsonParseException {
                if (json == null)
                    throw new NullPointerException(json + " is null json");
                JsonObject jObject = json.getAsJsonObject();
                int fps = jObject.get("fps").getAsInt();
                String name = jObject.get("name").getAsString();
                if (name == null)
                    throw new NullPointerException(name + " animation name is null");
                String identifier = entityIdentifier + "/" + name;
                JsonArray arr = jObject.get("frames").getAsJsonArray();
                Image[] frames = new Image[arr.size()];
                for (int i = 0; i < frames.length; i++) {
                    String filename = arr.get(i).getAsString();
                    Image frame = il.load(ENTITY_DIR_PREFIX + entityIdentifier + "/" + filename);
                    ANIMATION_SPRITES.put(filename, frame);
                    frames[i] = frame;
                }

                Animation animation = new Animation(identifier, frames, fps);
                ANIMATIONS.put(identifier, animation);
                return animation;
            }
        });
        Gson gson = gb.create();
        String fileOut = fr.read(ENTITY_DIR_PREFIX + entityIdentifier + ENTITY_INFO_SUFFIX);

        gson.fromJson(fileOut, Animation[].class);
    }

    public static Image getTileSprite(String s) {
        Image image = TILE_SPRITES.get(s);
        if (image == null)
            throw new NullPointerException(image + " is not a valid image");
        return image;
    }

    public static TileState getTileInfo(int i) {
        TileState info = TILE_INFO.get(i);
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
