package it.unicam.cs.mpgc.rpg130730.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import static java.util.Map.entry;

import it.unicam.cs.mpgc.rpg130730.entities.Enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.util.CustomResourceFileReader;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;

public final class LevelEditor {
    private final static String LEVELS_DIR = "/home/tom/Projects/sword-game/app/src/main/resources/levels/";

    @SuppressWarnings("null")
    public static void main(String[] args) {
        // LevelData lvl1 = new LevelData(
        // LevelData.stringToIntArray(new
        // CustomResourceFileReader().read("/levels/first_level.txt")),
        // Map.ofEntries(
        // entry(new Vector2(2, 3), EnemyType.PIG)));

        // LevelData lvl2 = new LevelData(
        // LevelData.stringToIntArray(new
        // CustomResourceFileReader().read("/levels/second_level.txt")),
        // Map.ofEntries(
        // entry(new Vector2(2, 3), EnemyType.PIG),
        // entry(new Vector2(5, 5), EnemyType.PIG)));

        // LevelEditor.createLevelDataFile("level1.dat", lvl1);
        // LevelEditor.createLevelDataFile("level2.dat", lvl2);
        System.out.println(LevelEditor.loadLevelDataFile("level1.dat"));
    }

    private static void createLevelDataFile(String filename, LevelData data) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(LEVELS_DIR + filename));
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LevelData loadLevelDataFile(String filename) {
        ObjectInputStream ois;
        LevelData levelData = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(LEVELS_DIR + filename));
            levelData = (LevelData) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (levelData == null)
            throw new NullPointerException(levelData + " object is null");
        return levelData;
    }

    public record LevelData(int[] tilesArragementData, Map<Vector2, EnemyType> enemyData) implements Serializable {
        public static int[] stringToIntArray(String tiles) {
            int[] arr = Arrays.stream(tiles.replaceAll("\r\n|[\r\n]", " ").split(" ")).mapToInt(Integer::parseInt)
                    .toArray();
            if (arr == null)
                throw new NullPointerException(arr + " array is null");
            return arr;
        }

        @Override
        public final String toString() {
            StringBuilder sb = new StringBuilder();
            double x = Tilemap.TILEMAP_DIMENSIONS.x();
            double y = Tilemap.TILEMAP_DIMENSIONS.y();
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    sb.append(tilesArragementData[(int) (i * x + j)]);
                    sb.append(" ");
                }
                sb.append("\n");
            }
            sb.append(enemyData);
            String string = sb.toString();
            if (string == null)
                throw new NullPointerException(string + " string is null");
            return string;
        }
    }
}
