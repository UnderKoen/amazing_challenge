package nl.underkoen.amazing_challenge.controllers;

import nl.underkoen.amazing_challenge.models.Glade;

/**
 * @author Under_Koen
 */
public class Context {
    public final int startMoney;
    public final VariableStorage variableStorage = new VariableStorage(this);
    public final HardwareContainer hardwareContainer = new HardwareContainer(this);
    public final MapController mapController;
    public final PriceTable priceTable;
    public final Glade glade;

    private int moneySpend;
    private int moneyEarned;

    public Context(PriceTable priceTable, int startMoney, Glade glade) {
        this.priceTable = priceTable;
        this.startMoney = startMoney;
        this.glade = glade;
        mapController = new MapController(this);
    }

    public void earnMoney(int amount) {
        moneyEarned += amount;
        printMoneyInfo("earned", amount);
    }

    public void spendMoney(int amount) {
        moneySpend += amount;
        printMoneyInfo("spend", amount);
    }

    public void printMoneyInfo(String what, int amount) {
        System.out.printf("just %s %s, total spending: %s, total earning: %s, money left: %s%n", what, amount, moneySpend, moneyEarned, getRemainingMoney());
    }

    public int getMoneySpend() {
        return moneySpend;
    }

    public int getMoneyEarned() {
        return moneyEarned;
    }

    public int getRemainingMoney() {
        return startMoney + moneyEarned - moneySpend;
    }
}
