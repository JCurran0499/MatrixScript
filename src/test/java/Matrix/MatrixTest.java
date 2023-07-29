package Matrix;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MatrixTest {

    Matrix m;

    @Test
    public void identityMatrix() {
        m = Matrix.Identity(5);
        assertEquals(m.rows(), m.cols());
        assertEquals(m.rows(), 5);
        assertEquals(m.size(), 25);
        assertTrue(m.isSquare());
        assertTrue(m.equals(
            new Matrix("1 0 0 0 0 ; 0 1 0 0 0 ; 0 0 1 0 0 ; 0 0 0 1 0 ; 0 0 0 0 1")
        ));
    }
}
