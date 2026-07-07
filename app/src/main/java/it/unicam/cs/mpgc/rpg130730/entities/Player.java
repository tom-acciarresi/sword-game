package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.GameLoop;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.InputMap.KeyBind;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;

public class Player extends Entity {
    private static final int DEFAULT_PLAYER_SPEED = 400; // px/s
    public static final int DEFAULT_PLAYER_HEALTH = 5;

    private Vector2 movementInput = Vector2.ZERO;
    private boolean acceptsInput = true;
    private AnimationPlayer ap;

    public Player() {
        super();
        setHealth(DEFAULT_PLAYER_HEALTH);
        ap = new AnimationPlayer(AssetLibrary.getAnimation("knight/idle_down"));
    }

    public Player(Vector2 position) {
        this();
        setPosition(position);
    }

    public void update(double timeDelta) {
        handleMovement(timeDelta);
        handleAnimation();
    }

    private void handleMovement(double timeDelta) {
        movementInput = acceptsInput ? getMovementInput() : Vector2.ZERO;
        if (movementInput.equals(Vector2.ZERO))
            return;
        double movementDelta = DEFAULT_PLAYER_SPEED * GameLoop.getTimeDelta();
        Vector2 deltaPos = new Vector2(movementInput.x() * movementDelta, movementInput.y() * movementDelta);
        Vector2 newPos = getPosition().add(deltaPos);

        checkEnemyCollision();

        move(newPos);
    }

    private void checkEnemyCollision() {
        // TODO

    }

    private Vector2 getMovementInput() {
        int horizontalAxis = (InputMap.isKeyPressed(KeyBind.LEFT) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.RIGHT) ? +1 : 0);
        int verticalAxis = (InputMap.isKeyPressed(KeyBind.UP) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.DOWN) ? +1 : 0);
        return new Vector2(horizontalAxis, verticalAxis).normalized();
    }

    public void setAcceptsInput(boolean acceptsInput) {
        this.acceptsInput = acceptsInput;
    }

    @Override
    public void setHealth(double health) {
        if (health <= 0)
            gameOver();

        super.setHealth(health);
        Launcher.getGUI().updateBar(health / DEFAULT_PLAYER_HEALTH);
    }

    private void handleAnimation() {
        setSprite(ap.getCurrFrame());

        String direction = getPredominantDirection(movementInput);

        if (!acceptsInput || movementInput == Vector2.ZERO) {
            Animation newAnim = AssetLibrary.getAnimation("knight/idle_" + direction);
            if (ap.getCurrAnimation().equals(newAnim)) {
                return;
            }
            ap.changeTo(newAnim);
            return;
        }

        Animation newAnim = AssetLibrary.getAnimation("knight/walk_" + direction);
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

    private void gameOver() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameOver'");
    }
}
