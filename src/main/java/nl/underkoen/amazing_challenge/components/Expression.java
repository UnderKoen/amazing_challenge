package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Under_Koen
 */
public class Expression {
    private final Function<Context, Integer> function;

    public Expression(Function<Context, Integer> function) {
        this.function = function;
    }

    public int calculate(Context context) {
        return function.apply(context);
    }

    public static Expression combine(Expression e1, Expression e2, BiFunction<Integer, Integer, Integer> function) {
        return new Expression(c -> {
            c.spendMoney(c.priceTable.getCalculate(), "berekenen");
            return function.apply(e1.calculate(c), e2.calculate(c));
        });
    }

    public static Expression add(Expression e1, Expression e2) {
        return combine(e1, e2, Integer::sum);
    }

    public static Expression min(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> i1 - i2);
    }

    public static Expression times(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> i1 * i2);
    }

    public static Expression divide(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> i1 / i2);
    }

    public static Expression modulo(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> i1 % i2);
    }

    public static Expression number(int i) {
        return new Expression(c -> i);
    }

    public static Expression variable(String var) {
        return new Expression(c -> c.variableStorage.read(var));
    }

    public static Expression eye() {
        return new Expression(c -> c.mapController.eye());
    }

    public static Expression colorEye() {
        return new Expression(c -> c.mapController.colorEye());
    }

    public static Expression compass() {
        return new Expression(c -> c.mapController.compass());
    }
}
