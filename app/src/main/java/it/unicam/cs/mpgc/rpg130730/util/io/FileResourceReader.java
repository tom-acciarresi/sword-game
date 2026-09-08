package it.unicam.cs.mpgc.rpg130730.util.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jspecify.annotations.Nullable;

/**
 * Reads files
 *
 * @author Tommaso Acciarresi
 */
public class FileResourceReader {
    public @Nullable String read(String filepath) {
        BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream(filepath));
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            for (int result = bis.read(); result != -1; result = bis.read()) {
                buf.write((byte) result);
            }
            return buf.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("Error reading file " + filepath);
        return null;
    }
}
