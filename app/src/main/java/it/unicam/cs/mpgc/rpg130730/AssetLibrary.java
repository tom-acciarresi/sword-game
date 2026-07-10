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
        CustomResourceFileReader fr = new CustomResourceFileReader();
        CustomResourceImageLoader il = new CustomResourceImageLoader();

        loadTileSprites(il, fr);

        loadLevelData(fr);

        loadEntitySprites("knight", il, fr);
        loadEntitySprites("pig", il, fr);
    }

    private static void loadTileSprites(CustomResourceImageLoader il, CustomResourceFileReader fr) {
        Gson gson = new GsonBuilder().registerTypeAdapter(TileState.class, new JsonDeserializer<TileState>() {
            @Override
            public TileState deserialize(@Nullable JsonElement json, @Nullable Type typeOfT,
                    @Nullable JsonDeserializationContext context)
                    throws JsonParseException {
                if (json == null)
                    throw new NullPointerException(json + " is null json");
                JsonObject jObject = json.getAsJsonObject();
                int index = jObject.get("index").getAsInt();
                String filename = jObject.get("filename").getAsString();
                Image sprite = il.load(TILE_DIR_PREFIX + filename);
                boolean collides = jObject.get("collides").getAsBoolean();
                TILE_SPRITES.put(filename, sprite);
                TileState tileState = new TileState(index, sprite, collides);
                TILE_INFO.put(index, tileState);
                return tileState;
            }
        }).create();

        String fileOut = fr.read(TILE_INFO_FILE);
        gson.fromJson(fileOut, TileState[].class);
    }

    // TODO change
    private static void loadLevelData(CustomResourceFileReader fr) {
        Arrays.stream(Levels.ROOM_1.getDeclaringClass().getEnumConstants()).forEach(l -> {
            LEVEL_DATA.put(l.filename(), fr.read(LEVEL_DIR_PREFIX + l.filename()).replaceAll("\r\n|[\r\n]", " "));
        });
    }

    private static void loadEntitySprites(String entityIdentifier, CustomResourceImageLoader il,
            CustomResourceFileReader fr) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Animation.class, new JsonDeserializer<Animation>() {
            @Override
            public Animation deserialize(@Nullable JsonElement json, @Nullable Type typeOfT,
                    @Nullable JsonDeserializationContext context) throws JsonParseException {
                if (json == null)
                    throw new NullPointerException(json + " is null json");
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
            }
        }).create();

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
