package Interpreters.Primitives;

import Interpreters.Interpreter;
import Interpreters.Primitive;

public class Range extends Primitive {

    private final Primitive start;
    private final Primitive end;

    public Range(Interpreter s, Interpreter e) {
        start = s.solve();
        end = e.solve();
    }

    /* Base Methods */

    public Primitive solve() {
        if (start.id().equals("err"))
            return start;
        if (end.id().equals("err"))
            return end;

        return this;
    }

    public String id() {
        return "range";
    }

    public String string() {
        return start.string() + ":" + end.string();
    }
}
