package Interpreters.Arithmetic;

import Interpreters.*;
import Interpreters.Primitives.*;

public class Add implements Interpreter {
    private final Interpreter i1;
    private final Interpreter i2;

    public Add(Interpreter i1, Interpreter i2) {
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

        if (p1.id().equals("num")) {
            return ((Num) p1).add(p2).solve();
        }

        if (p1.id().equals("mat")) {
            return ((Mat) p1).add(p2).solve();
        }

        return new Err("invalid addition");
    }

    public String id() {
        return "add";
    }
}
