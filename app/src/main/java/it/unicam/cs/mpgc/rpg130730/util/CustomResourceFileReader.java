package it.unicam.cs.mpgc.rpg130730.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;

public final class CustomResourceFileReader {
    public String read(String filepath) {
        try {
            String data = Files.newBufferedReader(Path.of(AssetLibrary.RESOURCE_FOLDER_PATH + filepath))
                    .readAllAsString();
            if (data == null)
                throw new NullPointerException(data + " file content is null");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void write(String filepath, String content) {

    }
}
