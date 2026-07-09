package it.unicam.cs.mpgc.rpg130730.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public final class LevelEditor {
    private final static String LEVELS_DIR = "/home/tom/Projects/sword-game/app/src/main/resources/levels/";

    public static void main(String[] args) throws IOException {
        LevelEditor levelEditor = new LevelEditor();
        levelEditor.writeToDisk("test.txt", "bababooey");
    }

    private void writeToDisk(String filename, String content) throws IOException {
        Path path = Path.of(LEVELS_DIR, filename);
        System.out.println(path);
        BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        bw.write("this is a test again");
        bw.close();
    }

    public class LevelData implements Serializable {
        private int[] tilesArragementData;
        private List<Map<String, Object>> enemyData;
    }
}
