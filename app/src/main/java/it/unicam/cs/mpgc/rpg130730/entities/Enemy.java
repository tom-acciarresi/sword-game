package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;

public class Enemy extends Entity {
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
    private AnimationPlayer ap;

    private static final Vector2 INITIAL_DIRECTION = System.currentTimeMillis() % 2 == 0 ? Vector2.RIGHT : Vector2.DOWN;
    private Vector2 currDirection = INITIAL_DIRECTION;

    public Enemy(EnemyType type) {
        super();

        enemyInfo = type.info();
        ap = new AnimationPlayer(AssetLibrary.getAnimation(enemyInfo.identifier() + "/idle_down"));

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
        ap.tick();
        setSprite(ap.getCurrFrame());

        double x = currDirection.x(), y = currDirection.y();
        String direction = Math.abs(x) > Math.abs(y) ? (x < 0 ? "left" : "right") : (y < 0 ? "up" : "down");

        if (currDirection == Vector2.ZERO) {
            Animation newAnim = AssetLibrary.getAnimation(enemyInfo.identifier() + "/idle_" + direction);
            if (ap.getCurrAnimation().equals(newAnim)) {
                return;
            }
            ap.changeTo(newAnim);
            return;
        }

        Animation newAnim = AssetLibrary.getAnimation(enemyInfo.identifier() + "/walk_" + direction);
        if (ap.getCurrAnimation().equals(newAnim)) {
            return;
        }

        ap.changeTo(newAnim);
    }

    public record EnemyInfo(double health, String identifier) {
    }
}
