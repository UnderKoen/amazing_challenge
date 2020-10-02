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

    public Statement(Consumer<Context> function) {
        this.function = function;
    }

    public void run(Context context) {
        function.accept(context);
    }

    public static Statement assign(String var, Expression value) {
        return new Statement(c -> c.variableStorage.store(var, value.calculate(c)));
    }
}
