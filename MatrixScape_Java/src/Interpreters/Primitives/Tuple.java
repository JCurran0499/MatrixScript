package Interpreters.Primitives;

import Interpreters.Interpreter;
import Interpreters.Primitive;

public class Tuple extends Primitive {
    private final Primitive v1;
    private final Primitive v2;

    public Tuple(Interpreter v1, Interpreter v2) {
        this.v1 = v1.solve();
        this.v2 = v2.solve();
    }

    /* Base Methods */

    public Primitive solve() {
        if (v1.id().equals("err"))
            return v1;
        if (v2.id().equals("err"))
            return v2;

        return this;
    }

    public String id() {
        return "tuple";
    }

    public String string() {
        return v1.string() + ", " + v2.string();
    }

    /* Logic Methods */

    public Tuple negate() {
        Interpreter newV1 = v1;
        Interpreter newV2 = v2;

        if (newV1.id().equals("tuple"))
            newV1 = ((Tuple) newV1).negate();
        if (newV2.id().equals("tuple"))
            newV2 = ((Tuple) newV2).negate();

        return new Tuple(newV2, newV1);
    }
}
