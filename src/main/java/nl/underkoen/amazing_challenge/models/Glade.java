package nl.underkoen.amazing_challenge.models;

import java.util.function.Consumer;

/**
 * @author Under_Koen
 */
public class Glade {
    private final Tile[][] map;
    private Consumer<Glade> updateListener;
    private int highestGoal = 1;

    public Glade(String map) {
        String[] tiles = map.split(";");
        this.map = new Tile[20][20];
        for (int i = 0; i < tiles.length; i++) {
            String tile = tiles[i];
            Tile.MapType type = Tile.MapType.fromToken(tile.substring(0,1));
            int num = Integer.parseInt(tile.substring(1,2));
            this.map[i/20][i%20] = new Tile(type, num);
            if (type == Tile.MapType.G0AL) highestGoal = Math.max(highestGoal, num);
        }
    }

    public Glade(Tile[][] map) {
        this.map = new Tile[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                this.map[i][j] = map[i][j];
            }
        }
    }

    public void setTile(int x, int y, Tile tile) {
        map[y][x] = tile;
        if (updateListener != null) updateListener.accept(this);
    }

    public Tile getTile(int x, int y) {
        return map[y][x];
    }
    public Tile getTile(Position position) {
        return getTile(position.getX(), position.getY());
    }

    public void setUpdateListener(Consumer<Glade> updateListener) {
        this.updateListener = updateListener;
    }

    public Tile[][] getMap() {
        return map;
    }

    public int getHighestGoal() {
        return highestGoal;
    }
}
