package nl.underkoen.amazing_challenge.components;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Under_Koen
 */
public class Used {
    private List<String> used = new ArrayList<>();

    public Used(String s) {
        used.add(s);
    }

    public Used add(String s) {
        used.add(s);
        return this;
    }

    public List<String> getUsed() {
        return used;
    }
}
