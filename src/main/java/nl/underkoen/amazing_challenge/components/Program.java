package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;

/**
 * @author Under_Koen
 */
public class Program implements Runnable {
    private final InitBlock initBlock;
    private final ProgramBlock programBlock;

    public Program(InitBlock initBlock, ProgramBlock programBlock) {
        this.initBlock = initBlock;
        this.programBlock = programBlock;
    }

    @Override
    public void run(Context context) {
        context.spendMoney(programBlock.getPrice(context.priceTable));
        initBlock.run(context);
        programBlock.run(context);
    }
}
