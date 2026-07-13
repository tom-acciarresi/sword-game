package it.unicam.cs.mpgc.rpg130730.util.tools;

import static java.util.Map.entry;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import it.unicam.cs.mpgc.rpg130730.entities.EnemyType;
import it.unicam.cs.mpgc.rpg130730.environment.Level;
import it.unicam.cs.mpgc.rpg130730.environment.LevelData;
import it.unicam.cs.mpgc.rpg130730.environment.RoomTransitionData;
import it.unicam.cs.mpgc.rpg130730.environment.TileGrid;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

@SuppressWarnings("null")
public class LevelEditor {
    // #region constants
    // FIXME: MAY CONTAIN PRIVATE INFO!
    private static final String LEVELS_DIR = "/home/tom/Projects/sword-game/app/src/main/resources/levels/";
    // #endregion

    public static void main(String[] args) {
        // #region level creation
        RoomTransitionData roomTransitionData1 = new RoomTransitionData(
                new Vector2(11.75, 6).scalar(64), Level.ROOM_1, new Vector2(10.5, 5.9).scalar(64),
                new Vector2(-0.75, 6).scalar(64), Level.ROOM_2, new Vector2(0.5, 5.9).scalar(64));

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
                        entry(new Vector2(3, 3).scalar(64), EnemyType.PIG)),

                new HashSet<RoomTransitionData>(Arrays.asList(
                        roomTransitionData1)));

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
                        entry(new Vector2(3, 1).scalar(64), EnemyType.PIG),
                        entry(new Vector2(5, 5).scalar(64), EnemyType.PIG)),

                new HashSet<RoomTransitionData>(Arrays.asList(
                        roomTransitionData1)));
        // # endregion

        LevelEditor.writeToDisk("level1.dat", lvl1);
        LevelEditor.writeToDisk("level2.dat", lvl2);
    }

    private static void writeToDisk(String filename, LevelData data) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEVELS_DIR + filename));
            oos.writeObject(data);
            oos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created '" + filename + "'");
    }

    private static int[] stringToIntArray(String s) {
        int[] array = Arrays.stream(s.strip().split("\\D+")).mapToInt(Integer::parseInt).toArray();

        if (array.length != TileGrid.TILE_AMOUNT)
            throw new IndexOutOfBoundsException();

        return array;
    }
}
