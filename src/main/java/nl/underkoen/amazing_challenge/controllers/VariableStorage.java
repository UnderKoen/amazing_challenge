package nl.underkoen.amazing_challenge.controllers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Under_Koen
 */
public class VariableStorage {
    private final Context context;
    private final Map<String, Integer> variables = new HashMap<>();

    public VariableStorage(Context context) {
        this.context = context;
    }

    public int read(String var) {
        if (!variables.containsKey(var)) throw new RuntimeException(String.format("Variable %s is not bought", var));
        return variables.get(var);
    }

    public void store(String var, int val) {
        context.spendMoney(context.priceTable.getAssignment());
        if (!variables.containsKey(var)) throw new RuntimeException(String.format("Variable %s is not bought", var));
        variables.put(var, val);
    }

    public void register(String val) {
        context.spendMoney(context.priceTable.getBuyVariable());
        variables.put(val, 0);
    }

    @Override
    public String toString() {
        return variables.toString();
    }
}
