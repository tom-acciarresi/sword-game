package it.unicam.cs.mpgc.rpg130730.util.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ObjectResourceDeserializer {
    @SuppressWarnings("unchecked")
    public <T> T read(String filepath) {
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(
                    Files.newInputStream(Path.of(getClass().getResource(filepath).toURI()), StandardOpenOption.READ));
            return (T) ois.readObject();
        } catch (IOException | URISyntaxException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Error during object deserialization");
    }
}
