package Interpreters.Commands;

import Interpreters.*;
import Interpreters.Primitives.*;

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

        }

        if (p2.id().equals("tuple"))
            return ((Tuple) p2).get(p1);

        return new Err("invalid 'get' command");
    }

    public String id() { return "get"; }
}
