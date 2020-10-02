package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;

import java.util.List;

/**
 * @author Under_Koen
 */
public class Program implements Runnable {
    private final ProgramBlock programBlock;
    private final Used used;

    public Program(Used used, ProgramBlock programBlock) {
        this.programBlock = programBlock;
        this.used = used;
    }

    @Override
    public void run(Context context) {
        List<String> vars = this.used.getUsed();
        runWithOption(context, vars, 0);
    }

    private void runWithOption(Context context, List<String> options, int i) {
        if (options.size() <= i) {
            programBlock.run(context);
            System.out.println("met " + context.variableStorage);
            System.out.println();
        } else {
            String v = options.get(i);
            context.variableStorage.store(v, true);
            runWithOption(context, options, i + 1);
            context.variableStorage.store(v, false);
            runWithOption(context, options, i + 1);
        }
    }
}
