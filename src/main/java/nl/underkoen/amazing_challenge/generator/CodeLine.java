package nl.underkoen.amazing_challenge.generator;

import nl.underkoen.amazing_challenge.controllers.PriceTable;
import nl.underkoen.amazing_challenge.models.Glade;
import nl.underkoen.amazing_challenge.models.Position;
import nl.underkoen.amazing_challenge.models.Tile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Under_Koen
 */
public abstract class CodeLine implements CodeConnection {
    protected Position from;
    protected Position to;
    protected Glade glade;

    public CodeLine(Position from, Position to, Glade glade) {
        this.from = from;
        this.to = to;
        this.glade = glade;
    }

    public abstract int getRunPrice(PriceTable priceTable);

    public int getEarnings() {
        int earning = 0;

        Position min = Position.min(from, to);
        Position max = Position.max(from, to);

        for (int x = min.getX(); x < max.getX(); x++) {
            Tile tile = glade.getTile(x, min.getY());
            if (tile.getMapType() == Tile.MapType.BONUS) {
                earning += Math.pow(2, tile.getNumber());
            }
        }

        for (int y = min.getY(); y < max.getY(); y++) {
            Tile tile = glade.getTile(min.getX(), y);
            if (tile.getMapType() == Tile.MapType.BONUS) {
                earning += Math.pow(2, tile.getNumber());
            }
        }

        return 0;
    }

    public int getPrice(PriceTable priceTable) {
        return getRunPrice(priceTable) - getEarnings();
    }

    @Override
    public String toString() {
        return "[" + from + " -> " + to + "]";
    }

    public static CodeLine getBestLine(Position from, Position to, Glade glade, PriceTable priceTable) {
        Tile toTile = glade.getTile(to);

        DefaultLine defaultConnection = new DefaultLine(from, to, glade);
        LoopLine loopConnection = new LoopLine(from, to, glade);
        FinalLine finalConnection = toTile.getMapType() == Tile.MapType.G0AL && toTile.getNumber() == glade.getHighestGoal() ? new FinalLine(from, to, glade) : null;

        List<CodeLine> connections = Arrays.asList(defaultConnection, loopConnection, finalConnection);

        CodeLine line = connections.stream()
                .filter(Objects::nonNull)
                .reduce((c1, c2) -> c1.getPrice(priceTable) <= c2.getPrice(priceTable) ? c1 : c2)
                .orElseThrow();
        return line;
    }

    protected static class DefaultLine extends CodeLine {
        public DefaultLine(Position from, Position to, Glade glade) {
            super(from, to, glade);
        }

        @Override
        public String generateCode() {
            return "stapVooruit\n".repeat(Math.max(0, from.distance(to)));
        }

        @Override
        public int getRunPrice(PriceTable priceTable) {
            return singlePrice(priceTable) * from.distance(to);
        }

        protected int singlePrice(PriceTable priceTable) {
            int dis = from.distance(to);
            return priceTable.getFunctionLine() + priceTable.getForward() * dis;
        }
    }

    protected static class LoopLine extends CodeLine {
        private static int offset = 0;

        public static int getOffset() {
            return offset;
        }

        public LoopLine(Position from, Position to, Glade glade) {
            super(from, to, glade);
            offset = 0;
        }

        @Override
        public String generateCode() {
            int dis = from.distance(to);
            String code = "zolang a < " + (dis + offset) + " {\n" +
                    "a = a + 1\n" +
                    "stapVooruit\n" +
                    "}";
            offset += dis;
            return code;
        }

        @Override
        public int getRunPrice(PriceTable priceTable) {
            int dis = from.distance(to);
            return initPrice(priceTable)
                    + priceTable.getAssignment() * dis
                    + priceTable.getForward() * dis
                    + priceTable.getCompare() * (dis + 1)
                    + priceTable.getCalculate() * dis;
        }

        protected int initPrice(PriceTable priceTable) {
            return priceTable.getWhileLine() + priceTable.getAssignmentLine() + priceTable.getFunctionLine();
        }
    }

    protected static class FinalLine extends CodeLine {
        public FinalLine(Position from, Position to, Glade glade) {
            super(from, to, glade);
        }

        @Override
        public String generateCode() {
            return ("zolang 1 == 1 {\n" +
                    "stapVooruit\n" +
                    "}");
        }

        @Override
        public int getRunPrice(PriceTable priceTable) {
            int dis = from.distance(to);
            return initPrice(priceTable)
                    + priceTable.getForward() * dis
                    + priceTable.getCompare() * (dis + 1);
        }

        protected int initPrice(PriceTable priceTable) {
            return priceTable.getWhileLine() + priceTable.getFunctionLine();
        }
    }

    public static class ReverseLine extends CodeLine {
        private final CodeLine orginal;

        public ReverseLine(CodeLine orginal) {
            super(orginal.from, orginal.to, orginal.glade);
            this.orginal = orginal;
        }

        @Override
        public int getRunPrice(PriceTable priceTable) {
            int dis = from.distance(to);
            if (orginal instanceof LoopLine) {
                LoopLine loopLine = (LoopLine) orginal;
                return loopLine.initPrice(priceTable)
                        + priceTable.getAssignment() * dis
                        + priceTable.getBack() * dis
                        + priceTable.getCompare() * (dis + 1)
                        + priceTable.getCalculate() * dis;

            }
            if (orginal instanceof DefaultLine) {
                return priceTable.getFunctionLine() + priceTable.getBack() * dis;
            }
            return orginal.getPrice(priceTable);
        }

        @Override
        public String generateCode() {
            return orginal.generateCode().replace("Vooruit", "Achteruit");
        }
    }
}
