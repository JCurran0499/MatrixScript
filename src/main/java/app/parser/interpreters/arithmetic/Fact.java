package app.parser.interpreters.arithmetic;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Num;

public class Fact implements Interpreter {
    private final Interpreter i;

    public Fact(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (p.id().equals("err"))
            return p;

        if (p.id().equals("num"))
            return ((Num) p).factorial();

        return new Err("invalid factorial");
    }

    public String id() {
        return "fact";
    }

}
