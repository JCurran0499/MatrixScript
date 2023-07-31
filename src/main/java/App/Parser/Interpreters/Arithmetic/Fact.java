package App.Parser.Interpreters.Arithmetic;

import App.Parser.Interpreters.Interpreter;
import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Primitives.Err;
import App.Parser.Interpreters.Primitives.Num;

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
