package App.ResponsesJson;

public class TokenResponse {
    private final String sessionToken;

    public TokenResponse(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
