package it.unicam.cs.mpgc.rpg130730.util;

import java.io.IOException;
import java.io.InputStream;

import it.unicam.cs.mpgc.rpg130730.AppLauncher;

public class CustomFileReader {

    /**
     * @param filepath - the file path sarting from `resources/[package]/`
     */
    public String readFile(String filepath) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream(AppLauncher.FILEPATH_PREFIX + filepath);
            return new String(resourceAsStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("Error reading file");
        return null;
    }
}
