package it.unicam.cs.mpgc.rpg130730.entities;

import javafx.scene.image.Image;

public record Animation(String identifier, Image[] frames, int fps) {
    // #region get-set
    public Image getFrame(int i) {
        Image image = frames[i];
        if (image == null)
            throw new NullPointerException(image + " is not a valid image");
        return image;
    }

    public int getLength() {
        return frames.length;
    }
    // #endregion

    @Override
    public final @org.jspecify.annotations.Nullable String toString() {
        return String.format("name:%s\n%s\nfps: %d", identifier, frames.toString(), fps);
    }
}
