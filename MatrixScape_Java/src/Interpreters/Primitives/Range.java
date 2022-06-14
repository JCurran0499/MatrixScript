package Interpreters.Primitives;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Matrix.Matrix;

public class Range extends Primitive {

    private final Primitive start;
    private final Primitive end;

    public Range(Interpreter s, Interpreter e) {
        start = s.solve();
        end = e.solve();
    }

    /* Base Methods */

    public Primitive solve() {
        if (start.id().equals("err"))
            return start;
        if (end.id().equals("err"))
            return end;

        if (start.id().equals("mat") && end.id().equals("mat")) {
            Matrix startM = ((Mat) start).mat();
            Matrix endM = ((Mat) end).mat();

            return new Mat(startM.augment(endM)).solve();
        }

        if (start.id().equals("num") && end.id().equals("num"))
            if (((Num) start).isInteger() && ((Num) end).isInteger())
                return this;

        return new Err("range must consist of integers");
    }

    public String id() {
        return "range";
    }

    public String string() {
        return start.string() + ":" + end.string();
    }

    /* Logic Methods */

    public Range negate() {
        return new Range(end, start);
    }
}
