package Resources.Matrix.Exceptions;

public class MatrixDimensionsException extends RuntimeException {

    public MatrixDimensionsException() {
        super("Invalid dimensions for defining a matrix");
    }
}
