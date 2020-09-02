package nl.underkoen.amazing_challenge.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import nl.edulogo.core.Size;
import nl.edulogo.display.fx.FXDisplay;
import nl.edulogo.editor.fx.FXEditor;

import java.util.function.Consumer;

/**
 * @author Under_Koen
 */
public abstract class EditorView {
    private static final OS os = new OS();

    private final Consumer<String> run;
    private final FXEditor editor;
    private final FXDisplay<FXEditor> display;

    public EditorView(Consumer<String> run) {
        this.run = run;

        editor = new FXEditor();
        display = new FXDisplay<>(new Size(350, 500), editor);
        generateMenu();

        System.setOut(new CustomPrintStream(System.out, s -> {
            if (s.isEmpty()) return;
            Platform.runLater(() -> editor.getConsole().println(s));
        }));
        System.setErr(new CustomPrintStream(System.err, s -> {
            if (s.isEmpty()) return;
            Platform.runLater(() -> editor.getConsole().error(s));
        }));
    }

    public void show() {
        display.show();
        display.setTitle("20, editor");
    }

    private void generateMenu() {
        KeyCombination.Modifier mod = os.getModifier();

        Menu runMenu = new Menu("Run");

        MenuItem run = new MenuItem("Run");
        run.setAccelerator(new KeyCodeCombination(KeyCode.R, mod));
        run.setOnAction(this::run);

        MenuItem stop = new MenuItem("Stop");
        stop.setAccelerator(new KeyCodeCombination(KeyCode.E, mod));
        stop.setOnAction(this::stop);

        runMenu.getItems().addAll(run, stop);

        editor.getMenuBar().getMenus().addAll(runMenu);
    }

    private void run(Object o) {
        run.accept(editor.getText());
    }

    protected abstract void stop(Object o);
}
