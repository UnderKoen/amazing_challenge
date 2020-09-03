package nl.underkoen.amazing_challenge.models;

import java.util.Objects;

/**
 * @author Under_Koen
 */
public class Griever {
    private final Position position;
    private final int rotation;

    public Griever(Position position, int rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Position getPosition() {
        return position;
    }

    public int getRotation() {
        return rotation;
    }

    public Griever withPosition(Position position) {
        return new Griever(position, rotation);
    }

    public Griever withRotation(int rotation) {
        return new Griever(position, rotation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Griever)) return false;
        Griever griever = (Griever) o;
        return getRotation() == griever.getRotation() &&
                Objects.equals(getPosition(), griever.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getRotation());
    }

    public String toString() {
        return String.format("[%s, %s]", position, rotation);
    }
}
