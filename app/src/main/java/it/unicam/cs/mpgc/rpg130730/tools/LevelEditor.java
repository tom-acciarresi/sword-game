package it.unicam.cs.mpgc.rpg130730.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class LevelEditor {
    private final static String LEVELS_DIR = "/home/tom/Projects/sword-game/app/src/main/resources/levels/";

    public static void main(String[] args) throws IOException {
        LevelEditor levelEditor = new LevelEditor();
        levelEditor.writeToDisk("test.txt", "bababooey");
    }

    private void writeToDisk(String filename, String content) throws IOException {
        Path path = Path.of(LEVELS_DIR, filename);
        BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
        bw.write(content);
        bw.close();
    }

    public class LevelData implements Serializable {
        // private int[] tilesArragementData;
        // private List enemyData;
    }
}
