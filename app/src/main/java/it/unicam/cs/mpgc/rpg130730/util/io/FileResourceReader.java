package it.unicam.cs.mpgc.rpg130730.util.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileResourceReader {
    public String read(String filepath) {
        try {
            String data = Files.newBufferedReader(Path.of(getClass().getResource(filepath).toURI())).readAllAsString();

            if (data == null)
                throw new NullPointerException();

            return data;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }
}
