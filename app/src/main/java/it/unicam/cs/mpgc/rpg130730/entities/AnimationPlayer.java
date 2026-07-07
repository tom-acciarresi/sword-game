package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.ArrayList;
import java.util.List;
import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import javafx.scene.image.Image;

public class AnimationPlayer implements Updatable {
    private Animation currAnimation = new Animation(new ArrayList<Image>(), 0);
    private Image currFrame = AssetLibrary.MISSING_SPRITE;
    private int frameIndex;
    private int tickInterval;
    private int ticksLeft;

    public AnimationPlayer(Animation startingAnimation) {
        subscribeToUpdates();
        changeTo(startingAnimation);
    }

    public void changeTo(Animation a) {
        currAnimation = a;
        currFrame = currAnimation.getFrame(0);
        tickInterval = Launcher.TARGET_FRAMERATE / currAnimation.fps();
        ticksLeft = tickInterval;
        frameIndex = 0;
    }

    private void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex % currAnimation.getLength();
    }

    private void increaseFrameIndex() {
        setFrameIndex(frameIndex + 1);
    }

    @Override
    public void update(double timeDelta) {
        if (currAnimation.fps() == 0)
            return;
        if (ticksLeft-- <= 0) {
            increaseFrameIndex();
            Image image = currAnimation.getFrame(frameIndex);
            currFrame = image;
            ticksLeft = tickInterval;
        }
    }

    public Animation getCurrAnimation() {
        return currAnimation;
    }

    public Image getCurrFrame() {
        return currFrame;
    }

    public record Animation(List<Image> frames, int fps) {
        public Image getFrame(int i) {
            Image image = frames.get(i);
            if (image == null)
                throw new NullPointerException(image + " is not a valid image");
            return image;
        }

        public int getLength() {
            return frames.size();
        }
    }
}
