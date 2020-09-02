package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Under_Koen
 */
public class Comparison {
    private final Function<Context, Boolean> function;

    public Comparison(Function<Context, Boolean> function) {
        this.function = function;
    }

    public boolean calculate(Context context) {
        context.spendMoney(context.priceTable.getCompare());
        return function.apply(context);
    }

    public static Comparison combine(Expression e1, Expression e2, BiFunction<Integer, Integer, Boolean> function) {
        return new Comparison(c -> function.apply(e1.calculate(c), e2.calculate(c)));
    }

    public static Comparison equal(Expression e1, Expression e2) {
        return combine(e1, e2, Integer::equals);
    }

    public static Comparison notEqual(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> !i1.equals(i2));
    }

    public static Comparison bigger(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> i1 < i2);
    }

    public static Comparison smaller(Expression e1, Expression e2) {
        return combine(e1, e2, (i1, i2) -> i1 > i2);
    }
}