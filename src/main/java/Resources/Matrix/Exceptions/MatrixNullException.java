package Resources.Matrix.Exceptions;

public class MatrixNullException extends RuntimeException {

    public MatrixNullException() {
        super("Invalid argument: null");
    }
}
