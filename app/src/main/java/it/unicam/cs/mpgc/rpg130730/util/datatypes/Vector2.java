package it.unicam.cs.mpgc.rpg130730.util.datatypes;

public record Vector2(double x, double y) implements java.io.Serializable {
    public static final Vector2 ZERO = new Vector2(0.0, 0.0),
            LEFT = new Vector2(-1.0, 0.0),
            RIGHT = new Vector2(1.0, 0.0),
            UP = new Vector2(0.0, -1.0),
            DOWN = new Vector2(0.0, 1.0);

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector2 normalized() {
        double length = length();

        if (length == 0)
            return ZERO;

        return new Vector2(x / length, y / length);
    }

    public Vector2 closestCardinalVector() {
        return Math.abs(x) > Math.abs(y)
                ? (x < 0
                        ? Vector2.LEFT
                        : Vector2.RIGHT)
                : (y < 0
                        ? Vector2.UP
                        : Vector2.DOWN);
    }

    public String cardinalDirectionString() {
        return Math.abs(x) > Math.abs(y)
                ? (x < 0
                        ? "left"
                        : "right")
                : (y < 0
                        ? "up"
                        : "down");
    }

    public Vector2 invert() {
        return new Vector2(-x, -y);
    }

    public Vector2 plus(Vector2 o) {
        return new Vector2(this.x + o.x, this.y + o.y);
    }

    public Vector2 minus(Vector2 o) {
        return this.plus(o.invert());
    }

    public Vector2 scalar(double k) {
        return new Vector2(x * k, y * k);
    }

    public double dot(Vector2 o) {
        return this.x * o.x + this.y * o.y;
    }

    public Vector2 distanceTo(Vector2 o) {
        return new Vector2(o.x - this.x, o.y - this.y);
    }

    public double distanceValueTo(Vector2 o) {
        return new Vector2(o.x - this.x, o.y - this.y).length();
    }

    @Override
    public String toString() {
        String output = String.format("%.2f, %.2f", x, y);
        if (output == null)
            throw new NullPointerException();
        return output;
    }
}
