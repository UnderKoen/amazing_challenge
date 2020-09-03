package nl.underkoen.amazing_challenge.generator;

import nl.underkoen.amazing_challenge.controllers.PriceTable;
import nl.underkoen.amazing_challenge.models.*;
import nl.underkoen.amazing_challenge.ui.CustomPrintStream;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Under_Koen
 */
public class Generator {
    public static Graph<Griever, CodeConnection> createGraph(Glade glade, PriceTable priceTable) {
        Graph<Griever, CodeConnection> graph = new DirectedWeightedMultigraph<>(CodeConnection.class);

        List<Position> positions = new ArrayList<>();
        List<Griever> grievers = new ArrayList<>();
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                Tile tile = glade.getTile(x, y);
                Position pos = new Position(x, y);
                if (tile.getMapType() != Tile.MapType.OBSTACLE) {
                    positions.add(pos);
                    for (int i = 0; i < 4; i++) {
                        Griever griever = new Griever(pos, i);
                        graph.addVertex(griever);
                        grievers.add(griever);
                    }
                }
            }
        }

        for (Griever griever : grievers) {
            Position position = griever.getPosition();
            switch (griever.getRotation()) {
                case 2 -> {
                    for (int y = position.getY() + 1; y < 20; y++) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(position.getX(), y)), glade, priceTable)) break;
                    }
                }
                case 1 -> {
                    for (int x = position.getX() + 1; x < 20; x++) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(x, position.getY())), glade, priceTable)) break;
                    }
                }
                case 0 -> {
                    for (int y = position.getY() - 1; y > 0; y--) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(position.getX(), y)), glade, priceTable)) break;
                    }

                }
                case 3 -> {
                    for (int x = position.getX() - 1; x > 0; x--) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(x, position.getY())), glade, priceTable)) break;
                    }
                }
            }
        }

        for (Position position : positions) {
            for (int r1 = 0; r1 < 4; r1++) {
                for (int r2 = 0; r2 < 4; r2++) {
                    if (r1 == r2) continue;
                    Tile tile = glade.getTile(position);

                    CodeRotation codeConnection = null;
                    if (tile.getMapType() == Tile.MapType.TURN) {
                        codeConnection = CodeRotation.getBestRotationWithTurn(r1, r2, tile.getNumber(), priceTable);
                        if (codeConnection == null) continue;
                    }

                    if (codeConnection == null) codeConnection = CodeRotation.getBestRotation(r1, r2, priceTable);
                    if (codeConnection == null) continue;

                    graph.addEdge(new Griever(position, r1), new Griever(position, r2), codeConnection);
                    graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));
                }
            }
        }

        return graph;
    }

    private static boolean addEdge(Graph<Griever, CodeConnection> graph, Griever p1, Griever p2, Glade glade, PriceTable priceTable) {
        Tile t2 = glade.getTile(p2.getPosition());
        if (t2.getMapType() == Tile.MapType.TURN) {
            System.out.println(t2);
            if (t2.getNumber() == 0) {
                //TODO
            } else {
                CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

                graph.addEdge(p1, p2.withRotation((p2.getRotation() + t2.getNumber()) % 4), codeConnection);
                graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));
            }
            return true;
        } else if (graph.containsVertex(p2)) {
            CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

            graph.addEdge(p1, p2, codeConnection);
            graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));
            return false;
        } else return true;
    }

    private static List<Griever> getRoute(Graph<Griever, CodeConnection> graph, Glade glade) {
        return graph.vertexSet().stream()
                .map(BiHolder.hold(p -> glade.getTile(p.getPosition().getX(), p.getPosition().getY())))
                .filter(t -> t.getValue().getMapType() == Tile.MapType.START || t.getValue().getMapType() == Tile.MapType.G0AL)
                .flatMap(t -> t.getValue().getMapType() == Tile.MapType.G0AL || t.getValue().getNumber() == t.getKey().getRotation() ? Stream.of(t) : Stream.empty())
                .sorted(comparator)
                .map(BiHolder::getKey)
                .collect(Collectors.toList());
    }

    private final static Comparator<BiHolder<Griever, Tile>> comparator = (o1, o2) -> {
        if (o1.getValue().getMapType() == Tile.MapType.START) return -1;
        if (o2.getValue().getMapType() == Tile.MapType.START) return 1;
        return o1.getValue().getNumber() - o2.getValue().getNumber();
    };

    public static int current = 0;
    public static int max = 0;

    protected static PrintStream out() {
        CustomPrintStream printStream = (CustomPrintStream) System.out;
        return printStream.getOut();
    }

    public static String generate(Glade glade, PriceTable priceTable) {
        Graph<Griever, CodeConnection> graph = createGraph(glade, priceTable);
        List<Griever> route = getRoute(graph, glade);

        Griever start = route.remove(0);
        current = 0;
        max = 0;
        for (int i = 1; i <= route.size() / 4; i++) {
            max += Math.pow(4, i);
        }

        StringBuilder completeCode = new StringBuilder();
        for (CodeConnection codeConnection : recursion(graph, start, route, priceTable)) {
            completeCode.append(codeConnection.generateCode()).append("\n");
        }

        if (CodeLine.LoopLine.getOffset() != 0) return "gebruik a\n\n" +  completeCode.toString();
        return completeCode.toString();
    }

    private static List<CodeConnection> recursion(Graph<Griever, CodeConnection> graph, Griever start, List<Griever> path, PriceTable priceTable) {
        printProgress();
        path = new ArrayList<>(path);

        if (path.isEmpty()) return new ArrayList<>();
        if (path.size() == 4) {
            SingleSourcePaths<Griever, CodeConnection> paths = new DijkstraShortestPath<>(graph).getPaths(start);

            int min = -1;
            List<CodeConnection> best = null;
            for (Griever griever : path) {
                printProgress();
                GraphPath<Griever, CodeConnection> graphPath = paths.getPath(griever);
                if (graphPath == null) continue;
                List<CodeConnection> route = graphPath.getEdgeList();

                int price = route.stream().mapToInt(c -> c.getPrice(priceTable)).sum();

                if (min == -1 || price < min) {
                    min = price;
                    best = route;
                }
            }

            return best;
        }

        SingleSourcePaths<Griever, CodeConnection> paths = new DijkstraShortestPath<>(graph).getPaths(start);
        List<Griever> current = new ArrayList<>();
        for (int i = 0; i < 4; i++) current.add(path.remove(0));

        int min = -1;
        List<CodeConnection> best = null;
        for (Griever griever : current) {
            GraphPath<Griever, CodeConnection> graphPath = paths.getPath(griever);
            if (graphPath == null) continue;

            List<CodeConnection> route = graphPath.getEdgeList();
            route.addAll(recursion(graph, griever, path, priceTable));

            int price = route.stream().mapToInt(c -> c.getPrice(priceTable)).sum();

            if (min == -1 || price < min) {
                min = price;
                best = route;
            }
        }
        return best;
    }

    private static void printProgress() {
        out().printf("\rGenerating route %.1f%% (%s/%s)", current * 1.0 / max * 100.0, current++, max);
    }
}
