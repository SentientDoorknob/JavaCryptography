package org.crypography_tools;

import java.util.*;

public class UnitTable {
    private final TreeMap<boolean[][], Integer> table;

    public UnitTable() {
        Comparator<boolean[][]> comparator = UnitTable::CompareTableComponents;
        table = new TreeMap<>(comparator);
    }

    public void Put(boolean[][] comp) {
        table.putIfAbsent(comp, comp.length);
    }

    public boolean[][] GetFirstTable() {
        return table.firstKey();
    }

    public Integer GetFirstLength() {
        return table.firstEntry().getValue();
    }

    public Map.Entry<boolean[][], Integer> GetFirstEntry() {
        return table.firstEntry();
    }

    public boolean[][][] GetValues() {
        return table.keySet().toArray(new boolean[0][0][0]);
    }

    public Integer[] GetKeys() {
        return table.values().toArray(new Integer[0]);
    }

    public static int CompareTableComponents(boolean[][] a, boolean[][] b) {
        int eval_a = Tools.EvaluateTableComponent(a);
        int eval_b = Tools.EvaluateTableComponent(b);

        if (eval_a == eval_b) return 1 - Integer.compare(b.length, a.length);
        return 1 - Integer.compare(eval_b, eval_a);
    }
}
