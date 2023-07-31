package App.Parser.Interpreters.Commands;

import App.Parser.Interpreters.Primitives.Err;
import Resources.Matrix.Matrix;
import App.Parser.Interpreters.Interpreter;
import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Primitives.Mat;
import App.Parser.Interpreters.Primitives.Num;
import App.Parser.Interpreters.Primitives.Tuple;

import java.math.BigDecimal;
import java.util.Arrays;

public class Dim implements Interpreter {

    private final Interpreter i;

    public Dim(Interpreter i) {
        this.i = i;
    }

    public Primitive solve() {
        Primitive p = i.solve();

        if (p.id().equals("err"))
            return p;

        if (p.id().equals("mat")) {
            Matrix m = ((Mat) p).mat();
            return new Tuple(Arrays.asList(new Interpreter[]
                    {new Num(BigDecimal.valueOf(m.rows())), new Num(BigDecimal.valueOf(m.cols()))}));
        }

        return new Err("invalid 'dim' command");
    }

    public String id() { return "dim"; }
}
