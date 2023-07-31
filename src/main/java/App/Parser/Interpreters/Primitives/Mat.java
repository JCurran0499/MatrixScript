package App.Parser.Interpreters.Primitives;

import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Interpreter;
import Resources.Matrix.Exceptions.MatrixException;
import Resources.Matrix.Matrix;
import java.math.BigDecimal;

public class Mat extends Primitive {
    private Matrix mat;

    public Mat(Matrix m) {
        mat = m;
    }

    public Mat(Interpreter[][] m) {
        StringBuilder matrixString = new StringBuilder();
        Primitive p;
        for (Interpreter[] row : m) {
            for (Interpreter i : row) {
                p = i.solve();

                if (p.id().equals("num")) {
                    matrixString.append(p.string());
                }

                if (p.id().equals("mat")) {
                    matrixString.append(p.string(), 1, p.string().length() - 1).append(" ; ");
                }

                if (p.id().equals("range")) {
                    for (int e : ((Range) p).fullRange())
                        matrixString.append(e).append(" ");
                }

                matrixString.append(" ");
            }

            matrixString.append(" ; ");
        }

        try {
            mat = new Matrix(matrixString.toString());
        } catch (MatrixException e) {
            mat = null;
        }
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

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return mat.equals(((Mat) p).mat);
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

    public Primitive get(Tuple t) {
        if (t.length() != 2)
            return new Err("invalid 'get' command");

        if (t.get(new Num(0)).id().equals("num") && t.get(new Num(1)).id().equals("num")) {
            Num r = (Num) t.get(new Num(0));
            Num c = (Num) t.get(new Num(1));
            if (!r.isInteger() || !c.isInteger())
                return new Err("integers are required to index a matrix");

            int rint = r.num().intValue();
            int cint = c.num().intValue();
            if (rint < 0 || rint >= mat.rows() || cint < 0 || cint >= mat.cols())
                return new Err("out of matrix bounds");

            return new Num(mat.getValue(rint, cint));
        }

        return null;
    }

    public Mat negate() {
        return new Mat(mat.multiply(-1));
    }
}
