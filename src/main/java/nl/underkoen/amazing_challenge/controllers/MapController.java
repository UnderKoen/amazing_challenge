package nl.underkoen.amazing_challenge.controllers;

import nl.underkoen.amazing_challenge.models.Position;
import nl.underkoen.amazing_challenge.models.Tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Under_Koen
 */
public class MapController {
    private static final Random rnd = new Random();

    private final Context context;
    private final Map<Position, Integer> activatedBombs = new HashMap<>();
    private int x;
    private int y;
    private int rotation;
    private Tile current;
    private int currentGoal = 1;
    private int topGoal = 1;

    public MapController(Context context) {
        this.context = context;
        Tile[][] map = context.glade.getMap();
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                Tile tile = map[y][x];
                if (tile.getMapType() == Tile.MapType.START) {
                    current = new Tile(Tile.MapType.STARTED, tile.getNumber());
                    this.x = x;
                    this.y = y;
                } else if (tile.getMapType() == Tile.MapType.G0AL) {
                    topGoal = Math.max(topGoal, tile.getNumber());
                }
            }
        }
        if (current == null) throw new RuntimeException("There should be a start in the glade");
        rotation = current.getNumber();
    }

    private void move(int dx, int dy) {
        Tile next = context.glade.getTile(x + dx, y + dy);

        if (next.getMapType() == Tile.MapType.OBSTACLE) {
            context.spendMoney(context.priceTable.getPushObstacle(), "muur duwen");

            sleep();
            return;
        }

        context.glade.setTile(x, y, current);

        this.x += dx;
        this.y += dy;

        current = next;
        context.glade.setTile(x, y, new Tile(Tile.MapType.START, rotation));

        activateTile(current);
    }

    private void activateTile(Tile tile) {
        for (Position pos : activatedBombs.keySet()) {
            activatedBombs.compute(pos, (p, i) -> {
                if (i == null) return 0;
                if (i == 1) {
                    context.glade.setTile((int) pos.getX(), (int) pos.getY(), new Tile(Tile.MapType.OBSTACLE, 0));
                }
                return i - 1;
            });
        }

        int number = tile.getNumber();
        switch (tile.getMapType()) {
            case TURN -> {
                if (number == 0) {
                    rotate(rnd.nextInt(4));
                } else {
                    rotate(number);
                }
            }
            case BONUS -> {
                int score = (int) Math.pow(2, number);
                context.earnMoney(score);
                System.out.println("Bonus van " + score + " gepakt");

                current = new Tile(Tile.MapType.COLOR, 4);
            }
            case G0AL -> {
                if (number == currentGoal) {
                    currentGoal++;
                    current = new Tile(Tile.MapType.COLOR, 4);
                    if (currentGoal > topGoal) {
                        throw new CompleteException("You reached the finish!");
                    }
                }
            }
            case BOMB -> checkBomb();
        }
        sleep();
    }

    private void checkBomb() {
        Position pos = new Position(x, y);
        if (activatedBombs.containsKey(pos)) {
            if (activatedBombs.get(pos) == 0) {
                throw new CompleteException("You exploded :(");
            }
        } else {
            activatedBombs.put(pos, current.getNumber());
            checkBomb();
        }
    }

    private int getDX() {
        return switch (rotation) {
            case 1 -> 1;
            case 3 -> -1;
            case 0, 2 -> 0;
            default -> throw new RuntimeException("Incorrect rotation");
        };
    }

    private int getDY() {
        return switch (rotation) {
            case 0 -> -1;
            case 2 -> 1;
            case 1, 3 -> 0;
            default -> throw new RuntimeException("Incorrect rotation");
        };
    }

    private void rotate(int d) {
        rotation += d;
        rotation = rotation % 4;
        if (rotation < 0) rotation += 4;

        context.glade.setTile(x, y, new Tile(Tile.MapType.START, rotation));
    }

    private void sleep() {
        try {
            Thread.sleep(context.priceTable.getDelay());
        } catch (InterruptedException e) {
            throw new RuntimeException("Program stopped");
        }
    }

    public void forward() {
        context.spendMoney(context.priceTable.getForward(), "stap vooruit");

        move(getDX(), getDY());
    }

    public void back() {
        context.spendMoney(context.priceTable.getBack(), "stap achteruit");

        move(-getDX(), -getDY());
    }

    public void left() {
        context.spendMoney(context.priceTable.getLeft(), "draai links");

        rotate(-1);
        activateTile(current);
    }

    public void right() {
        context.spendMoney(context.priceTable.getRight(), "draai rechts");

        rotate(1);
        activateTile(current);
    }

    public int eye() {
        if (!context.hardwareContainer.isEyeEnabled()) throw new RuntimeException("Black white eye is not bought");
        context.spendMoney(context.priceTable.getUseEye(), "zwOog (" +  current.getColorBi() + ")");
        return current.getColorBi();
    }

    public int colorEye() {
        if (!context.hardwareContainer.isColorEyeEnabled()) throw new RuntimeException("Color eye is not bought");
        context.spendMoney(context.priceTable.getUseColorEye(), "kleurOog (" + current.getColor() + ")");
        return current.getColor();
    }

    public int compass() {
        if (!context.hardwareContainer.isCompassEnabled()) throw new RuntimeException("Compass is not bought");
        context.spendMoney(context.priceTable.getUseCompass(), "kompass (" + rotation + ")");
        return rotation;
    }
}
