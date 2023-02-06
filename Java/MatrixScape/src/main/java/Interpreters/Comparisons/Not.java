package Interpreters.Comparisons;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.*;

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
            return ((Bool) p).not();
        }

        return new Err("can only reverse booleans");
    }

    public String id() {
        return "not";
    }
}
