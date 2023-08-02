package resources.matrix.exceptions;

import resources.matrix.exceptions.MatrixException;
import resources.matrix.exceptions.MatrixExceptionMessage;

public class MatrixNullException extends MatrixException {

    public MatrixNullException() {
        super(MatrixExceptionMessage.NULL_ARGUMENT.name);
    }
}
