package nl.underkoen.amazing_challenge.components;


import nl.underkoen.amazing_challenge.controllers.Context;

import java.util.function.Consumer;

/**
 * @author Under_Koen
 */
public class InitStatement implements Runnable {
    private final Consumer<Context> consumer;

    public InitStatement(Consumer<Context> consumer) {
        this.consumer = consumer;
    }

    public static InitStatement variable(String var) {
        return new InitStatement(context -> context.variableStorage.register(var));
    }

    public static InitStatement eye() {
        return new InitStatement(context -> context.hardwareContainer.enableEye());
    }

    public static InitStatement colorEye() {
        return new InitStatement(context -> context.hardwareContainer.enableColorEye());
    }

    public static InitStatement compass() {
        return new InitStatement(context -> context.hardwareContainer.enableCompass());
    }

    @Override
    public void run(Context context) {
        consumer.accept(context);
    }
}
