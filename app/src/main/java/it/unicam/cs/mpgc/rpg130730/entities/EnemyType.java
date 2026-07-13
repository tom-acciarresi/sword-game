package it.unicam.cs.mpgc.rpg130730.entities;

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
