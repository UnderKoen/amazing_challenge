package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;

import java.util.List;

/**
 * @author Under_Koen
 */
public class InitBlock implements Runnable {
    private final List<InitStatement> initStatementList;

    public InitBlock(List<InitStatement> initStatementList) {
        this.initStatementList = initStatementList;
    }

    @Override
    public void run(Context context) {
        initStatementList.forEach(r -> r.run(context));
    }
}
