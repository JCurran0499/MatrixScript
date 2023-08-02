package resources.matrix.exceptions;

import resources.matrix.exceptions.MatrixException;
import resources.matrix.exceptions.MatrixExceptionMessage;

public class MatrixStringException extends MatrixException {

    public MatrixStringException() {
        super(MatrixExceptionMessage.INVALID_STRING.name);
    }
}
