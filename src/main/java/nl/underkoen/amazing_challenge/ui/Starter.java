package nl.underkoen.amazing_challenge.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.underkoen.amazing_challenge.controllers.PriceTable;
import nl.underkoen.amazing_challenge.controllers.Runner;
import nl.underkoen.amazing_challenge.models.Glade;

/**
 * @author Under_Koen
 */
public class Starter extends Application {
    private EditorView editorView;
    private MapView mapView;
    private PriceView priceView;
    private Glade currentGlade;
    private Thread run;

    public static void launch() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        editorView = new EditorView(this::run) {
            @Override
            protected void stop(Object o) {
                if (run != null) {
                    run.interrupt();
                }
            }
        };
        mapView = new MapView(this::update);
        priceView = new PriceView();

        mapView.show();
        priceView.show();
        editorView.show();
    }

    private void run(String code) {
        if (run != null) run.interrupt();
        run = new Thread(() -> {
            System.out.println("\n\n\n\n");
            System.out.println("=== Running ===");
            try {
                Glade runGlade = new Glade(currentGlade.getMap());
                mapView.updateGlade(runGlade);

                Runner.compileAndRun(code, runGlade, priceView.getPrices());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        run.start();
    }

    private void update(String code) {
        currentGlade = new Glade(code);
        mapView.updateGlade(currentGlade);
    }
}
