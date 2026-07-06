package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;

public class Enemy extends Entity {
    public enum EnemyType {
        PIG(new EnemyInfo(10, "pig"));

        private final EnemyInfo info;

        EnemyType(EnemyInfo info) {
            this.info = info;
        }

        public EnemyInfo info() {
            return info;
        }
    }

    private EnemyInfo test;
    private AnimationPlayer ap = new AnimationPlayer(AssetLibrary.getAnimation("pig" + "/walk_left"));

    public Enemy(EnemyType type) {
        super();

        test = type.info();

        setHealth(test.health());
    }

    public Enemy(EnemyType type, Vector2 position) {
        this(type);
        setPosition(position);
    }

    @Override
    public void update(double timeDelta) {
        setSprite(ap.getCurrFrame());
    }

    public record EnemyInfo(int health, String identifier) {
    }
}
