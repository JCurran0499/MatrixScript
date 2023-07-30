package Matrix;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixConstructorsTest {

    Matrix m;
    Matrix m2;

    @Test
    public void constructor1() {
        m = new Matrix(1, 1);
        assertEquals(m.rows(), 1);
        assertEquals(m.cols(), 1);
        assertTrue(m.equals(new Matrix("0 ")));

        m = new Matrix(2, 2);
        assertEquals(m.rows(), 2);
        assertEquals(m.cols(), 2);
        assertTrue(m.equals(new Matrix("0 0 ; 0 0 ")));

        m = new Matrix(4, 2);
        assertEquals(m.rows(), 4);
        assertEquals(m.cols(), 2);
        assertTrue(m.equals(new Matrix("0 0 ; 0 0; 0 0; 0 0 ")));

        m = new Matrix(5, 1);
        assertEquals(m.rows(), 5);
        assertEquals(m.cols(), 1);
        assertTrue(m.equals(new Matrix("0 ;0 ; 0; 0; 0 ")));
    }

    @Test
    public void constructor1UnhappyPath() {
        try {
            m = new Matrix(0, 0);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(0, 5);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(-3, 1);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(2, -5);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }
    }

    @Test
    public void constructor2() {
        m = new Matrix(new int[][] {{5, 6 ,7}, {2, 4, -5}});
        assertEquals(m.rows(), 2);
        assertEquals(m.cols(), 3);
        assertTrue(m.equals(new Matrix("5 6 7 ; 2 4 -5")));

        m = new Matrix(new int[][] {{5, 0}, {0, 0}, {1, 2}, {999, 56}});
        assertTrue(m.equals(new Matrix("5 0 ; 0 0 ; 1 2 ; 999 56")));
    }

    @Test
    public void constructor2UnhappyPath() {
        try {
            m = new Matrix(new int[][] {{}, {}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new int[][] {{2}, {}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new int[][] {{2, 3}, {4, 5}, {6, 7, 8}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }
    }

    @Test
    public void constructor3() {
        m = new Matrix(new double[][] {{5.785, 6 ,-7.3}, {2.222, 4.864, -5}});
        assertEquals(m.rows(), 2);
        assertEquals(m.cols(), 3);
        assertTrue(m.equals(new Matrix("5.785 6 -7.3 ; 2.222 4.864 -5")));
    }

    @Test
    public void constructor3UnhappyPath() {
        try {
            m = new Matrix(new double[][] {{}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new double[][] {{2.5838}, {}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new double[][] {{}, {5.46}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new double[][] {{2.23, 3}, {4, 5}, {6.67, 7, 8.891011}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new double[][] {null, {4, 5}, {6.67, 7}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new double[][] {{1, 2, 3}, {4, 5}, null});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }
    }

    @Test
    public void constructor4() {
        m = new Matrix(new BigDecimal[][] {
            {BigDecimal.valueOf(5.785), BigDecimal.valueOf(6), new BigDecimal("-7.3")},
            {BigDecimal.valueOf(2.222), BigDecimal.valueOf(4.864), BigDecimal.valueOf(-5)}
        });
        assertTrue(m.equals(new Matrix("5.785 6 -7.3 ; 2.222 4.864 -5")));
    }

    @Test
    public void constructor4UnhappyPath() {
        try {
            m = new Matrix(new BigDecimal[][] {{}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new BigDecimal[][] {{}, {BigDecimal.valueOf(5.46)}});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);}

        try {
            m = new Matrix(new BigDecimal[][] {
                {BigDecimal.valueOf(2.23), BigDecimal.valueOf(3)},
                {BigDecimal.valueOf(4), BigDecimal.valueOf(5)},
                {BigDecimal.valueOf(6.67), BigDecimal.valueOf(7), BigDecimal.valueOf(8.891011)}
            });
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new BigDecimal[][] {
                null,
                {BigDecimal.valueOf(4), BigDecimal.valueOf(5)},
                {BigDecimal.valueOf(5), BigDecimal.valueOf(7)}
            });
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }
    }

    @Test
    public void constructor4InvalidValues() {
        try {
            m = new Matrix(new BigDecimal[][] {{new BigDecimal("")}});
            fail();
        } catch (Exception e) {}

        try {
            m = new Matrix(new BigDecimal[][] {{new BigDecimal("invalid")}});
            fail();
        } catch (Exception e) {}

        try {
            m = new Matrix(new BigDecimal[][] {{new BigDecimal(" 5  5")}});
            fail();
        } catch (Exception e) {}
    }

    @Test
    public void constructor5() {
        m = new Matrix(new int[][] {{5, 6}, {3, 2}, {0, -9}});
        m2 = new Matrix("5 6 ; 3 2 ; 0  -9");

        assertTrue(new Matrix(m).equals(m));
        assertTrue(new Matrix(m).equals(m2));
        assertTrue(new Matrix(m2).equals(m));
    }

    @Test
    public void constructor5DeepCopy() {
        m = new Matrix("1 2 3 ; 4 5 6 ; 7 8 9");
        m2 = new Matrix(m);

        m2.setValue(0, 0, 0);
        assertFalse(m.equals(m2));
        assertEquals(m.getValue(0, 0), 1);
        assertEquals(m2.getValue(0, 0), 0);

        m.setValue(1, 1, 0);
        assertEquals(m.getValue(1, 1), 0);
        assertEquals(m2.getValue(1, 1), 5);
    }

    @Test
    public void constructor6() {
        m = new Matrix("5");
        assertTrue(m.equals(new Matrix(new int[][] {{5}})));

        m = new Matrix("0.365");
        assertTrue(m.equals(new Matrix(new double[][] {{0.365}})));
        assertTrue(m.equals(new Matrix(new BigDecimal[][]
            {{BigDecimal.valueOf(0.365)}}
        )));

        m = new Matrix("5 6 -123 4; 37 0 9 -3");
        assertTrue(m.equals(new Matrix(new int[][] {{5, 6, -123, 4}, {37, 0, 9, -3}})));
        assertEquals(m.rows(), 2);
        assertEquals(m.cols(), 4);
        assertFalse(m.isSquare());
    }

    @Test
    public void constructor6StrangeFormatting() {
        m = new Matrix("      5    ");
        assertTrue(m.equals(new Matrix(new int[][] {{5}})));

        m = new Matrix("  4  5   ;               2 3");
        assertTrue(m.equals(new Matrix(new int[][] {{4, 5}, {2, 3}})));
    }

    @Test
    public void constructor6UnhappyPathFormatting() {
        try {
            m = new Matrix("");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix("  ");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix(";");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix(" ; ");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix("5 ;   ; 6");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix(" 1 2 3 ; 4 5 6 ;");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix("; 1 2 ; 3 4");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }
    }

    @Test
    public void constructor6UnhappyPathTerms() {
        try {
            m = new Matrix("5 6 ; 4 invalid");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix("x");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix("5 6 5; 4 - 3");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }

        try {
            m = new Matrix("[5 6] ; 4 2");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_STRING.name);
        }
    }

    @Test
    public void constructor6UnhappyPathDimensions() {
        try {
            m = new Matrix("5 6 ; 4 ");
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }
    }

    @Test
    public void nullConstructor() {
        try {
            m = new Matrix((String) null);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.NULL_ARGUMENT.name);
        }

        try {
            m = new Matrix((int[][]) null);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.NULL_ARGUMENT.name);
        }

        try {
            m = new Matrix((double[][]) null);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.NULL_ARGUMENT.name);
        }

        try {
            m = new Matrix((BigDecimal[][]) null);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.NULL_ARGUMENT.name);
        }

        try {
            m = new Matrix((Matrix) null);
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.NULL_ARGUMENT.name);
        }
    }

    @Test
    public void improperArrayConstructor() {
        try {
            m = new Matrix(new int[][] {});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new double[][] {});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }

        try {
            m = new Matrix(new BigDecimal[][] {});
            fail();
        } catch (MatrixException e) {
            assertEquals(e.getMessage(), MatrixExceptionMessage.INVALID_DIMENSIONS.name);
        }
    }
}
