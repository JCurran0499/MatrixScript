package app.api.responses;

import lombok.Getter;
import lombok.NonNull;
import lombok.AllArgsConstructor;


@Getter
@AllArgsConstructor
public class ListSessionsResponse {
    private int sessionCount;
    private @NonNull SessionJson[] sessions;

    public record SessionJson(
            @NonNull String token,
            @NonNull String expiration
    ) {}
}
