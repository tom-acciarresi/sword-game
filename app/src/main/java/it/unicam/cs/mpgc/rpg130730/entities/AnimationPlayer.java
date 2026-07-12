package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import javafx.scene.image.Image;

public class AnimationPlayer {
    private Animation currAnimation = new Animation("null", new Image[0], 0);
    private Image currFrame = AssetLibrary.MISSING_SPRITE;
    private int frameIndex;
    private int tickInterval;
    private int ticksLeft;

    // #region constructors
    public AnimationPlayer(Animation startingAnimation) {
        changeTo(startingAnimation);
    }
    // #endregion

    // #region set-get
    public Animation getCurrAnimation() {
        return currAnimation;
    }

    public Image getCurrFrame() {
        return currFrame;
    }

    private void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex % currAnimation.getLength();
    }

    // #endregion
    public void tick() {
        if (currAnimation.fps() == 0)
            return;
        if (ticksLeft-- <= 0) {
            setFrameIndex(frameIndex + 1);
            Image image = currAnimation.getFrame(frameIndex);
            currFrame = image;
            ticksLeft = tickInterval;
        }
    }

    public void changeTo(Animation a) {
        currAnimation = a;
        currFrame = currAnimation.getFrame(0);
        tickInterval = Launcher.TARGET_FRAMERATE / currAnimation.fps();
        ticksLeft = tickInterval;
        frameIndex = 0;
    }
}
