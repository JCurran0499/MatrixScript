package app.api.responses;


import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenResponse {
    private String status;
    private @NonNull String sessionToken;
}
