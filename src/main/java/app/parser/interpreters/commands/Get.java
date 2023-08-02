package app.parser.interpreters.commands;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import app.parser.interpreters.primitives.Tuple;

public class Get implements Interpreter {

    private final Interpreter i1;
    private final Interpreter i2;

    public Get(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        // --------- Errors --------- \\

        if (p1.id().equals("err"))
            return p1;
        if (p2.id().equals("err"))
            return p2;

        // --------- Computation --------- \\
        if (p1.id().equals("tuple") && p2.id().equals("mat")) {
            return ((Mat) p2).get((Tuple) p1);
        }

        if (p2.id().equals("tuple"))
            return ((Tuple) p2).get(p1);

        return new Err("invalid 'get' command");
    }

    public String id() { return "get"; }
}
