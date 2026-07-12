package it.unicam.cs.mpgc.rpg130730.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import it.unicam.cs.mpgc.rpg130730.entities.Player;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager;
import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Level;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public class SaveSystem {
    // #region constants
    private static final String SAVE_DIR = System.getProperty("user.home") + "/Sword_Game/";
    private static final String SAVE_FILENAME = "sav.dat";
    private static final String SAVE_LOCATION = SAVE_DIR + SAVE_FILENAME;
    // #endregion

    public static void save() {
        Player player = SceneManager.getPlayer();
        SaveData data = new SaveData(
                SceneManager.getCurrLevel(),
                player.getPosition(),
                player.getHealth(),
                player.getKills());

        try {
            if (!Files.exists(Path.of(SAVE_DIR)))
                Files.createDirectories(Path.of(SAVE_DIR));

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_LOCATION));
            oos.writeObject(data);
            oos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static @org.jspecify.annotations.Nullable SaveData load() {
        if (!Files.exists(Path.of(SAVE_LOCATION)))
            return null;

        try {
            ObjectInputStream ois = new ObjectInputStream(
                    Files.newInputStream(Path.of(SAVE_LOCATION), StandardOpenOption.READ));
            return (SaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void deleteSave() {
        try {
            Files.deleteIfExists(Paths.get(SAVE_LOCATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public record SaveData(Level level, Vector2 pos, double health, int kills) implements java.io.Serializable {
        @Override
        public final @org.jspecify.annotations.Nullable String toString() {
            return String.format("level: %s, health: %.1f, kills: %d", level.toString(), health, kills);
        }
    }
}
