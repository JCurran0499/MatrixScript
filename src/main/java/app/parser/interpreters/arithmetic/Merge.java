package app.parser.interpreters.arithmetic;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import resources.matrix.Matrix;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import app.parser.interpreters.primitives.Num;
import app.parser.interpreters.primitives.Range;

public class Merge implements Interpreter {

    private final Interpreter i1;
    private final Interpreter i2;

    public Merge(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }


    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        if (p1.id().equals("err"))
            return p1;
        if (p2.id().equals("err"))
            return p2;

        if (p1.id().equals("mat") && p2.id().equals("mat")) {
            Matrix m1 = Mat.cast(p1).mat();
            Matrix m2 = Mat.cast(p2).mat();

            return new Mat(m1.augment(m2)).solve();
        }

        if (p1.id().equals("num") && p2.id().equals("num")) {
            Num n1 = Num.cast(p1);
            Num n2 = Num.cast(p2);
            if (n1.isInteger() && n2.isInteger())
                return new Range(n1.num().intValue(), n2.num().intValue());
            else return new Err("range must consist of integers");
        }

        return new Err("invalid merge");
    }

    public String id() {
        return "merge";
    }

}
