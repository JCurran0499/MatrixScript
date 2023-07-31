package Resources.Matrix.Exceptions;

public abstract class MatrixException extends RuntimeException {

    public MatrixException(String errMessage) {
        super(errMessage);
    }
}
