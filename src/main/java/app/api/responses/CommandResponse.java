package app.api.responses;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CommandResponse {
    private @NonNull String status;
    private String response;
    private String matrix;
    private String errMessage;
}

