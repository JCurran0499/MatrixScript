package Matrix;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class MatrixArithmeticTest {

    Matrix m;
    Matrix m2;

    @Test
    public void add() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new double[][] {
            {-1, 4, 10},
            {1.2, -8, 7}
        });
        assertTrue(m.add(m2).equals(new Matrix("0 7.5 7 ; 6.5 1 17")));

        m = new Matrix("5");
        m2 = new Matrix("-7");
        assertTrue(m.add(m2).equals(new Matrix("-2")));

        assertTrue(m.add(m2).equals(m2.add(m)));
    }

    @Test
    public void addChain() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new int[][] {
            {-1, 4, 10},
            {1, -8, 7}
        });
        assertTrue(m.add(m2).add(m2).add(m).equals(new Matrix("0 15 14 ; 12.6 2 34")));
    }

    @Test
    public void addUnhappyPath() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });

        assertNull(m.add(null));
        assertNull(m.add(new Matrix("1 2 3 4 ; 5 6 7 8")));
        assertNull(m.add(new Matrix("1 2 3 ; 4 5 6 ; 7 8 9")));
    }

    @Test
    public void subtract() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new double[][] {
            {-1, 4, 10},
            {1.2, -8, 7}
        });
        assertTrue(m.subtract(m2).equals(new Matrix("2 -0.5 -13 ; 4.1 17 3")));

        m = new Matrix("-5");
        m2 = new Matrix("-7");
        assertTrue(m.subtract(m2).equals(new Matrix("2")));

        assertFalse(m.subtract(m2).equals(m2.subtract(m)));
    }

    @Test
    public void subtractChain() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new int[][] {
            {-1, 4, 10},
            {1, -8, 7}
        });
        assertTrue(m.subtract(m2).subtract(m2).subtract(m).equals(new Matrix("2 -8 -20 ; -2 16 -14")));
    }

    @Test
    public void subtractUnhappyPath() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });

        assertNull(m.subtract(null));
        assertNull(m.subtract(new Matrix("1 2 3 4 ; 5 6 7 8")));
        assertNull(m.subtract(new Matrix("1 2 3 ; 4 5 6 ; 7 8 9")));
    }

    @Test
    public void addingNegatives() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        assertTrue(m.add(new Matrix("1 2 3 ; 4 5 6"))
            .equals(m.subtract(new Matrix("-1 -2 -3 ; -4 -5 -6")))
        );
    }

    @Test
    public void multiplyScalar() {
        m = new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10}
        });
        assertTrue(m.multiply(6).equals(new Matrix("6 18 -18 ; 30 54 60")));
        assertTrue(m.multiply(0.555).equals(new Matrix("0.555 1.665 -1.665 ; 2.775 4.995 5.55")));
    }
}
