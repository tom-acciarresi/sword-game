package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.Objects;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.RoomTransition;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Player
 *
 * @author Tommaso Acciarresi
 */
public class Player extends Character2D {
    // #region constants
    private static final String IDENTIFIER = "knight";
    private static final int DEFAULT_SPEED = 400; // px/s
    public static final int DEFAULT_HEALTH = 5;
    public static final int DEFAULT_DAMAGE = 1;
    private static final int DAMAGE_COOLDOWN_FRAMES = 30;
    private static final int ATTACK_DURATION_FRAMES = 10;
    private static final int ATTACK_COOLDOWN_FRAMES = 25 + ATTACK_DURATION_FRAMES;

    private final AnimationPlayer animationPlayer;
    private final Rectangle sword = new Rectangle(28, 48,
            new ImagePattern(AssetLibrary.SWORD_SPRITE));
    // #endregion

    private Vector2 attackDirection = Vector2.DOWN;
    private Vector2 movementDirection = Vector2.ZERO;

    private int damageCooldown;

    private boolean isAttacking = false;
    private int attackDurationCounter;
    private int attackCooldown;

    private int kills;

    public Player() {
        super();
        setHealth(DEFAULT_HEALTH);

        animationPlayer = new AnimationPlayer(AssetLibrary.getInstance().getAnimation(IDENTIFIER + "/idle_down"));
        this.setSprite(animationPlayer.getCurrFrame());

        sword.setVisible(false);
        this.getChildren().add(sword);
    }

    // #region get-set
    @Override
    public void setHealth(double health) {
        super.setHealth(health);

        if (health <= 0) {
            System.out.println("You died!!!");
            gameOver();
        }
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getKills() {
        return kills;
    }
    // #endregion

    @Override
    public void update(double timeDelta) {
        checkAttack();
        handleMovement(timeDelta);
        checkEnemyCollision();
        checkRoomTransition();

        handleAnimation();
    }

    private void checkAttack() {
        if (!canAttack()) {
            attackCooldown--;

            if (attackDurationCounter > 0) {
                attackDurationCounter--;
                return;
            } else {
                sword.setVisible(false);
                isAttacking = false;
            }

            return;
        }

        attackDirection = InputMap.getInstance().getAttackDirection();
        if (attackDirection.equals(Vector2.ZERO))
            return;

        attack(attackDirection);
    }

    private boolean canAttack() {
        return attackCooldown <= 0;
    }

    private void attack(Vector2 attackDirection) {
        attackCooldown = ATTACK_COOLDOWN_FRAMES;
        attackDurationCounter = ATTACK_DURATION_FRAMES;
        isAttacking = true;

        moveSword(attackDirection.closestCardinalVector());
        hitScan();
    }

    private void moveSword(Vector2 attackDirection) {
        sword.setVisible(true);
        if (attackDirection.equals(Vector2.LEFT)) {
            sword.setTranslateX(-64 + 8);
            sword.setTranslateY(16);
            sword.setRotate(90);
        } else if (attackDirection.equals(Vector2.RIGHT)) {
            sword.setTranslateX(64 - 8);
            sword.setTranslateY(16);
            sword.setRotate(-90);
        } else if (attackDirection.equals(Vector2.UP)) {
            sword.setTranslateX(0 - 12);
            sword.setTranslateY(-64 + 8);
            sword.setRotate(180);
        } else if (attackDirection.equals(Vector2.DOWN)) {
            sword.setTranslateX(-8);
            sword.setTranslateY(64 - 8);
            sword.setRotate(0);
        } else
            throw new IllegalStateException("Attack direction not valid");
    }

    private void hitScan() {
        Bounds hitBox = this.getBoundsInParent();
        if (hitBox == null)
            throw new NullPointerException();
        Enemy enemy = CollisionSystem.getInstance().collidesWithEnemy(hitBox);
        if (enemy != null) {
            enemy.setHealth(enemy.getHealth() - 1);
        }
    }

    private void handleMovement(double timeDelta) {
        movementDirection = acceptsInput() ? InputMap.getInstance().getMovementInput() : Vector2.ZERO;
        if (movementDirection.equals(Vector2.ZERO))
            return;

        double movementValue = DEFAULT_SPEED * timeDelta;
        Vector2 deltaPos = new Vector2(movementDirection.x() * movementValue, movementDirection.y() * movementValue);

        move(getPosition().plus(deltaPos));
    }

    private boolean acceptsInput() {
        if (!canBeDamaged() || isAttacking)
            return false;
        return true;
    }

    private void checkEnemyCollision() {
        if (!canBeDamaged()) {
            playFlashingAnimation();
            damageCooldown--;
            return;
        }

        this.getSprite().setVisible(true);
        BoundingBox playerBounds = this.getCollisionBounds();

        Enemy enemy = CollisionSystem.getInstance().collidesWithEnemy(playerBounds);
        if (enemy != null) {
            damageCooldown = DAMAGE_COOLDOWN_FRAMES;
            collide(enemy);
        }
    }

    private void playFlashingAnimation() {
        this.getSprite().setVisible(System.currentTimeMillis() % 2 == 0);
    }

    private boolean canBeDamaged() {
        return damageCooldown <= 0;
    }

    private void collide(Enemy enemy) {
        knockback(enemy.getPosition().distanceTo(this.getPosition()));
        setHealth(getHealth() - 1);
    }

    private void knockback(Vector2 movementVector) {
        this.move(getPosition().plus(movementVector));
    }

    private void checkRoomTransition() {
        RoomTransition transition = CollisionSystem.getInstance().enteredTransition(getCollisionBounds());
        if (transition != null)
            transition.enter();
    }

    private void handleAnimation() {
        setViewOrder(-getPosition().y());

        animationPlayer.tick();
        setSprite(animationPlayer.getCurrFrame());

        String animationIdentifier;
        if (!movementDirection.equals(Vector2.ZERO))
            animationIdentifier = IDENTIFIER + "/walk_" + movementDirection.cardinalDirectionString();
        else
            animationIdentifier = IDENTIFIER + "/idle_" + movementDirection.cardinalDirectionString();

        if (isAttacking)
            animationIdentifier = IDENTIFIER + "/attack_" + attackDirection.cardinalDirectionString();

        Animation newAnimation = AssetLibrary.getInstance().getAnimation(animationIdentifier);
        if (!animationPlayer.getCurrAnimation().equals(newAnimation))
            animationPlayer.changeTo(newAnimation);
    }

    private void gameOver() {
        SaveSystem.getInstance().deleteSave();
        Launcher.quitWithoutSaving();
    }
}
