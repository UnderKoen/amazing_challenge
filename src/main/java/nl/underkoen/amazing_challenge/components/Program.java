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
        int price = programBlock.getPrice(context.priceTable);
        context.spendMoney(price);
        System.out.println("Kosten van de code " + price);
        initBlock.run(context);
        programBlock.run(context);
    }
}
