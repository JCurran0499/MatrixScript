package Program;


import org.jetbrains.annotations.Nullable;

public class Response {
    private final String status;
    @Nullable private final String response;
    @Nullable private final String matrix;
    @Nullable private final String errMessage;


    public Response(String status, @Nullable String response,
                    @Nullable String matrix, @Nullable String errMessage) {
        this.status = status;
        this.response = response;
        this.matrix = matrix;
        this.errMessage = errMessage;
    }

    public String getStatus() {
        return status;
    }

    @Nullable
    public String getResponse() {
        return response;
    }

    @Nullable
    public String getMatrix() {
        return matrix;
    }

    @Nullable
    public String getErrMessage() {
        return errMessage;
    }
}
