package Matrix;

import static org.junit.jupiter.api.Assertions.*;

import Resources.Matrix.Matrix;
import org.junit.jupiter.api.Test;

public class MatrixAdvancedTest {

    Matrix m;
    Matrix m2;

    @Test
    public void transpose() {

    }

    @Test
    public void transposeReflexive() {
        m = Matrix.Identity(6);
        assertTrue(m.transpose().equals(m));
    }
}
