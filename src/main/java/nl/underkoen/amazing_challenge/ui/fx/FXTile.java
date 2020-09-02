package nl.underkoen.amazing_challenge.ui.fx;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nl.underkoen.amazing_challenge.models.Tile;

/**
 * @author Under_Koen
 */
public class FXTile extends StackPane {
    public FXTile(Tile tile) {
        ImageView image = new ImageView(tile.getImage());
        Text number = new Text(String.valueOf(tile.getNumber()));
        number.setStroke(Color.WHITE);

        TextFlow flow = new TextFlow(number);

        getChildren().addAll(image, flow);
        StackPane.setAlignment(flow, Pos.TOP_LEFT);

        Bounds bounds = number.getBoundsInParent();
        flow.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.5), null,
                new Insets(bounds.getMinY(), 32 - bounds.getWidth(), bounds.getMaxY(), bounds.getMinX()))));
    }
}
