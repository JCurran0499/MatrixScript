package Interpreters.Arithmetic;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.*;

public class Neg implements Interpreter {

    private final Interpreter i;

    public Neg(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (p.id().equals("err"))
            return p;

        if (p.id().equals("num"))
            return ((Num) p).negate();

        if (p.id().equals("mat"))
            return ((Mat) p).negate();

        if (p.id().equals("range"))
            return ((Range) p).negate();

        if (p.id().equals("tuple"))
            return ((Tuple) p).negate();

        return new Err("cannot negate this command");
    }

    public String id() {
        return "neg";
    }
}
