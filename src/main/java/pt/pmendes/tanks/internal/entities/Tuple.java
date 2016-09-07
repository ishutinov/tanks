package pt.pmendes.tanks.internal.entities;

/**
 * Created by pmendes.
 */
public class Tuple<K> {
    private final K x;
    private final K y;

    public Tuple(K x, K y) {
        this.x = x;
        this.y = y;
    }

    public K getX() {
        return x;
    }

    public K getY() {
        return y;
    }
}
