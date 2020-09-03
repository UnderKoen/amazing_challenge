package nl.underkoen.amazing_challenge.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.ToIntBiFunction;

/**
 * @author Under_Koen
 */
public class Position {
    public static Position min(Collection<Position> positions) {
        return reduce(positions, Math::min);
    }

    public static Position min(Position... positions) {
        return min(Arrays.asList(positions));
    }

    public static Position max(Collection<Position> positions) {
        return reduce(positions, Math::max);
    }

    public static Position max(Position... positions) {
        return max(Arrays.asList(positions));
    }

    public static Position reduce(Collection<Position> positions, ToIntBiFunction<Integer, Integer> func) {
        return positions.stream().reduce((p, p2) -> {
            int x = func.applyAsInt(p.getX(), p2.getX());
            int y = func.applyAsInt(p.getY(), p2.getY());
            return new Position(x, y);
        }).orElseThrow();
    }

    private int x;
    private int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int setX(int x) {
        int old = this.x;
        this.x = x;
        return old;
    }

    public int addX(int x) {
        return setX(getX() + x);
    }

    public int getY() {
        return y;
    }

    public int setY(int y) {
        int old = this.y;
        this.y = y;
        return old;
    }

    public int addY(int y) {
        return setY(getY() + y);
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    public void set(Position position) {
        set(position.getX(), position.getY());
    }

    public void add(int x, int y) {
        addX(x);
        addY(y);
    }

    public void add(Position position) {
        add(position.getX(), position.getY());
    }

    public Position copy() {
        return new Position(x, y);
    }

    public Position copyAdd(int x, int y) {
        Position pos = copy();
        pos.add(x, y);
        return pos;
    }

    public Position copyAdd(Position position) {
        Position pos = copy();
        pos.add(position);
        return pos;
    }

    public int distance(Position position) {
        return Math.abs(getX() - position.getX()) + Math.abs(getY() - position.getY());
    }

    public int distanceOrigin() {
        return Math.abs(getX()) + Math.abs(getY());
    }

    public int[] asArray() {
        return new int[]{x, y};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getX() == position.getX() &&
                getY() == position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", x, y);
    }
}