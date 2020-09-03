package nl.underkoen.amazing_challenge.generator;

import nl.underkoen.amazing_challenge.controllers.PriceTable;

import java.util.List;

/**
 * @author Under_Koen
 */
public abstract class CodeRotation implements CodeConnection {
    protected int from;
    protected int to;

    public CodeRotation(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[" + from + " -> " + to + "]";
    }

    public static CodeRotation getBestRotation(int from, int to, PriceTable priceTable) {
        CodeRotation defaultConnection = new DefaultRotation(from, to);
//        CodeRotation loopConnection = new LoopRotation(from, to);

        List<CodeRotation> connections = List.of(defaultConnection/*, loopConnection*/);
        return connections.stream().reduce((c1, c2) -> c1.getPrice(priceTable) <= c2.getPrice(priceTable) ? c1 : c2).orElseThrow();
    }

    public static CodeRotation getBestRotationWithTurn(int from, int to, int turn, PriceTable priceTable) {
        RotationWithTurn defaultConnection = new RotationWithTurn(from, to, turn);

        List<CodeRotation> connections = List.of(defaultConnection/*, loopConnection*/);
        return connections.stream().reduce((c1, c2) -> c1.getPrice(priceTable) <= c2.getPrice(priceTable) ? c1 : c2).orElseThrow();
    }

    protected static class DefaultRotation extends CodeRotation {
        public DefaultRotation(int from, int to) {
            super(from, to);
        }


        public String generateCode() {
            int diff = from - to;
            if (diff == 0) return "\n";

            if (Math.abs(diff) == 2) return "draaiRechts\n" +
                    "draaiRechts\n";

            if (diff == -3 || diff == 1) return "draaiLinks\n";
            if (diff == 3 || diff == -1) return "draaiRechts\n";

            return "\n";
        }

        @Override
        public int getPrice(PriceTable priceTable) {
            int diff = from - to;
            if (diff == 0) return 0;

            if (Math.abs(diff) == 2) return getSingle(priceTable) * 2;

            return getSingle(priceTable);
        }

        protected int getSingle(PriceTable priceTable) {
            return priceTable.getFunctionLine() + priceTable.getRight();
        }
    }

    protected static class RotationWithTurn extends CodeRotation {
        public static final int WRONG = 10000;
        public static boolean random = false;

        private int turn;

        public RotationWithTurn(int from, int to, int turn) {
            super(from, to);
            this.turn = turn;
            random = false;
        }

        @Override
        public String generateCode() {
            switch (turn) {
                case 0 -> {
                    random = true;
                    return "zolang kompas != " + to + " {\n" +
                            "draaiLinks\n" +
                            "}\n";
                }
                case 1 -> {
                    int diff = from - to;
                    if (diff == 0) return "\n";

                    if (Math.abs(diff) == 2) return "draaiRechts\n";

                    if (diff == -3 || diff == 1) return "stapAchteruit\n" +
                            "stapVooruit\n" +
                            "draaiRechts\n";
                    if (diff == 3 || diff == -1) return "stapAchteruit\n" +
                            "stapVooruit\n";

                    return "\n";
                }
                case 2 -> {
                    int diff = from - to;
                    if (diff == 0) return "\n";

                    if (Math.abs(diff) == 2) return "draaiLinks\n" +
                            "draaiLinks\n";

                    if (diff == -3 || diff == 1) return "draaiRechts\n";
                    if (diff == 3 || diff == -1) return "draaiLinks\n";

                    return "\n";
                }
                case 3 -> {
                    int diff = from - to;
                    if (diff == 0) return "\n";

                    if (Math.abs(diff) == 2) return "draaiLinks\n";

                    if (diff == -3 || diff == 1) return "stapAchteruit\n" +
                            "stapVooruit\n";
                    if (diff == 3 || diff == -1) return "stapAchteruit\n" +
                            "stapVooruit\n" +
                            "draaiLinks\n";

                    return "\n";
                }
            }
            return "";
        }

        @Override
        public int getPrice(PriceTable priceTable) {
            switch (turn) {
                case 0 -> {
                    return priceTable.getWhileLine() + priceTable.getFunctionLine() + priceTable.getLeft() * 4 + priceTable.getUseCompass() * 4 + priceTable.getCompare() * 5;
                }
                case 1 -> {
                    int diff = from - to;
                    if (diff == 0) return 0;

                    if (Math.abs(diff) == 2) return getSingle(priceTable);

                    if (diff == -3 || diff == 1)
                        return getBackward(priceTable) + getForward(priceTable) + getSingle(priceTable) + WRONG;
                    if (diff == 3 || diff == -1) return getBackward(priceTable) + getForward(priceTable) + WRONG;

                    return 0;
                }
                case 2 -> {
                    int diff = from - to;
                    if (diff == 0) return 0;

                    if (Math.abs(diff) == 2) return getSingle(priceTable) * 2;

                    if (diff == -3 || diff == 1) return getSingle(priceTable);
                    if (diff == 3 || diff == -1) return getSingle(priceTable);

                    return 0;
                }
                case 3 -> {
                    int diff = from - to;
                    if (diff == 0) return 0;

                    if (Math.abs(diff) == 2) return getSingle(priceTable);

                    if (diff == -3 || diff == 1) return getBackward(priceTable) + getForward(priceTable) + WRONG;
                    if (diff == 3 || diff == -1)
                        return getBackward(priceTable) + getForward(priceTable) + getSingle(priceTable) + WRONG;

                    return 0;
                }
            }
            return 0;
        }

        protected int getSingle(PriceTable priceTable) {
            return priceTable.getFunctionLine() + priceTable.getRight();
        }

        protected int getForward(PriceTable priceTable) {
            return priceTable.getFunctionLine() + priceTable.getForward();
        }

        protected int getBackward(PriceTable priceTable) {
            return priceTable.getFunctionLine() + priceTable.getBack();
        }
    }
}
