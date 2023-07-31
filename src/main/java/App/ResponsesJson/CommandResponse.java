package App.ResponsesJson;


import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public record CommandResponse(String status, @Nullable String response, @Nullable String matrix,
                              @Nullable String errMessage) {
    /*public CommandResponse(String status, @Nullable String response,
                           @Nullable String matrix, @Nullable String errMessage) {
        this.status = status;
        this.response = response;
        this.matrix = matrix;
        this.errMessage = errMessage;
    }*/
}
