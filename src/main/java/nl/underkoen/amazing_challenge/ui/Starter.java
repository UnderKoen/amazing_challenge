package nl.underkoen.amazing_challenge.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.underkoen.amazing_challenge.controllers.Runner;
import nl.underkoen.amazing_challenge.models.Glade;

/**
 * @author Under_Koen
 */
public class Starter extends Application {
    private EditorView editorView;
    private MapView mapView;
    private Glade currentGlade;

    public static void launch() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        editorView = new EditorView(this::run);
        mapView = new MapView(this::update);

        mapView.show();
        editorView.show();
    }

    private void run(String code) {
        new Thread(() -> {
            System.out.println("=== Running ===");
            try {
                Glade runGlade = new Glade(currentGlade.getMap());
                mapView.updateGlade(runGlade);

                Runner.compileAndRun(code, runGlade);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void update(String code) {
        currentGlade = new Glade(code);
        mapView.updateGlade(currentGlade);
    }
}
