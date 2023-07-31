package Resources.Matrix.Exceptions;

public class MatrixOutOfBoundsException extends RuntimeException {

    public MatrixOutOfBoundsException() {
        super("Out of bounds for this matrix");
    }
}
