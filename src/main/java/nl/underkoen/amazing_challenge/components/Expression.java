package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Under_Koen
 */
public class Expression extends Statement {
    private final Function<Context, Boolean> function;

    public Expression(Function<Context, Boolean> function) {
        super(context -> {
            System.out.println(function.apply(context));
        });
        this.function = function;
    }

    public boolean calculate(Context context) {
        return function.apply(context);
    }

    public static Expression combine(Expression e1, Expression e2, BiFunction<Boolean, Boolean, Boolean> function) {
        return new Expression(c -> function.apply(e1.calculate(c), e2.calculate(c)));
    }

    public static Expression and(Expression e1, Expression e2) {
        return combine(e1, e2, (b1, b2) -> b1 && b2);
    }

    public static Expression or(Expression e1, Expression e2) {
        return combine(e1, e2, (b1, b2) -> b1 || b2);
    }

    public static Expression implication(Expression e1, Expression e2) {
        return combine(e1, e2, (b1, b2) -> b2 || !b1);
    }

    public static Expression xnor(Expression e1, Expression e2) {
        return combine(e1, e2, (b1, b2) -> b1 == b2);
    }

    public static Expression not(Expression e1) {
        return new Expression(c -> !e1.calculate(c));
    }

    public static Expression bool(boolean b) {
        return new Expression(c -> b);
    }

    public static Expression variable(String var) {
        return new Expression(c -> c.variableStorage.read(var));
    }
}

