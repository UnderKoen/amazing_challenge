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
import java.util.Random;
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
                if (tile.getMapType() == Tile.MapType.BOMB && tile.getNumber() == 0) continue;
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
                        if (addEdge(graph, griever, griever.withPosition(new Position(position.getX(), y)), glade, priceTable))
                            break;
                    }
                }
                case 1 -> {
                    for (int x = position.getX() + 1; x < 20; x++) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(x, position.getY())), glade, priceTable))
                            break;
                    }
                }
                case 0 -> {
                    for (int y = position.getY() - 1; y > 0; y--) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(position.getX(), y)), glade, priceTable))
                            break;
                    }

                }
                case 3 -> {
                    for (int x = position.getX() - 1; x > 0; x--) {
                        if (addEdge(graph, griever, griever.withPosition(new Position(x, position.getY())), glade, priceTable))
                            break;
                    }
                }
            }
        }

        for (Position position : positions) {
            Tile tile = glade.getTile(position);
            if (tile.getMapType() == Tile.MapType.BOMB && tile.getNumber() == 1) continue;
            for (int r1 = 0; r1 < 4; r1++) {
                int r2 = (r1 + 1) % 4;
                addRotateEdge(graph, new Griever(position, r1), new Griever(position, r2), glade, priceTable);

                r2 = (r1 + 3) % 4;
                addRotateEdge(graph, new Griever(position, r1), new Griever(position, r2), glade, priceTable);
            }
        }

        return graph;
    }

    private static void addRotateEdge(Graph<Griever, CodeConnection> graph, Griever p1, Griever p2, Glade glade, PriceTable priceTable) {
        int r1 = p1.getRotation();
        int r2 = p2.getRotation();
        if (r1 == r2) return;
        Tile tile = glade.getTile(p1.getPosition());

        if (tile.getMapType() == Tile.MapType.TURN && tile.getNumber() == 0) {
            CodeRotation codeConnection = CodeRotation.getBestRotationWithTurn(r1, r2, tile.getNumber(), priceTable);
            if (codeConnection == null) return;
            graph.addEdge(p1, p2, codeConnection);
            graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));
            return;
        }

        if (tile.getMapType() == Tile.MapType.TURN) {
            int extraRot = tile.getNumber();
            r2 += extraRot;
            r2 %= 4;
            if (r1 == r2) return;
        }

        CodeConnection codeConnection = CodeRotation.getBestRotation(r1, r2, priceTable);
        if (codeConnection == null) return;

        graph.addEdge(p1.withRotation(r1), p2.withRotation(r2), codeConnection);
        graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));
    }

    private static Random rnd = new Random();

    private static boolean addEdge(Graph<Griever, CodeConnection> graph, Griever p1, Griever p2, Glade glade, PriceTable priceTable) {
        Tile t2 = glade.getTile(p2.getPosition());
        if (t2.getMapType() == Tile.MapType.TURN) {
            if (t2.getNumber() == 0) {
                int r = rnd.nextInt();
                p2 = p2.withRotation(r);
                graph.addVertex(p2);
                CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

                graph.addEdge(p1, p2, codeConnection);
                graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));

                for (int i = 0; i < 4; i++) {
                    CodeConnection rot = CodeRotation.getBestRotationWithTurn(r, i, 0, priceTable);
                    graph.addEdge(p2, p2.withRotation(i), rot);
                    graph.setEdgeWeight(rot, rot.getPrice(priceTable));
                }
            } else {
                CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

                graph.addEdge(p1, p2.withRotation((p2.getRotation() + t2.getNumber()) % 4), codeConnection);
                graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));

                addReverse(graph, p1, p2, glade, priceTable);
            }
            return true;
        } else if (graph.containsVertex(p2)) {
            CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

            graph.addEdge(p1, p2, codeConnection);
            graph.setEdgeWeight(codeConnection, codeConnection.getPrice(priceTable));

            addReverse(graph, p1, p2, glade, priceTable);
            return false;
        } else return true;
    }

    private static void addReverse(Graph<Griever, CodeConnection> graph, Griever p1, Griever p2, Glade glade, PriceTable priceTable) {
        Tile t1 = glade.getTile(p1.getPosition());
        if (t1.getMapType() == Tile.MapType.TURN) {
            if (t1.getNumber() == 0) {

            } else {
                CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

                CodeLine reverse = new CodeLine.ReverseLine(codeConnection);
                graph.addEdge(p2, p1.withRotation((p2.getRotation() + t1.getNumber()) % 4), reverse);
                graph.setEdgeWeight(reverse, reverse.getPrice(priceTable));
            }
        } else if (graph.containsVertex(p2)) {
            CodeLine codeConnection = CodeLine.getBestLine(p1.getPosition(), p2.getPosition(), glade, priceTable);

            CodeLine reverse = new CodeLine.ReverseLine(codeConnection);
            graph.addEdge(p2, p1, reverse);
            graph.setEdgeWeight(reverse, reverse.getPrice(priceTable));
        }
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

    public static String generate(Glade glade, PriceTable priceTable) {
        Graph<Griever, CodeConnection> graph = createGraph(glade, priceTable);
        List<Griever> route = getRoute(graph, glade);

        Graph<Griever, List> routes = new DirectedWeightedMultigraph<>(List.class);
        route.forEach(routes::addVertex);

        Griever start = route.remove(0);

        addEdges(graph, routes, start, route.subList(0, 4), priceTable);

        for (int i = 0; i < route.size() - 4; i++) {
            Griever s = route.get(i);
            addEdges(graph, routes, s, route.subList(i / 4 * 4 + 4, i / 4* 4 + 8), priceTable);
        }

        SingleSourcePaths<Griever, List> paths = new DijkstraShortestPath<>(routes).getPaths(start);

        int min = -1;
        List<CodeConnection> best = null;
        for (int i = 1; i <= 4; i++) {
            Griever finish = route.get(route.size() - i);
            List<CodeConnection> r = new ArrayList<>();

            paths.getPath(finish).getEdgeList().forEach(r::addAll);

            int price = r.stream().mapToInt(c -> c.getPrice(priceTable)).sum();

            if (min == -1 || price < min) {
                min = price;
                best = r;
            }
        }

        System.out.println(best);


        String pretend = "";
        StringBuilder completeCode = new StringBuilder();
        for (CodeConnection codeConnection : best) {
            completeCode.append(codeConnection.generateCode()).append("\n");
        }

        if (CodeLine.LoopLine.getOffset() != 0) pretend += "gebruik a\n";
        if (CodeRotation.RotationWithTurn.random) pretend += "gebruik kompas\n";
        return pretend + completeCode.toString();
    }

    private static void addEdges(Graph<Griever, CodeConnection> graph, Graph<Griever, List> route, Griever start, List<Griever> path, PriceTable priceTable) {
        SingleSourcePaths<Griever, CodeConnection> paths = new DijkstraShortestPath<>(graph).getPaths(start);
        for (Griever griever : path) {
            GraphPath<Griever, CodeConnection> graphPath = paths.getPath(griever);
            List<CodeConnection> r = graphPath.getEdgeList();
            int price = r.stream().mapToInt(c -> c.getPrice(priceTable)).sum();
            route.addEdge(start, griever, r);
            route.setEdgeWeight(r, price);
        }
    }
}
