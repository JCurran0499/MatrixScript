package app.parser.interpreters.comparisons;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Bool;
import app.parser.interpreters.primitives.Err;

public class Not implements Interpreter {

    private final Interpreter i;

    public Not(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (p.id().equals("err"))
            return p;

        if (p.id().equals("bool")) {
            return Bool.cast(p).not();
        }

        return new Err("can only reverse booleans");
    }

    public String id() {
        return "not";
    }
}
