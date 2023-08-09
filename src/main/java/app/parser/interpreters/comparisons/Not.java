package app.parser.interpreters.comparisons;

import app.parser.Interpreter;
import app.parser.Primitive;
import app.parser.primitives.Bool;
import app.parser.primitives.Err;

public class Not implements Interpreter {

    private final Interpreter i;

    public Not(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (Err.is(p))
            return p;

        if (Bool.is(p)) {
            return Bool.cast(p).not();
        }

        return new Err("can only reverse booleans");
    }
}
