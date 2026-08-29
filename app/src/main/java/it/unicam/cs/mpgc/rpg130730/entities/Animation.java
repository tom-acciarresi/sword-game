package it.unicam.cs.mpgc.rpg130730.entities;

import org.jspecify.annotations.Nullable;

import javafx.scene.image.Image;

/**
 * Animation
 *
 * @param identifier - animation identifier
 * @param frames     - animation duration in frames
 * @param fps        - animation playback speed
 *
 * @author Tommaso Acciarresi
 */
public record Animation(String identifier, Image[] frames, int fps) {
    // #region get-set
    public Image getFrame(int i) {
        Image image = frames[i];
        if (image == null)
            throw new NullPointerException();
        return image;
    }

    public int getLength() {
        return frames.length;
    }
    // #endregion

    @Override
    public final @Nullable String toString() {
        return String.format("name:%s\n%s\nfps: %d", identifier, frames.toString(), fps);
    }
}
