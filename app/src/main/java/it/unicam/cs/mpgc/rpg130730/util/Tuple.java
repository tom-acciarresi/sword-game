package it.unicam.cs.mpgc.rpg130730.util;

/**
 * Tuple datatype cause java still doesn't have these apparently
 *
 * @author Tommaso Acciarresi
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Tuple<?, ?>))
            return false;
        Tuple<?, ?> mc = (Tuple<?, ?>) obj;

        boolean sameX = this.x == null ? mc.x == null : this.x.equals(mc.x);
        boolean sameY = this.y == null ? mc.y == null : this.y.equals(mc.y);
        return sameX && sameY;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
