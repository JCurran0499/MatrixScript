package app.parser.interpreters.commands;

import app.parser.primitives.Err;
import resources.matrix.Matrix;
import app.parser.Interpreter;
import app.parser.Primitive;
import app.parser.primitives.Mat;
import app.parser.primitives.Num;
import app.parser.primitives.Tuple;

import java.math.BigDecimal;
import java.util.Arrays;

public class Dim implements Interpreter {

    private final Interpreter i;

    public Dim(Interpreter i) {
        this.i = i;
    }

    public Primitive solve() {
        Primitive p = i.solve();

        if (Err.is(p))
            return p;

        if (Mat.is(p)) {
            Matrix m = Mat.cast(p).mat();
            return new Tuple(Arrays.asList(new Interpreter[]
                    {new Num(BigDecimal.valueOf(m.rows())), new Num(BigDecimal.valueOf(m.cols()))}));
        }

        return new Err("invalid 'dim' command");
    }
}
