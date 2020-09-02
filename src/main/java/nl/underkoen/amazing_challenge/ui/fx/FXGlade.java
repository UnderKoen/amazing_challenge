package nl.underkoen.amazing_challenge.ui.fx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nl.edulogo.core.Size;
import nl.edulogo.display.fx.FXView;
import nl.underkoen.amazing_challenge.models.Glade;
import nl.underkoen.amazing_challenge.models.Tile;

/**
 * @author Under_Koen
 */
public class FXGlade implements FXView {
    private VBox rows;
    private HBox[] columns;

    public FXGlade() {
        rows = new VBox();
        rows.setAlignment(Pos.CENTER);
        columns = new HBox[20];
        for (int i = 0; i < 20; i++) {
            HBox column = new HBox();
            column.setAlignment(Pos.CENTER);
            columns[i] = column;
            rows.getChildren().add(column);
        }
    }

    public void showGlade(Glade glade) {
        glade.setUpdateListener(this::showGlade);
        Platform.runLater(() -> {
            Tile[][] map = glade.getMap();
            for (int i = 0; i < 20; i++) {
                columns[i].getChildren().clear();
                for (int j = 0; j < 20; j++) {
                    columns[i].getChildren().add(new FXTile(map[i][j]));
                }
            }
        });
    }

    @Override
    public Node getNode() {
        return rows;
    }

    @Override
    public Size getSize() {
        return new Size(700, 700);
    }
}
