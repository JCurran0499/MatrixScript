package app.parser.interpreters.comparisons;

import app.parser.Interpreter;
import app.parser.Primitive;
import app.parser.primitives.Bool;
import app.parser.primitives.Err;
import app.parser.primitives.Num;
import app.parser.primitives.Range;

public class Less implements Interpreter {

    private final Interpreter i1;
    private final Interpreter i2;

    public Less(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        if (Err.is(p1))
            return p1;
        if (Err.is(p2))
            return p2;

        Integer comparison = null;
        if (Num.is(p1))
            comparison = ((Num) p1).compareTo(p2);
        else if (Range.is(p1))
            comparison = ((Range) p1).compareTo(p2);

        if (comparison == null)
            return new Err("invalid comparison");

        return Bool.of(comparison < 0);
    }
}
