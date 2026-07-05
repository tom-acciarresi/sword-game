package it.unicam.cs.mpgc.rpg130730.util;

/**
 * Represents a 2D vector
 *
 * @author Tommaso Acciarresi
 */
public record Vector2(double x, double y) {
    public static final Vector2 ZERO = new Vector2(0.0, 0.0),
            LEFT = new Vector2(-1.0, 0.0),
            RIGHT = new Vector2(1.0, 0.0),
            UP = new Vector2(0.0, 1.0),
            DOWN = new Vector2(0.0, -1.0);

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector2 normalized() {
        double length = length();

        if (length == 0)
            return ZERO;

        return new Vector2(x / length, y / length);
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x() + other.x(), this.y() + other.y());
    }

    public Vector2 invert() {
        return new Vector2(-this.x(), -this.y());
    }

    @Override
    public String toString() {
        return "(" + String.format("%.2f", x) + ", " + String.format("%.2f", y) + ")";
    }
}
