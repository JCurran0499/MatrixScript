package Interpreters.Primitives;

import Interpreters.Primitive;
import java.math.BigDecimal;
import java.math.MathContext;
import Matrix.Matrix;

public class Num extends Primitive {
    private final BigDecimal num;

    public Num(BigDecimal n) {
        num = n;
    }

    /* Base Methods */

    public String id() {
        return "num";
    }

    public String string() {
        return num.toString();
    }

    /* Logic Methods */

    public BigDecimal num() {
        return num;
    }

    public Primitive add(Primitive a) {
        if (a.id().equals("num")) {
            BigDecimal n = ((Num) a).num;
            return new Num(num.add(n));
        }

        else return new Err("invalid addition");
    }

    public Primitive subtract(Primitive a) {
        if (a.id().equals("num")) {
            BigDecimal n = ((Num) a).num;
            return new Num(num.subtract(n));
        }

        else return new Err("invalid subtraction");
    }

    public Primitive multiply(Primitive a) {
        if (a.id().equals("num")) {
            BigDecimal n = ((Num) a).num;
            return new Num(num.multiply(n));
        }

        else if (a.id().equals("mat")) {
            Matrix m = ((Mat) a).mat();
            return new Mat(m.multiply(num.doubleValue()));
        }

        else return new Err("invalid multiplication");
    }

    public Primitive divide(Primitive a) {
        if (a.id().equals("num")) {
            BigDecimal n = ((Num) a).num;
            try {
                return new Num(num.divide(n, MathContext.DECIMAL128));
            } catch (ArithmeticException e) {
                return new Err("cannot divide by 0");
            }
        }

        else return new Err("invalid division");
    }

    public Primitive power(Primitive a) {
        if (a.id().equals("num") && ((Num) a).isInteger()) {
            BigDecimal n = ((Num) a).num;
            return new Num(num.pow(n.intValue()));
        }

        else return new Err("exponent must be an integer");
    }

    public Num negate() {
        return new Num(num.negate());
    }

    public boolean isInteger() {
        return (num.doubleValue() % 1 == 0);
    }
}
