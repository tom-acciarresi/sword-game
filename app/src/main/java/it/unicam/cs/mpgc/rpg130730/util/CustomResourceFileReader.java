package it.unicam.cs.mpgc.rpg130730.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CustomResourceFileReader {
    public String read(String filepath) {
        try {
            String data = Files.newBufferedReader(Path.of(getClass().getResource(filepath).toURI())).readAllAsString();
            if (data == null)
                throw new NullPointerException(data + " file content is null");
            return data;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }
}
