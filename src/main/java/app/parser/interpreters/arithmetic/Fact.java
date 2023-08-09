package app.parser.interpreters.arithmetic;

import app.parser.Interpreter;
import app.parser.Primitive;
import app.parser.primitives.Err;
import app.parser.primitives.Num;

public class Fact implements Interpreter {
    private final Interpreter i;

    public Fact(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (Err.is(p))
            return p;

        if (Num.is(p))
            return ((Num) p).factorial();

        return new Err("invalid factorial");
    }
}
