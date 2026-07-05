package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.scene.image.Image;

public class Enemy extends Entity {
    public enum EnemyType {
        PIG(new EnemyInfo(10, new CustomImageLoader().loadImage("/images/entities/pig_enemy/down.png")));

        private final EnemyInfo info;

        EnemyType(EnemyInfo info) {
            this.info = info;
        }

        public EnemyInfo info() {
            return info;
        }
    }

    public Enemy(EnemyType type) {
        super();

        EnemyInfo info = type.info();
        setHealth(info.health());
        setSprite(info.sprite());
    }

    public Enemy(EnemyType type, Vector2 position) {
        this(type);
        setPosition(position);
    }

    @Override
    public void update(double timeDelta) {
        // TODO Auto-generated method stub
    }

    public record EnemyInfo(int health, Image sprite) {
    }
}
