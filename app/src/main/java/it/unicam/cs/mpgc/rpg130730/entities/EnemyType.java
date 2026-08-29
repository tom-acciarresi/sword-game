package it.unicam.cs.mpgc.rpg130730.entities;

/**
 * List of all enemy types and their characteristics
 *
 * @author Tommaso Acciarresi
 */
public enum EnemyType {
    PIG(new EnemyData(1, "pig"));

    private final EnemyData info;

    private EnemyType(EnemyData info) {
        this.info = info;
    }

    public EnemyData info() {
        return info;
    }
}
