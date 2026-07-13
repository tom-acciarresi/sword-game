package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.Random;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public class Enemy extends Character2D {
    // #region constants
    private static final int DEFAULT_ENEMY_SPEED = 150;
    // #endregion

    private EnemyData enemyData;
    private AnimationPlayer animationPlayer;

    private Vector2 initialDirection;
    private Vector2 currDirection;

    // #region constructors
    public Enemy(EnemyType type) {
        super();
        enemyData = type.info();

        initialDirection = getRandomStartingDirection();
        currDirection = initialDirection;

        animationPlayer = new AnimationPlayer(AssetLibrary.getAnimation(enemyData.identifier() + "/idle_down"));
        setSprite(animationPlayer.getCurrFrame());

        setHealth(enemyData.health());
    }
    // #endregion

    private Vector2 getRandomStartingDirection() {
        boolean randomBool = new Random().nextBoolean();
        return randomBool
                ? (randomBool
                        ? Vector2.LEFT
                        : Vector2.RIGHT)
                : (randomBool
                        ? Vector2.UP
                        : Vector2.DOWN);
    }

    // #region get-set
    @Override
    public void setHealth(double health) {
        super.setHealth(health);

        if (health <= 0) {
            die();
        }
    }
    // #endregion

    @Override
    public void update(double timeDelta) {
        handleMovement(timeDelta);
        handleAnimation();
    }

    private void handleMovement(double timeDelta) {
        Vector2 posDelta = currDirection.scalar(DEFAULT_ENEMY_SPEED * timeDelta);

        Vector2 newPos = getPosition().plus(posDelta);

        Vector2 oldPos = getPosition();

        move(newPos);

        if (getPosition().equals(oldPos)) {
            currDirection = currDirection.invert();
        }
    }

    private void handleAnimation() {
        setViewOrder(-getPosition().y());

        animationPlayer.tick();
        setSprite(animationPlayer.getCurrFrame());

        double x = currDirection.x(), y = currDirection.y();
        String direction = Math.abs(x) > Math.abs(y) ? (x < 0 ? "left" : "right") : (y < 0 ? "up" : "down");

        if (currDirection.equals(Vector2.ZERO)) {
            Animation newAnim = AssetLibrary.getAnimation(enemyData.identifier() + "/idle_" + direction);
            if (animationPlayer.getCurrAnimation().equals(newAnim)) {
                return;
            }
            animationPlayer.changeTo(newAnim);
            return;
        }

        Animation newAnim = AssetLibrary.getAnimation(enemyData.identifier() + "/walk_" + direction);
        if (animationPlayer.getCurrAnimation().equals(newAnim)) {
            return;
        }

        animationPlayer.changeTo(newAnim);
    }

    private void die() {
        Launcher.getSceneManager().deleteEnemy(this);
        Player player = Launcher.getSceneManager().getPlayer();
        player.setKills(player.getKills() + 1);
        System.out.println(String.format("Kills: %d", player.getKills()));
    }
}
