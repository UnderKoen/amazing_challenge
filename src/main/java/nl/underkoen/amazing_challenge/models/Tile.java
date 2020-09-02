package nl.underkoen.amazing_challenge.models;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Under_Koen
 */
public class Tile {
    private static Map<Tile, Image> cache = new HashMap<>();

    private MapType mapType;
    private int number;

    public Tile(MapType mapType, int number) {
        this.mapType = mapType;
        this.number = number;
    }

    public Image getImage() {
        if (cache.containsKey(this)) {
            return cache.get(this);
        } else {
            Image image = generateImage();
            cache.put(this, image);
            return image;
        }
    }

    private Image generateImage() {
        String path = switch (mapType) {
            case BOMB -> "bomb.png";
            case G0AL -> "goal.png";
            case BONUS -> "bonus.gif";
            case OBSTACLE -> getObstacleImage();
            case TURN -> "turn.png";
            case START -> "start.png";
            case COLOR -> getColorImage();
            case STARTED -> "";
        };

        String url = "https://amazing.hbo-ict.org/images/" + path;
        if (mapType == MapType.STARTED) url = "https://www.iconexperience.com/_img/v_collection_png/256x256/shadow/signal_flag_blue.png";
        return new Image(url, 32, 32, true, true);
    }

    private String getObstacleImage() {
        return switch (number) {
            case 0 -> "debris.gif";
            case 1 -> "bush.gif";
            case 2 -> "stone.gif";
            case 3 -> "wood.png";
            default -> throw new RuntimeException("Obstacle number is incorrect");
        };
    }

    private String getColorImage() {
        return switch (number) {
            case 0 -> "black.png";
            case 1 -> "purple.jpg";
            case 2 -> "blue.gif";
            case 3 -> "green.jpg";
            case 4 -> "yellow.jpg";
            case 5 -> "orange.jpg";
            case 6 -> "red.jpg";
            case 7 -> "gray.jpg";
            case 8 -> "white.png";
            default -> throw new RuntimeException("Color number is incorrect");
        };
    }

    public int getColor() {
        return switch (mapType) {
            case BOMB, STARTED, START -> 0;
            case G0AL, BONUS -> 4;
            case OBSTACLE -> -1;
            case TURN -> 2;
            case COLOR -> number;
        };
    }

    public int getColorBi() {
        return getColor() > 0 ? 1 : 0;
    }

    public MapType getMapType() {
        return mapType;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return getNumber() == tile.getNumber() &&
                getMapType() == tile.getMapType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMapType(), getNumber());
    }

    public enum MapType {
        BOMB("B"), G0AL("D"), BONUS("E"), OBSTACLE("O"), TURN("R"), START("S"), COLOR("C"), STARTED(null);

        public final String token;

        MapType(String token) {
            this.token = token;
        }

        public static MapType fromToken(String token) {
            return Arrays.stream(values()).filter(m -> m.token.equals(token)).findFirst().orElseThrow();
        }
    }

    @Override
    public String toString() {
        return "Tile{" +
                "mapType=" + mapType +
                ", number=" + number +
                '}';
    }
}
