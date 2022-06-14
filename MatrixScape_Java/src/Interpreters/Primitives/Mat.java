package Interpreters.Primitives;

import Interpreters.Primitive;
import Matrix.Matrix;

import java.math.BigDecimal;
import java.math.MathContext;

public class Mat extends Primitive {
    private final Matrix mat;

    public Mat(Matrix m) {
        mat = m;
    }

    /* Base Methods */

    public Primitive solve() {
        if (mat == null)
            return new Err("invalid matrix");

        return this;
    }

    public String id() {
        return "mat";
    }

    public String string() {
        return mat.printString();
    }

    /* Logic Methods */

    public Matrix mat() {
        return mat;
    }

    public Primitive add(Primitive a) {
        if (a.id().equals("mat")) {
            Matrix m = ((Mat) a).mat;
            return new Mat(mat.add(m));
        }

        else return new Err("invalid addition");
    }

    public Primitive subtract(Primitive a) {
        if (a.id().equals("mat")) {
            Matrix m = ((Mat) a).mat;
            return new Mat(mat.subtract(m));
        }

        else return new Err("invalid subtraction");
    }

    public Primitive multiply(Primitive a) {
        if (a.id().equals("num")) {
            BigDecimal n = ((Num) a).num();
            return new Mat(mat.multiply(n.doubleValue()));
        }

        else if (a.id().equals("mat")) {
            Matrix m = ((Mat) a).mat();
            return new Mat(mat.multiply(m));
        }

        else return new Err("invalid multiplication");
    }

    public Primitive divide(Primitive a) {
        if (a.id().equals("num")) {
            BigDecimal n = ((Num) a).num();
            return new Mat(mat.divide(n.doubleValue()));
        }

        else return new Err("invalid division");
    }

    public Primitive power(Primitive a) {
        if (a.id().equals("num") && ((Num) a).isInteger()) {
            BigDecimal n = ((Num) a).num();
            return new Mat(mat.toPower(n.intValue()));
        }

        else return new Err("exponent must be an integer");
    }

    public Mat negate() {
        return new Mat(mat.multiply(-1));
    }
}
