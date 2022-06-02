package Interpreters.Primitives;

import Interpreters.Primitive;
import Matrix.Matrix;

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
}
