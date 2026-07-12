package it.unicam.cs.mpgc.rpg130730.util.tools;

import static java.util.Map.entry;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Map;

import it.unicam.cs.mpgc.rpg130730.entities.Enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130730.environment.LevelData;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

@SuppressWarnings("null")
public class LevelEditor {
    // #region constants
    // FIXME: MAY CONTAIN PRIVATE INFO!
    private static final String LEVELS_DIR = "/home/tom/Projects/sword-game/app/src/main/resources/levels/";
    // #endregion

    public static void main(String[] args) {
        // #region level creation
        LevelData lvl1 = new LevelData(
                stringToIntArray("""
                            8 8 8 8 8 8 8 8 8 8 8 8
                            8 1 1 1 1 1 1 1 1 1 1 8
                            8 1 2 2 1 1 1 1 1 1 1 8
                            8 1 1 2 1 1 1 1 1 1 1 8
                            8 1 1 1 1 1 2 1 1 1 2 8
                            8 1 1 1 1 1 2 2 8 1 2 8
                            8 1 1 1 2 1 1 1 8 8 2 2
                            8 1 1 1 2 2 1 1 8 1 1 8
                            8 1 1 1 1 1 1 1 1 1 1 8
                            8 8 8 8 8 8 8 8 8 8 8 8
                        """),
                Map.ofEntries(
                        entry(new Vector2(3, 3), EnemyType.PIG)));

        LevelData lvl2 = new LevelData(
                stringToIntArray("""
                            8 8 8 8 8 8 8 8 8 8 8 8
                            8 1 1 1 1 1 1 1 1 1 1 8
                            8 1 1 2 1 8 1 8 1 1 1 8
                            8 1 1 1 1 8 8 8 1 1 1 8
                            8 1 1 2 2 1 1 1 2 1 1 8
                            8 1 1 1 2 1 1 1 1 1 1 8
                            1 1 1 1 1 1 2 1 2 1 1 8
                            8 1 1 1 1 1 1 2 2 1 1 8
                            8 1 1 1 1 1 1 1 1 1 1 8
                            8 8 8 8 8 8 8 8 8 8 8 8
                        """),
                Map.ofEntries(
                        entry(new Vector2(3, 1), EnemyType.PIG),
                        entry(new Vector2(5, 5), EnemyType.PIG)));
        // # endregion

        LevelEditor.createLevelDataFile("level1.dat", lvl1);
        LevelEditor.createLevelDataFile("level2.dat", lvl2);
    }

    private static void createLevelDataFile(String filename, LevelData data) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEVELS_DIR + filename));
            oos.writeObject(data);
            oos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] stringToIntArray(String s) {
        int[] array = Arrays.stream(s.strip().split("\\D+")).mapToInt(Integer::parseInt).toArray();

        if (array.length != Tilemap.TILE_AMOUNT)
            throw new IndexOutOfBoundsException();

        return array;
    }
}
