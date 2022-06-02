package Interpreters.Arithmetic;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.*;
import Matrix.Matrix;
import java.math.BigDecimal;
import java.math.MathContext;

public class Div implements Interpreter {
    private final Interpreter i1;
    private final Interpreter i2;

    public Div(Interpreter i1, Interpreter i2) {
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

        if (p1.id().equals("num") && p2.id().equals("num")) {
            BigDecimal n1 = ((Num) p1).num();
            BigDecimal n2 = ((Num) p2).num();
            try {
                return new Num(n1.divide(n2, MathContext.DECIMAL128));
            } catch (ArithmeticException e) {
                return new Err("cannot divide by 0");
            }
        }

        if (p1.id().equals("mat") && p2.id().equals("num")) {
            Matrix m = ((Mat) p1).mat();
            BigDecimal n = ((Num) p2).num();
            return new Mat(m.divide(n.doubleValue()));
        }

        else return new Err("invalid division");
    }

    public String id() {
        return "div";
    }
}
