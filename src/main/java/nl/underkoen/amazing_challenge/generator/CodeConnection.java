package nl.underkoen.amazing_challenge.generator;

import nl.underkoen.amazing_challenge.controllers.PriceTable;

/**
 * @author Under_Koen
 */
public interface CodeConnection {
    String generateCode();
    int getPrice(PriceTable priceTable);
}
