package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.Random;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public class Enemy extends Character2D {
    public enum EnemyType {
        PIG(new EnemyInfo(1, "pig"));

        private final EnemyInfo info;

        EnemyType(EnemyInfo info) {
            this.info = info;
        }

        public EnemyInfo info() {
            return info;
        }
    }

    private EnemyInfo enemyInfo;
    private AnimationPlayer animationPlayer;

    private Vector2 initialDirection;
    private Vector2 currDirection;

    public Enemy(EnemyType type) {
        super();

        boolean randBoolean = new Random().nextBoolean();
        initialDirection = randBoolean
                ? (randBoolean ? Vector2.LEFT : Vector2.RIGHT)
                : (randBoolean ? Vector2.UP : Vector2.DOWN);
        currDirection = initialDirection;

        enemyInfo = type.info();
        animationPlayer = new AnimationPlayer(AssetLibrary.getAnimation(enemyInfo.identifier() + "/idle_down"));
        setSprite(animationPlayer.getCurrFrame());

        setHealth(enemyInfo.health());
    }

    public Enemy(EnemyType type, Vector2 position) {
        this(type);
        setPosition(position);
    }

    @Override
    public void update(double timeDelta) {
        handleMovement(timeDelta);
        handleAnimation();
        setViewOrder(-getPosition().y());
    }

    private void handleMovement(double timeDelta) {
        Vector2 posDelta = getPosition().plus(currDirection.scalar(150 * timeDelta));

        Vector2 oldPos = getPosition();

        move(posDelta);

        if (getPosition().equals(oldPos)) {
            currDirection = currDirection.invert();
        }
    }

    private void handleAnimation() {
        animationPlayer.tick();
        setSprite(animationPlayer.getCurrFrame());

        double x = currDirection.x(), y = currDirection.y();
        String direction = Math.abs(x) > Math.abs(y) ? (x < 0 ? "left" : "right") : (y < 0 ? "up" : "down");

        if (currDirection == Vector2.ZERO) {
            Animation newAnim = AssetLibrary.getAnimation(enemyInfo.identifier() + "/idle_" + direction);
            if (animationPlayer.getCurrAnimation().equals(newAnim)) {
                return;
            }
            animationPlayer.changeTo(newAnim);
            return;
        }

        Animation newAnim = AssetLibrary.getAnimation(enemyInfo.identifier() + "/walk_" + direction);
        if (animationPlayer.getCurrAnimation().equals(newAnim)) {
            return;
        }

        animationPlayer.changeTo(newAnim);
    }

    public record EnemyInfo(double health, String identifier) {
    }
}
