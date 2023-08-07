package app.parser.interpreters.comparisons;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Bool;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Num;
import app.parser.interpreters.primitives.Range;

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

        if (p1.id().equals("err"))
            return p1;
        if (p2.id().equals("err"))
            return p2;

        Integer comparison = null;
        if (p1.id().equals("num"))
            comparison = ((Num) p1).compareTo(p2);
        else if (p1.id().equals("range"))
            comparison = ((Range) p1).compareTo(p2);

        if (comparison == null)
            return new Err("invalid comparison");

        return Bool.of(comparison < 0);
    }

    public String id() {
        return "less";
    }
}
