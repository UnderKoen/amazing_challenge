package nl.underkoen.amazing_challenge.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import nl.edulogo.core.Size;
import nl.edulogo.display.fx.FXDisplay;
import nl.edulogo.editor.fx.FXEditor;
import nl.underkoen.amazing_challenge.models.Glade;
import nl.underkoen.amazing_challenge.ui.fx.FXGlade;

import java.util.function.Consumer;

/**
 * @author Under_Koen
 */
public class MapView {
    private static final OS os = new OS();
    private static final String GLADE = "O2;O2;O2;O2;O2;O2;O2;O2;O2;C5;C5;O2;O2;O2;O2;O2;O2;O2;O2;O2;O2;O2;C5;C5;C5;C5;C5;C5;C5;C5;C5;C5;C5;O1;O1;O1;O1;O1;O1;O2;O2;C5;C5;O1;C2;C4;C4;O1;O2;C3;C3;C3;C5;C5;C5;C5;C5;O1;O1;O2;O2;C5;O1;C2;C2;C2;C4;O2;O1;C3;C3;C3;C3;C5;O1;O1;C5;C5;O1;O2;O2;C5;C2;C2;C2;C2;C4;O2;C3;C3;C3;C3;C3;C5;C5;O1;O1;C5;O1;O2;O2;C5;O3;C2;C2;C4;C4;C3;C3;C3;C3;C3;C3;C3;C5;O1;O1;C5;O1;O2;O2;C5;O3;C2;C2;C3;C3;C3;C3;C3;C3;C3;C3;C3;C5;C5;O1;C5;O1;O2;O2;C5;O3;C2;C5;S1;C3;C3;C3;C3;C3;C3;C3;C3;C3;D1;C5;C5;C5;O2;O2;C5;C5;C5;C5;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C5;O2;C5;C5;C5;C3;C3;C3;C3;C3;C4;C3;C3;C3;C3;C3;C3;C3;C3;C5;C5;C5;C5;C5;C3;C3;C4;C4;C4;C4;C4;C3;C3;C3;C3;C3;C3;O1;C3;C4;C4;C5;O2;C5;C3;C3;C4;C4;C4;C2;C3;C3;C3;C3;C3;C3;C3;O1;C3;C5;C4;O2;O2;C5;C3;C3;C4;C4;C4;C2;C3;O1;C3;C3;C3;C3;C3;C3;C3;C5;C4;O2;O2;C5;C3;C3;C4;C4;C3;C3;C3;C3;C3;C3;C3;O1;C3;C3;C3;C5;C4;O2;O2;C5;C3;R1;R1;R1;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C5;C5;O2;O2;C5;C5;R1;R1;R1;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;O1;C5;O2;O2;O1;C5;R1;R1;R1;C3;C3;C3;C3;C3;C3;C3;C3;C3;C3;O1;O1;C5;O2;O2;O1;C5;C5;C5;E8;C5;C5;C5;C5;C5;C5;C5;C5;C5;C3;O1;C5;C5;O2;O2;O1;O1;O1;C5;C5;C5;C3;C3;C5;C5;C3;C3;C3;C5;C5;C5;C5;R1;O2;O2;O2;O2;O2;O2;O2;O2;O2;O2;C5;C5;O2;O2;O2;O2;O2;O2;O2;O2;O2";

    private final Consumer<String> update;

    private final FXGlade map;
    private final FXDisplay<FXGlade> mapDisplay;

    private final FXEditor editor;
    private final FXDisplay<FXEditor> editorDisplay;

    public MapView(Consumer<String> update) {
        this.update = update;

        map = new FXGlade();
        mapDisplay = new FXDisplay<>(new Size(700, 700), map, false);

        editor = new FXEditor();
        editor.setText(GLADE);

        editorDisplay = new FXDisplay<>(new Size(350, 500), editor);

        generateMenu();
    }

    public void show() {
//        mapDisplay.show();
//        mapDisplay.setTitle("Glade viewer");
//
//        editorDisplay.show();
//        editorDisplay.setTitle("Glade editor");

        run(null);
    }

    public void updateGlade(Glade glade) {
        map.showGlade(glade);
    }


    private void generateMenu() {
        KeyCombination.Modifier mod = os.getModifier();

        Menu runMenu = new Menu("Glade");

        MenuItem run = new MenuItem("Update");
        run.setAccelerator(new KeyCodeCombination(KeyCode.S, mod));
        run.setOnAction(this::run);

        runMenu.getItems().addAll(run);

        editor.getMenuBar().getMenus().addAll(runMenu);
    }

    private void run(Object o) {
        update.accept(editor.getText().replace("\n", ""));
    }
}
