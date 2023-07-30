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

    public Num(double n) {
        num = BigDecimal.valueOf(n);
    }

    /* Base Methods */

    public String id() {
        return "num";
    }

    public String string() {
        return num.toString();
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return num.compareTo(((Num) p).num) == 0;
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

    public Primitive factorial() {
        if (isInteger() && isPositive()) {
            BigDecimal factorial = BigDecimal.valueOf(1);
            for (BigDecimal b = num; b.compareTo(BigDecimal.ZERO) > 0; b = b.subtract(BigDecimal.ONE))
                factorial = factorial.multiply(b);

            return new Num(factorial);
        }

        return new Err("factorial must be a positive integer");
    }

    public Integer compareTo(Primitive a) {
        if (a.id().equals("num"))
            return num.compareTo(((Num) a).num);

        if (a.id().equals("range"))
            return num.compareTo(BigDecimal.valueOf(((Range) a).range()));

        return null;
    }

    public boolean isInteger() {
        return (num.doubleValue() % 1 == 0);
    }

    public boolean isPositive() { return (num.doubleValue() > 0); }
}
