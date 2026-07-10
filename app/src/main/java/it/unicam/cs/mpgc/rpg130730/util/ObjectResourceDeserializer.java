package it.unicam.cs.mpgc.rpg130730.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ObjectResourceDeserializer {
    @SuppressWarnings("unchecked")
    public <T> T read(String filepath) {
        T obj;
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(
                    Files.newInputStream(Path.of(getClass().getResource(filepath).toURI()), StandardOpenOption.READ));
            obj = (T) ois.readObject();
            // if (obj == null)
            // throw new NullPointerException(obj + " object is null");
            return obj;
        } catch (IOException | URISyntaxException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }
}
