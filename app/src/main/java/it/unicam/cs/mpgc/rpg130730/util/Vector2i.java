package it.unicam.cs.mpgc.rpg130730.util;

public record Vector2i(int x, int y) {
    public static final Vector2i ZERO = new Vector2i(0, 0),
            LEFT = new Vector2i(-1, 0),
            RIGHT = new Vector2i(1, 0),
            UP = new Vector2i(0, 1),
            DOWN = new Vector2i(0, -1);

}
