package it.unicam.cs.mpgc.rpg130730.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads text files
 *
 * @author Tommaso Acciarresi
 */
public class CustomFileReader {

    public String readFile(String filepath) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream(filepath);
            return new String(resourceAsStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("Error reading file");
        return null;
    }
}
