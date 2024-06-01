package app.api.responses;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private @NonNull String message;
}