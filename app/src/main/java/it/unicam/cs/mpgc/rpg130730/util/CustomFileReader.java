package it.unicam.cs.mpgc.rpg130730.util;

import java.io.IOException;
import java.io.InputStream;

public class CustomFileReader {
    public String read(String filepath) {
        InputStream resourceAsStream = getClass().getResourceAsStream(filepath);
        try {
            return new String(resourceAsStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
