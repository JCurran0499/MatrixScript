package App.ResponsesJson;

import App.Parser.Interpreters.Variables.SessionHandler;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SessionListResponse {
    private final int sessionCount;
    private final SessionJson[] sessions;

    public SessionListResponse(int sessionCount, List<String> sessionList) {
        this.sessionCount = sessionCount;

        this.sessions = new SessionJson[sessionCount];
        for (int i = 0; i < sessionCount; i++) {
            String s = sessionList.get(i);
            this.sessions[i] = new SessionJson(
                s, SessionHandler.getExpiration(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
        }
    }

    public class SessionJson {
        private final String token;
        private final String expiration;

        public SessionJson(String token, String expiration) {
            this.token = token;
            this.expiration = expiration;
        }

        public String getToken() {
            return token;
        }

        public String getExpiration() {
            return expiration;
        }
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public SessionJson[] getSessions() {
        return sessions;
    }
}
