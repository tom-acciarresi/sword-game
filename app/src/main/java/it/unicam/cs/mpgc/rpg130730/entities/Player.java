package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.Optional;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.GameLoop;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.RoomTransition;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player extends Character2D {
    // #region constants
    private static final int DEFAULT_PLAYER_SPEED = 400; // px/s
    public static final int DEFAULT_PLAYER_HEALTH = 5;
    public static final int DEFAULT_PLAYER_DAMAGE = 1;
    private static final int DAMAGE_COOLDOWN_FRAMES = 30;
    private static final int ATTACK_DURATION_FRAMES = 10;
    private static final int ATTACK_COOLDOWN_FRAMES = 25 + ATTACK_DURATION_FRAMES;
    // #endregion

    private Vector2 attackDirection = Vector2.DOWN;
    private Vector2 movementDirection = Vector2.ZERO;

    private AnimationPlayer animationPlayer;
    private Rectangle sword = new Rectangle(28, 48,
            new ImagePattern(AssetLibrary.SWORD_SPRITE));

    private int damageCooldown;

    private boolean isAttacking = false;
    private int attackDurationCounter;
    private int attackCooldown;

    private int kills;

    public Player() {
        super();
        setHealth(DEFAULT_PLAYER_HEALTH);

        animationPlayer = new AnimationPlayer(AssetLibrary.getAnimation("knight/idle_down"));
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

        attackDirection = InputMap.getAttackDirection();
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
        // Launcher.getSceneManager().getLevelContainer().getChildren()
        // .add(new Rectangle(bb.getMinX(), bb.getMinY(), bb.getWidth(),
        // bb.getHeight()));
        Optional<Enemy> collidesWithEnemy = CollisionSystem.collidesWithEnemy(hitBox);
        if (collidesWithEnemy.isPresent()) {
            Enemy enemy = collidesWithEnemy.get();
            enemy.setHealth(enemy.getHealth() - 1);
        }
    }

    // Bounds swordHitBox = sword.getBoundsInParent();
    // Launcher.getSceneManager().getLevelContainer().getChildren().add(new
    // Rectangle(swordHitBox.getMinX(),
    // swordHitBox.getMinY(), swordHitBox.getWidth(), swordHitBox.getHeight()));
    // Optional<Enemy> enemyHit = CollisionSystem.collidesWithEnemy(swordHitBox);
    // System.out.println(enemyHit);
    // if (enemyHit.isPresent()) {
    // Enemy enemyInstance = enemyHit.get();
    // enemyInstance.setHealth(enemyInstance.getHealth() - DEFAULT_PLAYER_DAMAGE);
    // }

    private void handleMovement(double timeDelta) {
        movementDirection = acceptsInput() ? InputMap.getMovementInput() : Vector2.ZERO;
        if (movementDirection.equals(Vector2.ZERO))
            return;

        double movementValue = DEFAULT_PLAYER_SPEED * GameLoop.getTimeDelta();
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

        Optional<Enemy> enemy = CollisionSystem.collidesWithEnemy(playerBounds);
        if (enemy.isPresent()) {
            damageCooldown = DAMAGE_COOLDOWN_FRAMES;
            Enemy enemyInstance = enemy.get();
            if (enemyInstance == null)
                throw new NullPointerException();
            collide(enemyInstance);
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
        Optional<RoomTransition> transition = CollisionSystem.enteredTransition(getCollisionBounds());
        if (transition.isPresent())
            transition.get().enter();
    }

    private void handleAnimation() {
        setViewOrder(-getPosition().y());

        animationPlayer.tick();
        setSprite(animationPlayer.getCurrFrame());

        String animationIdentifier;
        if (!movementDirection.equals(Vector2.ZERO))
            animationIdentifier = "knight/walk_" + movementDirection.cardinalDirectionString();
        else
            animationIdentifier = "knight/idle_" + movementDirection.cardinalDirectionString();

        if (isAttacking)
            animationIdentifier = "knight/attack_" + attackDirection.cardinalDirectionString();

        Animation newAnimation = AssetLibrary.getAnimation(animationIdentifier);
        if (!animationPlayer.getCurrAnimation().equals(newAnimation))
            animationPlayer.changeTo(newAnimation);
    }

    private void gameOver() {
        SaveSystem.deleteSave();
        Launcher.quitWithoutSaving();
    }
}
