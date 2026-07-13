package it.unicam.cs.mpgc.rpg130730.environment;

import java.util.Map;
import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.entities.EnemyType;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public record LevelData(int[] tileData, Map<Vector2, EnemyType> enemyData, Set<RoomTransitionData> transitions)
        implements java.io.Serializable {
    @Override
    public final @org.jspecify.annotations.Nullable String toString() {
        StringBuilder sb = new StringBuilder();
        double GRID_WIDTH = 10;
        double GRID_HEIGHT = 12;
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                sb.append(tileData[(int) (i * GRID_WIDTH + j)]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        sb.append(enemyData);
        sb.append(transitions);

        return sb.toString();
    }
}
