package nl.underkoen.amazing_challenge.ui.fx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nl.edulogo.core.Size;
import nl.edulogo.display.fx.FXView;
import nl.underkoen.amazing_challenge.controllers.PriceTable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Under_Koen
 */
public abstract class FXPrice implements FXView {
    private final PriceTable priceTable = new PriceTable();
    private final StackPane node;

    public FXPrice() {
        node = new StackPane();

        HBox columns = new HBox();

        VBox hardware = new VBox();
        addField(hardware, "kompas",
                priceTable::setBuyCompass, priceTable::getBuyCompass);
        addField(hardware, "zwOog (alles is wit behalve zwart)",
                priceTable::setBuyEye, priceTable::getBuyEye);
        addField(hardware, "kleurOog",
                priceTable::setBuyColorEye, priceTable::getBuyColorEye);
        addField(hardware, "variabele (a .. z)",
                priceTable::setBuyVariable, priceTable::getBuyVariable);
        addField(hardware, "start geld",
                priceTable::setStartMoney, priceTable::getStartMoney);


        VBox usage = new VBox();
        addField(usage, "stapVooruit",
                priceTable::setForward, priceTable::getForward);
        addField(usage, "stapAchteruit",
                priceTable::setBack, priceTable::getBack);
        addField(usage, "draaiLinks",
                priceTable::setLeft, priceTable::getLeft);
        addField(usage, "draaiRechts",
                priceTable::setRight, priceTable::getRight);
        addField(usage, "zwOog",
                priceTable::setUseEye, priceTable::getUseEye);
        addField(usage, "kleurOog",
                priceTable::setUseColorEye, priceTable::getUseColorEye);
        addField(usage, "kompas",
                priceTable::setUseCompass, priceTable::getUseCompass);
        addField(usage, "duw obstakel (schade)",
                priceTable::setPushObstacle, priceTable::getPushObstacle);
        addField(usage, "toewijzing (a = 1)",
                priceTable::setAssignment, priceTable::getAssignment);
        addField(usage, "operatie (+, 0, *, %, /)",
                priceTable::setCalculate, priceTable::getCalculate);
        addField(usage, "vergelijking (==, <, >, !=)",
                priceTable::setCompare, priceTable::getCompare);


        VBox software = new VBox();
        addField(software, "zolang (lus)",
                priceTable::setWhileLine, priceTable::getWhileLine);
        addField(software, "als (keuze)",
                priceTable::setIfLine, priceTable::getIfLine);
        addField(software, "opdracht",
                priceTable::setFunctionLine, priceTable::getFunctionLine);
        addField(software, "toekenning",
                priceTable::setAssignmentLine, priceTable::getAssignmentLine);
        addField(software, "delay",
                priceTable::setDelay, priceTable::getDelay);


        Button reset = new Button("Reset");
        reset.setOnAction(event -> reset());
        software.getChildren().add(reset);


        columns.getChildren().addAll(hardware, new Label("      "), usage, new Label("      "), software);
        columns.setAlignment(Pos.CENTER);

        node.getChildren().add(columns);
    }

    private void addField(VBox column, String name, Consumer<Integer> setValue, Supplier<Integer> getValue) {
        Label label = new Label(name + ":");
        TextField textField = new TextField(getValue.get().toString());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String v = newValue.replaceAll("[^0-9]", "");
            if (newValue.equals(v)) {
                if (v.isBlank()) return;
                setValue.accept(Integer.parseInt(newValue));
            } else {
                textField.setText(v);
            }
        });

        column.getChildren().addAll(new Label(), label, textField);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public Size getSize() {
        return new Size(700, 800);
    }

    public PriceTable getPriceTable() {
        return priceTable;
    }

    public abstract void reset();
}
