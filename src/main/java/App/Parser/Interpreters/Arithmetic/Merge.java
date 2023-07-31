package App.Parser.Interpreters.Arithmetic;

import App.Parser.Interpreters.Interpreter;
import App.Parser.Interpreters.Primitive;
import Resources.Matrix.Matrix;
import App.Parser.Interpreters.Primitives.Err;
import App.Parser.Interpreters.Primitives.Mat;
import App.Parser.Interpreters.Primitives.Num;
import App.Parser.Interpreters.Primitives.Range;

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
            Matrix m1 = ((Mat) p1).mat();
            Matrix m2 = ((Mat) p2).mat();

            return new Mat(m1.augment(m2)).solve();
        }

        if (p1.id().equals("num") && p2.id().equals("num")) {
            Num n1 = (Num) p1;
            Num n2 = (Num) p2;
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
