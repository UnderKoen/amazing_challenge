package nl.underkoen.amazing_challenge.controllers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Under_Koen
 */
public class VariableStorage {
    private final Context context;
    private final Map<String, Boolean> variables = new HashMap<>();

    public VariableStorage(Context context) {
        this.context = context;
    }

    public boolean read(String var) {
        return variables.getOrDefault(var, false);
    }

    public void store(String var, boolean val) {
        variables.put(var, val);
    }

    @Override
    public String toString() {
        return variables.toString();
    }
}
