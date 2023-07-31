package App.Parser.Interpreters.Primitives;

import App.Parser.Interpreters.Primitive;

import java.math.BigDecimal;
import java.lang.Math;

public class Range extends Primitive {

    private final int start;
    private final int end;

    public Range(int s, int e) {
        start = s;
        end = e;
    }

    /* Base Methods */

    public String id() {
        return "range";
    }

    public String string() {
        return start + ":" + end;
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        Range r = (Range) p;
        return start == (r.start) && end == (r.end);
    }

    /* Logic Methods */

    public Range negate() {
        return new Range(end, start);
    }

    public int range() {
        return Math.abs(end - start);
    }

    public Integer compareTo(Primitive a) {
        if (a.id().equals("num"))
            return BigDecimal.valueOf(range()).compareTo(((Num) a).num());

        if (a.id().equals("range"))
            return BigDecimal.valueOf(range()).compareTo(BigDecimal.valueOf(((Range) a).range()));

        return null;
    }

    public int[] fullRange() {
        int[] range = new int[range()];
        if (start <= end) {
            for (int i = start; i < end; i++)
                range[i - start] = i;
        }
        else {
            for (int i = start; i > end; i--)
                range[start - i] = i;
        }

        return range;
    }
}
