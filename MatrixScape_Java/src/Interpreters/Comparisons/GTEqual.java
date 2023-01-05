package Interpreters.Comparisons;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.Bool;
import Interpreters.Primitives.Err;
import Interpreters.Primitives.Num;
import Interpreters.Primitives.Range;

public class GTEqual {

    private final Interpreter i1;
    private final Interpreter i2;

    public GTEqual(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        if (p1.id().equals("err"))
            return p1;
        if (p2.id().equals("err"))
            return p2;

        Integer comparison = null;
        if (p1.id().equals("num"))
            comparison = ((Num) p1).compareTo(p2);
        else if (p1.id().equals("range"))
            comparison = ((Range) p1).compareTo(p2);

        if (comparison == null)
            return new Err("invalid comparison");

        return new Bool(comparison >= 0);
    }

    public String id() {
        return "gtequal";
    }
}
