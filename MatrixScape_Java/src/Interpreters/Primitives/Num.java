package Interpreters.Primitives;

import Interpreters.Primitive;
import java.math.BigDecimal;

public class Num extends Primitive {
    private final BigDecimal num;

    public Num(double n) {
        num = BigDecimal.valueOf(n);
    }

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


}
