package Interpreters.Arithmetic;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.*;
import Matrix.Matrix;

import java.math.BigDecimal;

public class Mult implements Interpreter {
    private final Interpreter i1;
    private final Interpreter i2;

    public Mult(Interpreter i1, Interpreter i2) {
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
            return new Num(n1.multiply(n2));
        }

        if (p1.id().equals("mat") && p2.id().equals("mat")) {
            Matrix m1 = ((Mat) p1).mat();
            Matrix m2 = ((Mat) p2).mat();
            return new Mat(m1.multiply(m2));
        }

        if (p1.id().equals("mat") && p2.id().equals("num")) {
            Matrix m = ((Mat) p1).mat();
            BigDecimal n = ((Num) p2).num();
            return new Mat(m.multiply(n.doubleValue()));
        }

        if (p2.id().equals("mat") && p1.id().equals("num")) {
            Matrix m = ((Mat) p2).mat();
            BigDecimal n = ((Num) p1).num();
            return new Mat(m.multiply(n.doubleValue()));
        }

        else return new Err("invalid multiplication");
    }

    public String id() {
        return "mult";
    }
}
