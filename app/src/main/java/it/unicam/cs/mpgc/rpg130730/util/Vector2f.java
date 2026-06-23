package it.unicam.cs.mpgc.rpg130730.util;

public record Vector2f(double x, double y) {
    public static final Vector2f ZERO = new Vector2f(0.0, 0.0),
            LEFT = new Vector2f(-1.0, 0.0),
            RIGHT = new Vector2f(1.0, 0.0),
            UP = new Vector2f(0.0, 1.0),
            DOWN = new Vector2f(0.0, -1.0);
}
