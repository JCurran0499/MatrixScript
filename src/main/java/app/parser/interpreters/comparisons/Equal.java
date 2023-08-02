package app.parser.interpreters.comparisons;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Bool;

public class Equal implements Interpreter {

    private final Interpreter i1;
    private final Interpreter i2;

    public Equal(Interpreter i1, Interpreter i2) {
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

        return new Bool(p1.equals(p2));
    }

    public String id() {
        return "equal";
    }
}
