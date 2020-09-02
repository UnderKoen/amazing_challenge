package nl.underkoen.amazing_challenge.components;

import nl.underkoen.amazing_challenge.controllers.Context;
import nl.underkoen.amazing_challenge.controllers.PriceTable;

import java.util.List;

/**
 * @author Under_Koen
 */
public class ProgramBlock implements Runnable {
    private final List<Statement> statementList;

    public ProgramBlock(List<Statement> statementList) {
        this.statementList = statementList;
    }

    @Override
    public void run(Context context) {
        statementList.forEach(r -> r.run(context));
    }

    public int getPrice(PriceTable priceTable) {
        return statementList.stream()
                .map(s -> s.getPrice)
                .mapToInt(f -> f.apply(priceTable))
                .sum();
    }
}
