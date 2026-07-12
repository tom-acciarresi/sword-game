package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.Tile;

public class CollisionHandler {
    private static Set<Tile> collTiles = new HashSet<Tile>();
    private static Set<Enemy> enemies = new HashSet<Enemy>();

    public static boolean addCollidableTile(Tile tile) {
        return collTiles.add(tile);
    }

    public static boolean removeCollidableTile(Tile tile) {
        return collTiles.remove(tile);
    }

    public static Set<Tile> getCollTiles() {
        return collTiles;
    }

    public static boolean addEnemy(Enemy enemy) {
        return enemies.add(enemy);
    }

    public static boolean removeEnemy(Enemy enemy) {
        return enemies.remove(enemy);
    }

    public static Set<Enemy> getEnemies() {
        return enemies;
    }
}
