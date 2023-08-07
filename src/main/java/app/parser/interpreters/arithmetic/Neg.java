package app.parser.interpreters.arithmetic;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.*;

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
            return Num.cast(p).negate();

        if (p.id().equals("mat"))
            return Mat.cast(p).negate();

        if (p.id().equals("range"))
            return Range.cast(p).negate();

        if (p.id().equals("tuple"))
            return Tuple.cast(p).negate();

        return new Err("cannot negate this command");
    }

    public String id() {
        return "neg";
    }
}
