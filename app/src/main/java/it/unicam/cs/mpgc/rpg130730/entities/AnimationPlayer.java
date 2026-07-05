package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.List;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.GameLoop.Updatable;
import javafx.scene.image.Image;

public class AnimationPlayer implements Updatable {
    private Animation currAnimation;

    @Nullable
    private Image currFrame;
    private int i;
    private int tickInterval;
    private int ticksLeft;

    public AnimationPlayer(Animation startingAnimation) {
        currAnimation = startingAnimation;

        currFrame = currAnimation.frames().getFirst();
        if (currAnimation.fps() != 0)
            // 60 fps / Ani FPS = amount of ticks per animation frame
            tickInterval = Launcher.TARGET_FRAMERATE / currAnimation.fps();
    }

    @Override
    public void update(double timeDelta) {
        if (currAnimation.fps() == 0)
            return;

        if (ticksLeft-- == 0) {
            i = (i + 1) % currAnimation.frames().size();
            currFrame = currAnimation.frames().get(++i);
            ticksLeft = tickInterval;
            System.out.println(currFrame);
        }
    }

    public record Animation(List<Image> frames, int fps) {
    }
}
