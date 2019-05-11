package lpij;

import java.util.Map;

public class VariableMapping {
    private final Map<Integer, Integer> map;
    final int length;

    public VariableMapping(Map<Integer, Integer> map, int length) {
        this.map = map;
        this.length = length;
    }

    Integer get(int i) {
        return map.get(i);
    }
}
