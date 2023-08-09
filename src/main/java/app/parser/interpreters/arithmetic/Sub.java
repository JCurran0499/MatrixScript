package app.parser.interpreters.arithmetic;

import app.parser.Interpreter;
import app.parser.Primitive;
import app.parser.primitives.Err;
import app.parser.primitives.Mat;
import app.parser.primitives.Null;
import app.parser.primitives.Num;

public class Sub implements Interpreter {
    private final Interpreter i1;
    private final Interpreter i2;

    public Sub(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        // --------- Errors --------- \\

        if (Err.is(p1))
            return p1;
        if (Err.is(p2))
            return p2;

        if (Null.is(p2))
            return new Err("imbalanced subtraction");

        // --------- Computation --------- \\

        if (Num.is(p1)) {
            return Num.cast(p1).subtract(p2).solve();
        }

        if (Mat.is(p1)) {
            return Mat.cast(p1).subtract(p2).solve();
        }

        return new Err("invalid subtraction");
    }
}
