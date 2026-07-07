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

    private static final Vector2 INITIAL_DIRECTION = Vector2.DOWN;
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
    }

    private void handleMovement(double timeDelta) {
        Vector2 posDelta = getPosition().add(currDirection.scalarMult(150 * timeDelta));

        Vector2 oldPos = getPosition();

        move(posDelta);

        if (getPosition().equals(oldPos)) {
            currDirection = currDirection.invert();
        }
    }

    private void handleAnimation() {
        setSprite(ap.getCurrFrame());

        String direction = getPredominantDirection(currDirection);

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

    private String getPredominantDirection(Vector2 v) {
        String direction;
        double x = v.x();
        double y = v.y();
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0)
                direction = "left";
            else
                direction = "right";
        } else if (y < 0) {
            direction = "up";
        } else {
            direction = "down";
        }
        return direction;
    }

    public record EnemyInfo(double health, String identifier) {
    }
}
