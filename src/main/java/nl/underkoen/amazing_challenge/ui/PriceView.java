package nl.underkoen.amazing_challenge.ui;

import nl.edulogo.core.Size;
import nl.edulogo.display.fx.FXDisplay;
import nl.underkoen.amazing_challenge.controllers.PriceTable;
import nl.underkoen.amazing_challenge.ui.fx.FXPrice;

/**
 * @author Under_Koen
 */
public class PriceView {
    private FXPrice price;
    private final FXDisplay<FXPrice> display;

    public PriceView() {
        price = new FXPrice() {
            @Override
            public void reset() {
                resetView();
            }
        };
        display = new FXDisplay<>(new Size(600, 700), price);
    }

    public void resetView() {
        price = new FXPrice() {
            @Override
            public void reset() {
                resetView();
            }
        };
        display.setView(price);
    }

    public void show() {
        display.show();
        display.setTitle("Prices");
    }

    public PriceTable getPrices() {
        return price.getPriceTable();
    }
}
