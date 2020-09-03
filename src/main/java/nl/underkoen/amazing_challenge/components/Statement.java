package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;
import nl.underkoen.amazing_challenge.controllers.PriceTable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Under_Koen
 */
public class Statement implements Runnable {
    private final Consumer<Context> function;
    public final Function<PriceTable, Integer> getPrice;

    public Statement(Consumer<Context> function) {
        this(function, p -> 0);
    }

    public Statement(Consumer<Context> function, Function<PriceTable, Integer> getPrice) {
        this.function = function;
        this.getPrice = getPrice;
    }

    public void run(Context context) {
        function.accept(context);
    }

    public static Statement assign(String var, Expression value) {
        return new Statement(c -> c.variableStorage.store(var, value.calculate(c)), PriceTable::getAssignmentLine);
    }

    public static Statement whileStat(Comparison comparison, ProgramBlock program) {
        return new Statement(c -> {
            while (comparison.calculate(c)) {
                program.run(c);
            }
        }, priceTable -> priceTable.getWhileLine() + program.getPrice(priceTable));
    }

    //TODO ask boose
    public static Statement whileStat(Comparison comparison, ProgramBlock program, ProgramBlock elseProgram) {
        return new Statement(c -> {
            if (!comparison.calculate(c)) {
                elseProgram.run(c);
                return;
            }
            while (comparison.calculate(c)) {
                program.run(c);
            }
        }, priceTable -> priceTable.getWhileLine() + program.getPrice(priceTable) + elseProgram.getPrice(priceTable));
    }

    public static Statement ifElse(If i, ProgramBlock elseProgram) {
        return new Statement(c -> {
            if (i.comparison.calculate(c)) {
                i.program.run(c);
            } else {
                if (elseProgram != null) elseProgram.run(c);
            }
        }, priceTable -> priceTable.getIfLine() + i.program.getPrice(priceTable) + (elseProgram == null ? 0 : elseProgram.getPrice(priceTable)));
    }

    public static Statement forward() {
        return new Statement(c -> c.mapController.forward(), PriceTable::getFunctionLine);
    }

    public static Statement backward() {
        return new Statement(c -> c.mapController.back(), PriceTable::getFunctionLine);
    }

    public static Statement left() {
        return new Statement(c -> c.mapController.left(), PriceTable::getFunctionLine);
    }

    public static Statement right() {
        return new Statement(c -> c.mapController.right(), PriceTable::getFunctionLine);
    }

    public static class If {
        public final Comparison comparison;
        public final ProgramBlock program;

        public If(Comparison comparison, ProgramBlock program) {
            this.comparison = comparison;
            this.program = program;
        }
    }
}
