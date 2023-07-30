package Matrix;

public enum MatrixExceptionMessage {
    INVALID_DIMENSIONS("Invalid dimensions for defining a matrix"),
    INVALID_STRING("Invalid string for defining a matrix"),
    OUT_OF_BOUNDS("Out of bounds"),
    NULL_ARGUMENT("Invalid argument: null");

    public final String name;
    MatrixExceptionMessage(String errMessage) {
        name = errMessage;
    }
}
