package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.Tile;

public class CollisionHandler {
    private static List<Tile> collTiles = new ArrayList<Tile>();
    private static List<Enemy> enemies = new ArrayList<Enemy>();

    public static boolean addCollidableTile(Tile tile) {
        return collTiles.add(tile);
    }

    public static boolean removeCollidableTile(Tile tile) {
        return collTiles.remove(tile);
    }

    public static List<Tile> getCollTiles() {
        return collTiles;
    }

    public static boolean addEnemy(Enemy enemy) {
        return enemies.add(enemy);
    }

    public static boolean removeEnemy(Enemy enemy) {
        return enemies.remove(enemy);
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }
}
