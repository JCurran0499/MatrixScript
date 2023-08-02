package resources.matrix.exceptions;

import resources.matrix.exceptions.MatrixException;
import resources.matrix.exceptions.MatrixExceptionMessage;

public class MatrixOutOfBoundsException extends MatrixException {

    public MatrixOutOfBoundsException() {
        super(MatrixExceptionMessage.OUT_OF_BOUNDS.name);
    }
}
